package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.listeners.TrackingListener;
import fr.ziberty.manhunt.tasks.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class InventoryListener implements Listener {
    private Manhunt main;
    private TrackingListener trackingListener;
    public InventoryListener(Manhunt manhunt, TrackingListener trackingListener) {
        this.main = manhunt;
        this.trackingListener = trackingListener;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        if (event.getView().getTitle().equalsIgnoreCase("Configuration Manhunt")) {
            event.setCancelled(true);
            switch (itemMeta.getDisplayName()) {
                case "§bSpeedrunners":
                    player.openInventory(new SpeedrunnersInventory(main).getInventory());
                    break;
                case "§aCommencer":
                    if (main.getSpeedrunnersList().isEmpty()) {
                        player.sendMessage("§cAucun speedrunner n'a été choisi");
                    } else if (main.getSpeedrunnersList().size() == Bukkit.getOnlinePlayers().size()) {
                        player.sendMessage("§cAucun hunter n'a été choisi");
                    } else {
                        launchGame();
                    }
                    break;
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("Speedrunners")) {
            event.setCancelled(true);
            switch (itemStack.getType()) {
                case PLAYER_HEAD:
                    Player itemPlayer = Bukkit.getPlayer(itemMeta.getDisplayName());
                    if (main.isPlayerSpeedrunner(player)) {
                        main.getSpeedrunnersList().remove(player);
                    } else {
                        main.getSpeedrunnersList().add(player);
                    }
                    player.openInventory(new SpeedrunnersInventory(main).getInventory());
                    break;
                case BARRIER:
                    player.openInventory(main.getConfigInventory().getInventory());
                    break;
            }
        }
    }

    private void launchGame() {
        HashMap<Player, Integer> playerTrackingMap = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!main.isPlayerSpeedrunner(p)) {
                playerTrackingMap.put(p, 0);
            }
            p.teleport(main.getSpeedrunnersList().get(0));
            p.sendTitle("§aLe manhunt", "§acommence", 20, 20, 20);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
        trackingListener.setPlayerTrackingMap(playerTrackingMap);
        GameTimer gameTimer = new GameTimer(main, trackingListener);
        gameTimer.runTaskTimer(main, 0, 1);
        main.setGameStarted(true);
    }
}
