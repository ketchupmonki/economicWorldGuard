package org.soylentred.economicworldguard;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin{
	FileConfiguration config = getConfig();
	private static Economy bank = null;

	public void onEnable(){
		//Get configs
		this.getConfig();
		config.addDefault("chunkBuyPrice", 250);
		config.addDefault("chunkSellPrice", 125);
		config.addDefault("debug", false);
		List<String> ignoreRegions = Arrays.asList();
		config.addDefault("ignoreRegions", ignoreRegions);
	    config.options().copyDefaults(true);
	    saveConfig();
	    
	    //Load vault
        if (!setupEconomy() ) {
        	Bukkit.getLogger().info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

		//Register Commands
		this.getCommand("buychunk").setExecutor(new commandBuyChunk(config.getBoolean("debug"), bank, config.getInt("chunkBuyPrice"), getWorldGuard(), config.getList("ignoreRegions")));
		this.getCommand("evictchunk").setExecutor(new commandEvictChunk(config.getBoolean("debug"), getWorldGuard()));
		this.getCommand("givechunk").setExecutor(new commandGiveChunk(config.getBoolean("debug"), getWorldGuard()));
		this.getCommand("sellchunk").setExecutor(new commandSellChunk(config.getBoolean("debug"), bank, config.getInt("chunkSellPrice"), getWorldGuard()));
		this.getCommand("sharechunk").setExecutor(new commandShareChunk(config.getBoolean("debug"), getWorldGuard()));
		this.getCommand("restrictchunk").setExecutor(new commandRestrictChunk(config.getBoolean("debug"), getWorldGuard()));
	}
	
	public void onDisable(){}
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
        	Bukkit.getLogger().info(String.format("[%s] - Disabled due to no WorldGuard dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return null;
	    }

	    return (WorldGuardPlugin) plugin;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        bank = rsp.getProvider();
        return bank != null;
    }
}
