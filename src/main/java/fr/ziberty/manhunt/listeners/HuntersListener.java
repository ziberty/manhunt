package fr.ziberty.manhunt.listeners;

import fr.ziberty.manhunt.Manhunt;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class HuntersListener implements Listener {

    private Manhunt main;

    public HuntersListener(Manhunt manhunt) {
        this.main = manhunt;
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!main.areHuntersBlocked() && main.isPlayerSpeedrunner(player)) return;
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            if (main.areHuntersBlocked() && !main.isPlayerSpeedrunner(player)) {
                player.teleport(new Location(player.getWorld(), event.getFrom().getBlockX(), event.getFrom().getBlockY(), event.getFrom().getBlockZ(), event.getFrom().getYaw(), event.getFrom().getPitch()));
            }
        }
    }

}
