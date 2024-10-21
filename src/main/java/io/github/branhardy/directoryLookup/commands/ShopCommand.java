package io.github.branhardy.directoryLookup.commands;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.services.NotionService;
import io.github.branhardy.directoryLookup.utils.FilterUtil;
import io.github.branhardy.directoryLookup.utils.ResponseUtil;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ShopCommand implements CommandExecutor {

    private final NotionService notionService;
    private final String databaseId;

    public ShopCommand(NotionService notionService, String databaseId) {
        this.notionService = notionService;
        this.databaseId = databaseId;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        String targetItem = args[0].replace("minecraft:", "");

        // Run task in separate thread as to not bottleneck the main server if it's stuck waiting for a response
        DirectoryLookup.instance
                .getServer()
                .getScheduler()
                .runTaskAsynchronously(DirectoryLookup.instance, () -> executeShopTask(sender, targetItem));

        return true;
    }

    public void executeShopTask(CommandSender sender, String targetItem) {
        DirectoryLookup.logger.info("Shop task scheduled");

        // Setup Notion query filter, this is a list because some items are grouped together under one name
        // i.e. "Sherd" is added for all sherd variants
        List<String> filter = FilterUtil.setupNotionFilter(targetItem);

        // Query Notion database using the generated filter
        String response = notionService.queryDatabase(databaseId, filter);

        // Take the response and format it into usable content
        List<Shop> shops = ResponseUtil.getShops(response);

        sendShopCountInfo(sender, filter, shops);
        sendShopInfo(sender, shops);
    }

    public void sendShopCountInfo(CommandSender sender, List<String> filter, List<Shop> shops) {
        String shopSuffix = shops.size() == 1 ? "shop" : "shops";

        if (shops.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No shops are selling the \"" + filter.getFirst() + "\" item");

            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Found " +
                        ChatColor.YELLOW + shops.size() +
                        ChatColor.GOLD + " " + shopSuffix);

        // If the filter contains a helper filter (i.e. generalized "dandelion" to "flowers"), let the
        // player know that some shops may not sell the exact item
        if (filter.size() > 1) {
            sender.sendMessage(
                    ChatColor.GRAY + "" +
                            ChatColor.ITALIC +
                            "Note: The " + shopSuffix + " may not sell the exact item, but have similar items stock"
            );
        }
    }

    public void sendShopInfo(CommandSender sender, List<Shop> shops) {
        for (Shop shop : shops) {
            TextComponent message = new TextComponent(shop.info());
            String ownerSuffix = shop.getOwners().contains(",") ? "Owners: " : "Owner: ";

            message.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new Text(ownerSuffix + shop.getOwners())
            ));

            sender.spigot().sendMessage(message);
        }
    }
}
