package io.github.branhardy.directoryLookup.utils;

import io.github.branhardy.directoryLookup.DirectoryLookup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BeaconMessageListener implements Listener {


    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();  // Get the command message (e.g., "/showBeacon:x:z")

        // Check if it starts with your custom command
        if (message.startsWith("/directorylookupbeacon:")) {
            event.setCancelled(true);  // Cancel the command so it doesn't show as an error

            // Extract the coordinates from the message ("/showBeacon:x:z")
            String coords = message.replace("/directorylookupbeacon:", "");

            // Now send this as a beacon message using the custom messaging channel
            BeaconUtil.handleBeaconCommand(event.getPlayer(), coords);  // Send the data to the server
            DirectoryLookup.logger.info("sendBeaconMessage");
        }
    }
}

