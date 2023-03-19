package fr.ziberty.manhunt;

import fr.ziberty.manhunt.commands.Config;
import fr.ziberty.manhunt.inventories.ConfigInventory;
import fr.ziberty.manhunt.inventories.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Manhunt extends JavaPlugin {

    private ConfigInventory configInventory;

    private List<Player> speedrunnersList = new ArrayList<>();

    public ConfigInventory getConfigInventory() {
        return configInventory;
    }

    private void setConfigInventory(ConfigInventory configInventory) {
        this.configInventory = configInventory;
    }

    public List<Player> getSpeedrunnersList() {
        return speedrunnersList;
    }

    public boolean isPlayerSpeedrunner(Player player) {
        return this.speedrunnersList.contains(player);
    }

    @Override
    public void onEnable() {
        setupWorld();
        setConfigInventory(new ConfigInventory());
        getCommand("config").setExecutor(new Config(this));
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void setupWorld() {
        Bukkit.getWorld("world").setDifficulty(Difficulty.NORMAL);
        Bukkit.getWorld("world").setTime(0);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamerule doDayLightCycle false");
    }
}
