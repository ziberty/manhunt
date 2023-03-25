package fr.ziberty.manhunt;

import fr.ziberty.manhunt.commands.Config;
import fr.ziberty.manhunt.crafts.RespawnSpeedrunnerItem;
import fr.ziberty.manhunt.inventories.ConfigInventory;
import fr.ziberty.manhunt.inventories.InventoryListener;
import fr.ziberty.manhunt.listeners.PreGameEvents;
import fr.ziberty.manhunt.listeners.RespawnItemListener;
import fr.ziberty.manhunt.listeners.TrackingListener;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Manhunt extends JavaPlugin {

    private ConfigInventory configInventory;

    private List<Player> speedrunnersList = new ArrayList<>();

    private TrackingListener trackingListener;

    private boolean gameStarted;

    public ConfigInventory getConfigInventory() {
        return configInventory;
    }

    private void setConfigInventory(ConfigInventory configInventory) {
        this.configInventory = configInventory;
    }

    public List<Player> getSpeedrunnersList() {
        return speedrunnersList;
    }

    public TrackingListener getTrackingListener() {
        return trackingListener;
    }

    private void setTrackingListener(TrackingListener trackingListener) {
        this.trackingListener = trackingListener;
    }

    public boolean hasGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isPlayerSpeedrunner(Player player) {
        return this.speedrunnersList.contains(player);
    }

    @Override
    public void onEnable() {
        setupWorld();
        setConfigInventory(new ConfigInventory());
        setTrackingListener(new TrackingListener(this));
        loadCommands();
        loadListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupWorld() {
        Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);
        Bukkit.getWorld("world").setTime(0);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDaylightCycle false");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doFireTick false");
        RespawnSpeedrunnerItem respawnSpeedrunnerItem = new RespawnSpeedrunnerItem();
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this, trackingListener), this);
        getServer().getPluginManager().registerEvents(trackingListener, this);
        getServer().getPluginManager().registerEvents(new PreGameEvents(this), this);
        getServer().getPluginManager().registerEvents(new RespawnItemListener(this), this);
    }

    private void loadCommands() {
        getCommand("config").setExecutor(new Config(this));
    }
}
