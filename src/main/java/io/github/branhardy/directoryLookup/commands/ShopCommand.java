package io.github.branhardy.directoryLookup.commands;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.services.ShopService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

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

        // Refactor to utilize Bukkit.getScheduler().runTaskAsynchronously() command instead
        DirectoryLookup.instance
                .getServer()
                .getScheduler()
                .runTaskAsynchronously(DirectoryLookup.instance, () -> {
                    DirectoryLookup.logger.info("Shop task scheduled");
                    List<Shop> shops = shopService.getShops(targetItem);
                    StringBuilder message = new StringBuilder();

                    for (Shop shop : shops) {
                        if (shop.hasItem(targetItem)) message.append(shop.info());
                    }

                    if (shops.isEmpty()) message.append("No shops are selling the \"").append(targetItem).append("\" item");

                    sender.sendMessage(message.toString());
                });

        return true;
    }
}
