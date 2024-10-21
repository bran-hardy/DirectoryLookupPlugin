package io.github.branhardy.directoryLookup.models;

import org.bukkit.ChatColor;

public class Shop {
    private final String name;
    private final String coordinates;
    private final String spawn;
    private final String owners;

    public Shop(String name, String coordinates, String spawn, String owners) {
        this.name = name;
        this.coordinates = coordinates.isEmpty() ? "No Coordinates" : coordinates;
        this.spawn = spawn.isEmpty() ? "No Spawn" : spawn;
        this.owners = owners.isEmpty() ? "No Owners" : owners;
    }

    public String getOwners() {
        return owners;
    }

    public String info() {
        return ChatColor.DARK_AQUA + name +
                ChatColor.WHITE + ": " +
                ChatColor.GREEN + coordinates +  ", " + spawn;
    }
}
