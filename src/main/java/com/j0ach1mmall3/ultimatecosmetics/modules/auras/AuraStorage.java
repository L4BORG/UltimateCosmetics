package com.j0ach1mmall3.ultimatecosmetics.modules.auras;


import com.j0ach1mmall3.jlib.inventory.JLibItem;
import com.j0ach1mmall3.ultimatecosmetics.api.storage.CosmeticStorage;
import org.bukkit.ChatColor;

/**
 * @author j0ach1mmall3 (business.j0ach1mmall3@gmail.com)
 * @since 6/03/2016
 */
public final class AuraStorage extends CosmeticStorage {
    private final Color color;

    public AuraStorage(String identifier, String permission, JLibItem jlibItem, Color color) {
        super(identifier, permission, jlibItem);
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public enum Color {
        BLACK(ChatColor.COLOR_CHAR + "0"),
        DARK_BLUE(ChatColor.COLOR_CHAR + "1"),
        DARK_GREEN(ChatColor.COLOR_CHAR + "2"),
        DARK_AQUA(ChatColor.COLOR_CHAR + "3"),
        DARK_RED(ChatColor.COLOR_CHAR + "4"),
        DARK_PURPLE(ChatColor.COLOR_CHAR + "5"),
        GOLD(ChatColor.COLOR_CHAR + "6"),
        GRAY(ChatColor.COLOR_CHAR + "7"),
        DARK_GRAY(ChatColor.COLOR_CHAR + "8"),
        BLUE(ChatColor.COLOR_CHAR + "9"),
        GREEN(ChatColor.COLOR_CHAR + "a"),
        AQUA(ChatColor.COLOR_CHAR + "b"),
        RED(ChatColor.COLOR_CHAR + "c"),
        LIGHT_PURPLE(ChatColor.COLOR_CHAR + "d"),
        YELLOW(ChatColor.COLOR_CHAR + "e"),
        WHITE(ChatColor.COLOR_CHAR + "f"),
        RAINBOW(ChatColor.COLOR_CHAR + "0");
        private final String asString;

        Color(String asString) {
            this.asString = asString;
        }

        public String asString() {
            return this.asString;
        }
    }
}
