package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

public class commandGiveChunk implements CommandExecutor {
	public boolean debug = false;

	public commandGiveChunk(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;

			if (debug) {
				Bukkit.getLogger().info("Command givechunk called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}
			//Make sure the user is sending the chunk to a player.
			if (args.length < 1){
				Bukkit.getLogger().info("Player used the wrong syntax.");
				sender.sendMessage("Wrong syntax used. Please use /givechunk [player]");
				return true;
			}
			if(Bukkit.getPlayer(args[0]) == null){
				Bukkit.getLogger().info("Player tried to give chunk to player that doesn't exist. (" + args[0] + ")");
				sender.sendMessage("Player " + args[0] + " not found.");
				return true;
			}
				
			
			// Get player region and location
			ProtectedRegion myRegion = common.getMyRegion(player);
			int[] chunkLocation = common.getChunkLocation(player.getLocation().getBlockX(), player.getLocation().getBlockZ()); 
			if (myRegion != null){
				//Check if the chunk that is being given away is using the naming convention
				boolean usesDefaultName = common.usesNamingConvention(chunkLocation[0], chunkLocation[1], player.getName(), myRegion);
				String oldRegionName = myRegion.getId();

				//Remove region
				RegionManager regionMan = common.worldGuardInst.getRegionContainer().get(player.getWorld());
				regionMan.removeRegion(myRegion.getId(), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);

				//Recreate the region in the name of the specified player
				//Collect basic info for the region
				int[] newChunkLocation = common.getChunkLocation(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
				String regionName = Bukkit.getPlayer(args[0]).getName() + Integer.toString(newChunkLocation[0]) + Integer.toString(newChunkLocation[1]);
				
				// Create the region
				int[] chunkBounds = common.getChunkBounds(chunkLocation[0], chunkLocation[1]);
				BlockVector loc1 = new BlockVector(chunkBounds[0], 0, chunkBounds[1]);
				BlockVector loc2 = new BlockVector(chunkBounds[2], 256, chunkBounds[3]);
				
				ProtectedRegion newRegion;
				if (usesDefaultName){
					newRegion = new ProtectedCuboidRegion(regionName, loc1, loc2);
				}else{
					//If the old region didn't have a name generated by economicWorldGuard then use that same ID for the new region.
					newRegion = new ProtectedCuboidRegion(oldRegionName, loc1, loc2);
				}
				// Set the owner
				DefaultDomain regionOwner = new DefaultDomain();
				regionOwner.addPlayer(Bukkit.getPlayer(args[0]).getUniqueId());
				newRegion.setOwners(regionOwner);
				newRegion.setMembers(regionOwner);
				newRegion.setFlag(common.EconomicWorldGuard, StateFlag.State.ALLOW);
				// Set the parent chunk as 'economicworldguard'
				try {
					newRegion.setParent(regionMan.getRegion("economicWorldGuard"));
				} catch (CircularInheritanceException e) {
					Bukkit.getLogger().info("Bad world.");
				}
						
				regionMan.addRegion(newRegion);
					
				player.sendMessage("Giving this chunk to " + args[0] + ".");
				if (debug) {
					Bukkit.getLogger().info("Region " + myRegion.getId() + " given to " + Bukkit.getPlayer(args[0]).toString());
				}
				return true;
			}
			
			player.sendMessage("You don't own a chunk here!");
			if (debug) {
				Bukkit.getLogger().info("No region was exchanged by givechunk command.");
			}
			return true;
		}
		return true;
	}
}