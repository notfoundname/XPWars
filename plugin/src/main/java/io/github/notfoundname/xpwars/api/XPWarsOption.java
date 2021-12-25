package io.github.notfoundname.xpwars.api;

public enum XPWarsOption {

    FEATURES_PERMANENT_ACTION_BAR_MESSAGES("features.permanent_action_bar_message", false),
    FEATURES_GAMES_GUI("features.games_gui", false),
    FEATURES_HIDE_ENEMY_NAMETAGS("features.hide_enemy_nametags", false),
    FEATURES_KITS("features.kits", false),
    FEATURES_LEVEL_SYSTEM("features.level_system", false),
    FEATURES_MORE_SPECIAL_ITEMS("features.more_special_items", false),

    FEATURES_PERMANENT_ACTION_BAR_MESSAGES_MESSAGE_IN_LOBBY(FEATURES_PERMANENT_ACTION_BAR_MESSAGES.path + ".message_in_lobby", "Your team: %team_colored% ( %team_players% / %team_maxplayers% )"),
    FEATURES_PERMANENT_ACTION_BAR_MESSAGES_MESSAGE_IN_GAME(FEATURES_PERMANENT_ACTION_BAR_MESSAGES.path + ".message_in_game", "Your team: %bed% %team_colored% ( %team_players% / %team_maxplayers% )"),
    FEATURES_PERMANENT_ACTION_BAR_MESSAGES_MESSAGE_SPECTATOR(FEATURES_PERMANENT_ACTION_BAR_MESSAGES.path + ".message_spectator", "You died!"),

    FEATURES_GAMES_GUI_INVENTORY_TITLE(FEATURES_GAMES_GUI.path + ".inventory.title", "BW Games"),
    FEATURES_GAMES_GUI_INVENTORY_ROWS(FEATURES_GAMES_GUI.path + ".inventory.rows", 4),
    FEATURES_GAMES_GUI_INVENTORY_RENDER_ACTUAL_ROWS(FEATURES_GAMES_GUI.path + ".inventory.render_actual_rows", 6),
    FEATURES_GAMES_GUI_INVENTORY_RENDER_OFFSET(FEATURES_GAMES_GUI.path + ".inventory.render_offset", 9),
    FEATURES_GAMES_GUI_INVENTORY_RENDER_HEADER_START(FEATURES_GAMES_GUI.path + ".inventory.render_header_start", 0),
    FEATURES_GAMES_GUI_INVENTORY_RENDER_FOOTER_START(FEATURES_GAMES_GUI.path + ".inventory.render_footer_start", 45),
    FEATURES_GAMES_GUI_INVENTORY_ITEMS_ON_ROW(FEATURES_GAMES_GUI.path + ".inventory.items_on_row", 9),
    FEATURES_GAMES_GUI_INVENTORY_SHOW_PAGE_NUMBERS(FEATURES_GAMES_GUI.path + ".inventory.show_page_numbers", false),
    FEATURES_GAMES_GUI_INVENTORY_INVENTORY_TYPE(FEATURES_GAMES_GUI.path + ".inventory.inventory_type", "CHEST"),

    FEATURES_KITS_INVENTORY_TITLE(FEATURES_KITS.path + ".inventory.title", "BW Games"),
    FEATURES_KITS_INVENTORY_ROWS(FEATURES_KITS.path + ".inventory.rows", 4),
    FEATURES_KITS_INVENTORY_RENDER_ACTUAL_ROWS(FEATURES_KITS.path + ".inventory.render_actual_rows", 6),
    FEATURES_KITS_INVENTORY_RENDER_OFFSET(FEATURES_KITS.path + ".inventory.render_offset", 9),
    FEATURES_KITS_INVENTORY_RENDER_HEADER_START(FEATURES_KITS.path + ".inventory.render_header_start", 0),
    FEATURES_KITS_INVENTORY_RENDER_FOOTER_START(FEATURES_KITS.path + ".inventory.render_footer_start", 45),
    FEATURES_KITS_INVENTORY_ITEMS_ON_ROW(FEATURES_KITS.path + ".inventory.items_on_row", 9),
    FEATURES_KITS_INVENTORY_SHOW_PAGE_NUMBERS(FEATURES_KITS.path + ".inventory.show_page_numbers", false),
    FEATURES_KITS_INVENTORY_INVENTORY_TYPE(FEATURES_KITS.path + ".inventory.inventory_type", "CHEST"),
    ;


    public final String path;
    public final Object defaultValue;

    XPWarsOption(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object value) {

    }

}
