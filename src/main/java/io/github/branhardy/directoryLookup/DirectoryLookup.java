package io.github.branhardy.directoryLookup;

import io.github.branhardy.directoryLookup.commands.ShopCommand;
import io.github.branhardy.directoryLookup.commands.ShopTabCompleter;
import io.github.branhardy.directoryLookup.services.NotionService;
import io.github.branhardy.directoryLookup.services.ShopService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * What to work on:
 *      - Error checking for...
 *          - No Spawn in database
 *              - If there is no spawn selected, don't display anything for that
 *          - no Coordinates in database
 *              - If there are no coordinates for the shop, say "No Coords"
 *      - Different Item spelling use cases
 *          - lighting_rod(s), end_rod(s), blaze_rod(s), fishing_rod(s)
 *          - diamond_tools, diamond_armor, horse_armor
 *              - these are not item specific, but the database uses these to clump together multiple tools and other stuff.
 *      - Currently no way to search for animals
 *          - This can be fixed in the tab completer
 */

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
        ShopService shopService = new ShopService(notionService, shopDatabase);

        this.getCommand("shop").setExecutor(new ShopCommand(shopService));
        this.getCommand("shop").setTabCompleter(new ShopTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
