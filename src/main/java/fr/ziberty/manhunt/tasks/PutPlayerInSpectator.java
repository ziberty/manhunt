package fr.ziberty.manhunt.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PutPlayerInSpectator extends BukkitRunnable {

    private int tick = 0;
    private Player player;

    public PutPlayerInSpectator(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (tick == 1) {
            player.setGameMode(GameMode.SPECTATOR);
            cancel();
        }
        tick++;
    }
}
