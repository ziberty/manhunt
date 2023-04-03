package fr.ziberty.manhunt.tasks;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.listeners.TrackingListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {
    private Manhunt main;
    private TrackingListener trackingListener;
    private int tick = 0;

    public GameTimer(Manhunt manhunt, TrackingListener trackingListener) {
        this.main = manhunt;
        this.trackingListener = trackingListener;
        this.trackingListener.setPlayersFirstLocations();
    }
    @Override
    public void run() {
        if (tick == 0) {
            main.setHuntersBlocked(true);
        }
        if (tick == 30*20) {
            main.setHuntersBlocked(false);
            Bukkit.broadcastMessage("§6Les hunters sont libres !");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!main.isPlayerSpeedrunner(player)) {
                int playerTrackingNumber = trackingListener.getPlayerTrackingMap().get(player);
                Player trackedPlayer = main.getSpeedrunnersList().get(playerTrackingNumber);
                switch (player.getLocation().getWorld().getEnvironment()) {
                    case NORMAL:
                        player.setCompassTarget(trackingListener.getLastKnownSpeedrunnerLocationInOverworld().get(trackedPlayer));
                        setTrackingLodestoneCompassDirection(player, trackingListener.getLastKnownSpeedrunnerLocationInOverworld().get(trackedPlayer));
                        break;
                    case NETHER:
                        if (!trackingListener.hasPlayerVisitedNether(trackedPlayer)) {
                            setTrackingLodestoneCompassDirection(player, trackingListener.getPlayerNetherPortalLocation().get(player));
                        } else {
                            setTrackingLodestoneCompassDirection(player, trackingListener.getLastKnownSpeedrunnerLocationInNether().get(trackedPlayer));
                        }
                        break;
                    case THE_END:
                        if (!trackingListener.hasPlayerVisitedEnd(trackedPlayer)) {
                            setTrackingLodestoneCompassDirection(player, trackingListener.getPlayerEndPortalLocation().get(player));
                        } else {
                            setTrackingLodestoneCompassDirection(player, trackingListener.getLastKnownSpeedrunnerLocationInEnd().get(trackedPlayer));
                        }
                        break;
                }
            } else {
                switch (player.getLocation().getWorld().getEnvironment()) {
                    case NORMAL:
                        trackingListener.getLastKnownSpeedrunnerLocationInOverworld().put(player, player.getLocation());
                        break;
                    case NETHER:
                        trackingListener.getLastKnownSpeedrunnerLocationInNether().put(player, player.getLocation());
                        break;
                    case THE_END:
                        trackingListener.getLastKnownSpeedrunnerLocationInEnd().put(player, player.getLocation());
                        break;
                }
            }
        }
        tick++;
    }

    private void setTrackingLodestoneCompassDirection(Player player, Location targetLoc) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.COMPASS) {
                CompassMeta meta = (CompassMeta) itemStack.getItemMeta();
                meta.setLodestone(targetLoc);
                meta.setLodestoneTracked(false);
                itemStack.setItemMeta(meta);
            }
        }
    }
}
