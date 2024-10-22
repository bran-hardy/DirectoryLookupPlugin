package io.github.branhardy.directoryLookup;

import io.github.branhardy.directoryLookup.commands.ShopCommand;
import io.github.branhardy.directoryLookup.commands.ShopTabCompleter;
import io.github.branhardy.directoryLookup.services.NotionService;
import io.github.branhardy.directoryLookup.utils.BeaconMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

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

        getServer().getPluginManager().registerEvents(new BeaconMessageListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
