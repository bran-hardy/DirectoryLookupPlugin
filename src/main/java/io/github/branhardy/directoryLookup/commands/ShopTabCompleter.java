package io.github.branhardy.directoryLookup.commands;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.services.NotionService;
import io.github.branhardy.directoryLookup.utils.FilterUtil;
import io.github.branhardy.directoryLookup.utils.ResponseUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ShopTabCompleter implements TabCompleter {

    private final NotionService notionService;
    private final String database;

    private List<String> cachedItems = new ArrayList<>();
    private long cacheExpiryTime = 0L;
    private static final long CACHE_DURATION = 600000; // 10 minutes in milliseconds

    public ShopTabCompleter(NotionService notionService, String database) {
        this.notionService = notionService;
        this.database = database;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (System.currentTimeMillis() > cacheExpiryTime) {
            refreshCache();
        }

        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);

            for (String item : cachedItems) {
                if (item.contains(input)) {
                    suggestions.add(item);
                }
            }
        }

        return suggestions;
    }

    private void refreshCache() {
        cacheExpiryTime = System.currentTimeMillis() + CACHE_DURATION;

        CompletableFuture.runAsync(() -> {
            String response = notionService.queryItems(database);
            cachedItems = response != null ? ResponseUtil.getItems(response) : new ArrayList<>();
        }).exceptionally(ex -> {
            DirectoryLookup.logger.severe(ex.getMessage());
            return null;
        });
    }
}
