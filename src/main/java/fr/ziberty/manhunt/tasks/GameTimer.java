package fr.ziberty.manhunt.tasks;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.listeners.TrackingListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {
    private Manhunt main;
    private TrackingListener trackingListener;
    public GameTimer(Manhunt manhunt, TrackingListener trackingListener) {
        this.main = manhunt;
        this.trackingListener = trackingListener;
    }
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!main.isPlayerSpeedrunner(player)) {
                int playerTrackingNumber = trackingListener.getPlayerTrackingMap().get(player);
                player.setCompassTarget(main.getSpeedrunnersList().get(playerTrackingNumber).getLocation());
            }
        }
    }
}
