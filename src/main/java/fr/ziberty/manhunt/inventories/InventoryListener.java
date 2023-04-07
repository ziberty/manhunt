package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.listeners.TrackingListener;
import fr.ziberty.manhunt.tasks.GameTimer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                    if (main.isPlayerSpeedrunner(itemPlayer)) {
                        main.getSpeedrunnersList().remove(itemPlayer);
                    } else {
                        main.getSpeedrunnersList().add(itemPlayer);
                    }
                    player.openInventory(new SpeedrunnersInventory(main).getInventory());
                    break;
                case BARRIER:
                    player.openInventory(main.getConfigInventory().getInventory());
                    break;
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("Ressusciter un allié")) {
            event.setCancelled(true);
            switch (itemStack.getType()) {
                case PLAYER_HEAD:
                    Player playerToRevive = Bukkit.getPlayer(itemMeta.getDisplayName());
                    revivePlayer(playerToRevive);
                    player.getInventory().remove(Material.SEA_PICKLE);
                    Bukkit.broadcastMessage("§c" + playerToRevive.getDisplayName() + " §ea été ressuscité !");
                    player.closeInventory();
                    break;
                case BARRIER:
                    player.closeInventory();
                    break;
            }
        }
    }

    private void launchGame() {
        HashMap<Player, Integer> playerTrackingMap = new HashMap<>();
        List<String> speedrunnerNames = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().clear();
            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
            if (!main.isPlayerSpeedrunner(p)) {
                playerTrackingMap.put(p, 0);
            } else {
                speedrunnerNames.add(p.getDisplayName());
            }
            p.teleport(main.getSpeedrunnersList().get(0));
            p.sendTitle("§aLe manhunt", "§acommence", 20, 20, 20);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle true");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
        Bukkit.broadcastMessage("§eLes speedrunners sont : §c" + String.join(", ", speedrunnerNames));
        trackingListener.setPlayerTrackingMap(playerTrackingMap);
        GameTimer gameTimer = new GameTimer(main, trackingListener);
        gameTimer.runTaskTimer(main, 0, 1);
        main.setGameStarted(true);
    }

    private void revivePlayer(Player player) {
        player.teleport(main.getSpeedrunnersList().get(0).getLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
    }
}
