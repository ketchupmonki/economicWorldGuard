package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class commandEwgadmin implements CommandExecutor {
	public boolean debug = false;
	public WorldGuardPlugin worldGuard;

	public commandEwgadmin(boolean debug, WorldGuardPlugin worldGuard) {
		this.debug = debug;
		this.worldGuard = worldGuard;
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
		RegionContainer container = worldGuard.getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet regions = query.getApplicableRegions(player.getLocation());

		int chunkX = (int) (16 * (Math.floor(Math.abs(player.getLocation().getBlockX() / 16))) / 16);
		if(player.getLocation().getBlockX() < 0){
			chunkX = chunkX * -1;
		}
		int chunkZ = (int) (16 * (Math.floor(Math.abs(player.getLocation().getBlockZ() / 16))) / 16);
		if(player.getLocation().getBlockZ() < 0){
			chunkZ = chunkZ * -1;
		}

		for (ProtectedRegion region : regions) {
			if (region.getId().endsWith(Integer.toString(chunkX) + Integer.toString(chunkZ))) {
				// If the region name ends with our chunkX and chunkY then
				// assume that it was generated by economicWorldGuard
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
		player.sendMessage("\"/ecgadmin help\" - View this help.");
		player.sendMessage("\"/ecgadmin evict\" - Remove any economicWorldGuard chunk currently stood in.");
		return true;
	}
}