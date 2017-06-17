package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class commandRenameChunk implements CommandExecutor {
	public boolean debug = false;

	public commandRenameChunk(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;

			if (debug) {
				Bukkit.getLogger().info("Command renamechunk called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}
			
			RegionManager regionMan = common.worldGuardInst.getRegionContainer().get(player.getWorld());
			
			if(regionMan.getRegion(args[0]) == null){
				// Get player region and location
				ProtectedRegion myRegion = common.getMyRegion(player);
				if (myRegion != null){
					//Remove region
					regionMan = common.worldGuardInst.getRegionContainer().get(player.getWorld());
					regionMan.removeRegion(myRegion.getId(), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
					
					//Create region with new name
					common.createChunk(player, args[0]);
					
					player.sendMessage("Chunk renamed to " + args[0] + ".");
					if (debug) {
						Bukkit.getLogger().info("Region " + myRegion.getId() + " renamed to " + args[0]);
					}
					return true;
				}
				
				player.sendMessage("You don't own a chunk here!");
				if (debug) {
					Bukkit.getLogger().info("No region was renamed.");
				}
			}else{
				sender.sendMessage("A region by the name '" + args[0] + "' already exists. Please choose a unique name!");
			} 
			return true;
		}
		return true;
	}
}