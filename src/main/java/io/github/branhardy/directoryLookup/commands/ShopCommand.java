package io.github.branhardy.directoryLookup.commands;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import io.github.branhardy.directoryLookup.models.Shop;
import io.github.branhardy.directoryLookup.services.ShopService;
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
            return false;
        }

        String targetItem = args[0].replace("minecraft:", "");

        DirectoryLookup.instance
                .getServer()
                .getScheduler()
                .runTaskAsynchronously(DirectoryLookup.instance, () -> {
                    DirectoryLookup.logger.info("Shop task scheduled");
                    List<Shop> shops = shopService.getShops(targetItem);

                    for (Shop shop : shops) {
                        sender.sendMessage(shop.info());
                    }

                    if (shops.isEmpty()) sender.sendMessage("No shops are selling the \"" + targetItem + "\" item");
                });

        return true;
    }
}
