package com.notfoundname.xpwars.utils;

import com.notfoundname.xpwars.XPWars;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EtcUtils {

    public static List<File> shopFiles = Arrays.stream(Objects.requireNonNull(Main.getInstance().getDataFolder().listFiles()))
            .filter(file -> file.isFile() && !Arrays.asList("config.yml", "sign.yml", "record.yml").contains(file.getName()))
            .collect(Collectors.toList());

    public static List<String> playerNames = XPWars.getInstance().getServer().getOnlinePlayers().stream()
            .map(Player::getName).collect(Collectors.toList());

}
