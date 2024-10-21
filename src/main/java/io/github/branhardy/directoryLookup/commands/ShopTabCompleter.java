package io.github.branhardy.directoryLookup.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            String input = args[0].toUpperCase(Locale.ROOT);

            // Add all items containing the input to the suggestions
            for (Material material : Material.values()) {
                if (material.name().contains(input)) {
                    suggestions.add("minecraft:" + material.name().toLowerCase(Locale.ROOT));
                }
            }

            // Add all entities containing the input to the suggestions
            for (EntityType entityType : EntityType.values()) {
                if (entityType.name().contains(input)) {
                    suggestions.add("minecraft:" + entityType.name().toLowerCase(Locale.ROOT));
                }
            }
        }

        return suggestions;
    }
}
