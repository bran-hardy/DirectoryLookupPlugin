package io.github.branhardy.directoryLookup;

import io.github.branhardy.directoryLookup.commands.ShopCommand;
import io.github.branhardy.directoryLookup.commands.ShopTabCompleter;
import io.github.branhardy.directoryLookup.services.NotionService;
import io.github.branhardy.directoryLookup.utils.FilterUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class DirectoryLookup extends JavaPlugin {

    public static DirectoryLookup instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;
        logger = this.getLogger();

        saveDefaultConfig();

        String apiUrl = getConfig().getString("notion.api-url");
        String apiKey = getConfig().getString("notion.api-key");
        String apiVersion = getConfig().getString("notion.version");

        String shopDatabase = getConfig().getString("notion.database.shop");

        NotionService notionService = new NotionService(apiUrl, apiKey, apiVersion);

        this.getCommand("shop").setExecutor(new ShopCommand(notionService, shopDatabase));
        this.getCommand("shop").setTabCompleter(new ShopTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
