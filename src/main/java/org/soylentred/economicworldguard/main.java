package org.soylentred.economicworldguard;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin{
	FileConfiguration config = getConfig();
	private static Economy bank = null;
	public List<String> ignoreRegions;

	public void onEnable(){
		//Get configs
		this.getConfig();
		config.addDefault("chunkBuyPrice", 250);
		config.addDefault("chunkSellPrice", 125);
		config.addDefault("debug", false);
		ignoreRegions = Arrays.asList();
		config.addDefault("ignoreRegions", ignoreRegions);
	    config.options().copyDefaults(true);
	    saveConfig();
	    ignoreRegions = config.getStringList("ignoreRegions");
	    
	    //Load vault
        if (!setupEconomy() ) {
        	Bukkit.getLogger().info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        //Set WorldGuardPlugin
        common.worldGuardInst = getWorldGuard();
        
		//Register Commands
		registerCommands();
	}
	
	public void onDisable(){}
	
	public void onLoad(){
        //Register economicWorldGuard WorldGuard flag
        FlagRegistry registry = getWorldGuard().getFlagRegistry();
        try {
        	// register our flag with the registry
            registry.register(common.EconomicWorldGuard);
        } catch (FlagConflictException e) {
         	Bukkit.getLogger().info("Economic World Guard flag 'ewg' being used by something else. Disabling economicWorldGuard...");
           	getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}
	
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
    
    public void registerCommands(){
		this.getCommand("buychunk").setExecutor(new commandBuyChunk(config.getBoolean("debug"), bank, config.getInt("chunkBuyPrice"), ignoreRegions));
		this.getCommand("ewgadmin").setExecutor(new commandEwgadmin(config.getBoolean("debug"), this));
		this.getCommand("givechunk").setExecutor(new commandGiveChunk(config.getBoolean("debug")));
		this.getCommand("sellchunk").setExecutor(new commandSellChunk(config.getBoolean("debug"), bank, config.getInt("chunkSellPrice")));
		this.getCommand("sharechunk").setExecutor(new commandShareChunk(config.getBoolean("debug")));
		this.getCommand("restrictchunk").setExecutor(new commandRestrictChunk(config.getBoolean("debug")));
    }
}
