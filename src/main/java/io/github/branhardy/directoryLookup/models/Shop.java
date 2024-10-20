package io.github.branhardy.directoryLookup.models;

import org.bukkit.ChatColor;

import java.util.List;

public class Shop {
    private final String name;
    private final String coordinates;
    private final String spawn;

    public Shop(String name, String coordinates, String spawn) {
        this.name = name;
        this.coordinates = coordinates.isEmpty() ? "No Coordinates" : coordinates;
        this.spawn = spawn.isEmpty() ? "No Spawn" : spawn;
    }

    public String info() {
        return ChatColor.DARK_AQUA + name +
                ChatColor.WHITE + ": " +
                ChatColor.GOLD + coordinates +
                ChatColor.WHITE + ", " +
                ChatColor.GREEN + spawn;
    }
}
