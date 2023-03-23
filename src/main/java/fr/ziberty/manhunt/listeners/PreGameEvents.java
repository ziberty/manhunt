package fr.ziberty.manhunt.listeners;

import fr.ziberty.manhunt.Manhunt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PreGameEvents implements Listener {
    private Manhunt main;
    public PreGameEvents(Manhunt manhunt) {
        this.main = manhunt;
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent event) {
        if (!main.hasGameStarted()) event.setCancelled(true);
    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent event) {
        if (!main.hasGameStarted()) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        if (!main.hasGameStarted()) event.setCancelled(true);
    }

}
