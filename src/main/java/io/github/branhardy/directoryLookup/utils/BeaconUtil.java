package io.github.branhardy.directoryLookup.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import io.github.branhardy.directoryLookup.DirectoryLookup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class BeaconUtil implements Listener {

    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    public static void showBeaconBeam(Player player, Location location, int durationTicks) {
        DirectoryLookup.logger.info("call showBeaconBeam");
        // Send the fake block to the client to display a beacon beam
        sendFakeBlock(player, location, Material.BEACON);
//
//        Particles
//        sendBeaconBeamParticles(player, location);

        new BukkitRunnable() {
            @Override
            public void run() {
                sendFakeBlock(player, location, Material.AIR);  // Remove the beacon after the duration
//
//                Particles
//                removeBeaconBeamParticles(player, location);
            }
        }.runTaskLater(DirectoryLookup.instance, durationTicks);
    }

    private static void sendFakeBlock(Player player, Location location, Material material) {
        DirectoryLookup.logger.info("receive sendFakeBlock");
        // ProtocolLib packet for sending fake block change
        PacketContainer packet = protocolManager.createPacket(
                com.comphenix.protocol.PacketType.Play.Server.BLOCK_CHANGE
        );

        packet.getBlockPositionModifier().write(0, new BlockPosition(
                location.getBlockX(), location.getBlockY(), location.getBlockZ()
        ));
        packet.getBlockData().write(0, WrappedBlockData.createData(material));

        protocolManager.sendServerPacket(player, packet);
    }

    private static void sendBeaconBeamParticles(Player player, Location location) {
        // Send a particle effect that represents a beacon beam
        player.spawnParticle(Particle.ELECTRIC_SPARK, location.clone().add(0.5, 0, 0.5), 1, 0, 0, 0, 0);
        for (int y = 1; y <= 50; y++) { // Adjust height as needed
            player.spawnParticle(Particle.EXPLOSION, location.clone().add(0.5, y, 0.5), 1, 0, 0, 0, 0);
        }
    }

    private static void removeBeaconBeamParticles(Player player, Location location) {
        // Optionally, remove particles if needed, or just allow them to disappear naturally
        // Minecraft handles particle cleanup automatically after a while
        DirectoryLookup.logger.info("Removing beacon beam particles for player: " + player.getName());
    }

    public static void handleBeaconCommand(Player player, String coordinates) {
        String[] parts = coordinates.split(":");
        if (parts.length == 2) {
            try {
                double x = Double.parseDouble(parts[0].trim());
                double z = Double.parseDouble(parts[1].trim());

                // Handle the coordinates (show the beacon beam)
                Location location = new Location(player.getWorld(), x, 63, z);  // Y is set to 64 by default
                showBeaconBeam(player, location, 200);  // Show the beacon for 200 ticks
            } catch (NumberFormatException e) {
                DirectoryLookup.logger.severe("Failed to parse coordinates from message: " + coordinates);
            }
        } else {
            DirectoryLookup.logger.severe("Invalid coordinates format: " + coordinates);
        }
    }



}
