package nfn11.thirdparty.connorlinfoot.actionbarapi;

import nfn11.xpwars.XPWars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarAPI implements Listener {
    private static Plugin plugin;
    private static String nmsver;
    private static boolean useOldMethods = false;

    public ActionBarAPI() {

        nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);

        if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_")) { // Not sure if 1_7 works for the
                                                                                // protocol hack?
            useOldMethods = true;
        }

        Bukkit.getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    public static void sendActionBar(Player player, String message) {
        if (!player.isOnline()) {
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(player.getUniqueId()), message);
        }

        // Call the event, if cancelled don't send Action Bar
        ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, message);
        Bukkit.getPluginManager().callEvent(actionBarMessageEvent);
        if (actionBarMessageEvent.isCancelled())
            return;
        if (Bukkit.getVersion()
                .contains("1.16")/* this api is broken on 1.16 and right now I have no idea how to fix this */) {
            player.sendActionBar(message);
        } else {
            try {
                Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
                Object craftPlayer = craftPlayerClass.cast(player);
                Object packet;
                Class<?> packetPlayOutChatClass = Class
                        .forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
                Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
                if (useOldMethods) {
                    Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                    Class<?> iChatBaseComponentClass = Class
                            .forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                    Method m3 = chatSerializerClass.getDeclaredMethod("a", String.class);
                    Object cbc = iChatBaseComponentClass
                            .cast(m3.invoke(chatSerializerClass, "{\"text\": \"" + message + "\"}"));
                    packet = packetPlayOutChatClass
                            .getConstructor(new Class<?>[] { iChatBaseComponentClass, byte.class })
                            .newInstance(cbc, (byte) 2);
                } else {
                    Class<?> chatComponentTextClass = Class
                            .forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                    Class<?> iChatBaseComponentClass = Class
                            .forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                    try {
                        Class<?> chatMessageTypeClass = Class
                                .forName("net.minecraft.server." + nmsver + ".ChatMessageType");
                        Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                        Object chatMessageType = null;
                        for (Object obj : chatMessageTypes) {
                            if (obj.toString().equals("GAME_INFO")) {
                                chatMessageType = obj;
                            }
                        }
                        Object chatCompontentText = chatComponentTextClass
                                .getConstructor(new Class<?>[] { String.class }).newInstance(message);
                        packet = packetPlayOutChatClass
                                .getConstructor(new Class<?>[] { iChatBaseComponentClass, chatMessageTypeClass })
                                .newInstance(chatCompontentText, chatMessageType);
                    } catch (ClassNotFoundException cnfe) {
                        Object chatCompontentText = chatComponentTextClass
                                .getConstructor(new Class<?>[] { String.class }).newInstance(message);
                        packet = packetPlayOutChatClass
                                .getConstructor(new Class<?>[] { iChatBaseComponentClass, byte.class })
                                .newInstance(chatCompontentText, (byte) 2);
                    }
                }
                Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
                Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
                Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
                Object playerConnection = playerConnectionField.get(craftPlayerHandle);
                Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendActionBar(final Player player, final String message, int duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than
            // 3 seconds, ensures precision.
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(plugin, duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's
        // screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(plugin, (long) duration);
        }
    }
}