package io.github.branhardy.directoryLookup.models;

import org.bukkit.ChatColor;

import java.util.List;

public class Shop {
    private final String name;
    private final List<String> inventory;
    private final String coordinates;
    private final String spawn;

    public Shop(String name, List<String> inventory, String coordinates, String spawn) {
        this.name = name;
        this.inventory = inventory;
        this.coordinates = coordinates;
        this.spawn = spawn;
    }

    public boolean hasItem(String searchedItem) {
        return inventory.contains(searchedItem);
    }

    public String info() {
        return ChatColor.DARK_AQUA + name +
                ChatColor.WHITE + ": " +
                ChatColor.GOLD + coordinates +
                ChatColor.WHITE + ", " +
                ChatColor.GREEN + spawn;
    }
}
