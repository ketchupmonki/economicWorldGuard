package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class commandEwgadmin implements CommandExecutor {
	public boolean debug = false;
	private main pluginInstance = null;

	public commandEwgadmin(boolean debug, main pluginInstance) {
		this.debug = debug;
		this.pluginInstance = pluginInstance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;
			if (debug) {
				Bukkit.getLogger().info("Command ewgadmin called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}

			if (args.length < 1){
				return help(player);
			}else{
				switch(args[0].toLowerCase()){
				case "help":
					return help(player);
				case "evict":
					return evict(player);
                case "addignored":
                    return addIgnored(player, args);
                case "removeignored":
                    return removeIgnored(player, args);
                case "listignored":
                	return listIgnored(player);
				default:
					player.sendMessage("Invalid argument '" + args[0].toLowerCase() + "'. Use /ewgadmin help to see valid commands.");
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean evict(Player player){
		// Get regions at the player's location
		RegionContainer container = common.worldGuardInst.getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet regions = query.getApplicableRegions(player.getLocation());
		
		for (ProtectedRegion region : regions) {
			if (region.getFlag(common.EconomicWorldGuard) == StateFlag.State.ALLOW ){
				RegionManager regionMan = container.get(player.getWorld());
				regionMan.removeRegion(region.getId(), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
				player.sendMessage("Region " + region.getId() + " removed.");
				if (debug) {
					Bukkit.getLogger().info("Region " + region.getId() + " removed.");
				}
				return true;
			}
		}
		player.sendMessage("No region to be removed was found.");
		if (debug) {
			Bukkit.getLogger().info("No region was removed by evict command.");
		}
		return true;
	}
	
	public boolean help(Player player){
		player.sendMessage("Valid economicWorldGuard commands:");
		player.sendMessage("\"/ewgadmin help\" - View this help.");
		player.sendMessage("\"/ewgadmin evict\" - Remove any economicWorldGuard chunk currently stood in.");
		player.sendMessage("\"/ewgadmin addIgnored [regionName]\" - Add a region to the ignored regions list. See the 'ignoreRegions' options in the config rundown for more info on this.");
		player.sendMessage("\"/ewgadmin removeIgnored [regionName]\" - Remove a region from the ignored regions list.");
		return true;
	}
	
    public boolean addIgnored(Player player, String[] args){
    	if (args.length < 2){
    		player.sendMessage("Not enough arguments used. Please provide a region name! \"/ewgadmin addIgnored [regionName]\"");
    		return true;
    	}
    	pluginInstance.ignoreRegions = pluginInstance.config.getStringList("ignoreRegions");
    	
    	for (String ignoreRegion : pluginInstance.ignoreRegions){
    		if(ignoreRegion.equals(args[1])){
    			player.sendMessage(args[1] + " is already in the ignoredRegions list!");
    			return true;
    		}
    	}
    	pluginInstance.ignoreRegions.add(args[1]);
        pluginInstance.config.set("ignoreRegions", pluginInstance.ignoreRegions);
        pluginInstance.saveConfig();
        pluginInstance.registerCommands();
        player.sendMessage(args[1] + " successfully added to the ignoredRegions list!");
        return true;
    }
    
    public boolean removeIgnored(Player player, String[] args){
    	if (args.length < 2){
    		player.sendMessage("Not enough arguments used. Please provide a region name! \"/ewgadmin removeIgnored [regionName]\"");
    		return true;
    	}
    	pluginInstance.ignoreRegions = pluginInstance.config.getStringList("ignoreRegions");
    	
    	for (String ignoreRegion : pluginInstance.ignoreRegions){
    		if(ignoreRegion.equals(args[1])){
    			pluginInstance.ignoreRegions.remove(ignoreRegion);
    	        pluginInstance.config.set("ignoreRegions", pluginInstance.ignoreRegions);
    	        pluginInstance.saveConfig();
    	        pluginInstance.registerCommands();
    	        player.sendMessage(args[1] + " successfully removed from the ignoredRegions list!");
    			return true;
    		}
    	}
    	player.sendMessage(args[1] + " couldn't be found in the ignore regions list, so wasn't removed!");
        return true;
    }
    
	private boolean listIgnored(Player player) {
		String ignoredList = "";
		for (String ignoreRegion : pluginInstance.ignoreRegions){
			ignoredList = ignoredList + ignoreRegion + ", ";
		}
		if(ignoredList.length() > 0){
			player.sendMessage("The following regions are currently in the ignoreRegions list:");
			player.sendMessage(ignoredList.substring(0, ignoredList.length() - 2) + ".");
		}else{
			player.sendMessage("The ignoreRegions list is currently empty!");
		}
		return true;
	}
}
