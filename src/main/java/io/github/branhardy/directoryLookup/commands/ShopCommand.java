package io.github.branhardy.directoryLookup.commands;

import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.services.ShopService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShopCommand implements CommandExecutor {

    private final ShopService shopService;

    public ShopCommand(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "You can only search for one item");

            return true;
        }

        String targetItem = args[0].replace("minecraft:", "");

        shopService.getShops().thenApply(result -> {
            StringBuilder message = new StringBuilder();

            for (Shop shop : result) {
                if (shop.hasItem(targetItem)) message.append(shop.info());
            }

            if (result.isEmpty()) message.append("No shops are selling the \"").append(targetItem).append("\" item");

            sender.sendMessage(message.toString());

            return null; // Why do I have to return null here?
        }).exceptionally(ex -> {
            sender.sendMessage(ChatColor.RED + "An error occurred while fetching shop data");
            return null;
        });

        return true;
    }
}
