package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.domains.DefaultDomain;

public class commandShareChunk implements CommandExecutor {
	public boolean debug = false;

	public commandShareChunk(boolean debug) {
		this.debug = debug;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;

			if (debug) {
				Bukkit.getLogger().info("Command sharechunk called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}
			
			//Make sure the user is sharing the chunk to a player.
			if (args.length < 1){
				Bukkit.getLogger().info("Player used the wrong syntax.");
				sender.sendMessage("Wrong syntax used. Please use /sharechunk [player]");
				return true;
			}
			if(Bukkit.getPlayer(args[0]) == null){
				Bukkit.getLogger().info("Player tried to share chunk whith a player that doesn't exist. (" + args[0] + ")");
				sender.sendMessage("Player " + args[0] + " not found.");
				return true;
			}
				
			// Get player region
			ProtectedRegion myRegion = common.getMyRegion(player);
			if (myRegion != null){
				//Get current member list
				RegionManager regionMan = common.worldGuardInst.getRegionContainer().get(player.getWorld());
				DefaultDomain currentMembers = regionMan.getRegion(myRegion.getId()).getMembers();
				
				//Add member
				currentMembers.addPlayer(Bukkit.getPlayer(args[0]).getUniqueId());
				regionMan.getRegion(myRegion.getId()).setMembers(currentMembers);
				
				player.sendMessage("Sharing chunk with " + args[0] + ".");
				Bukkit.getPlayer(args[0]).sendMessage(player.getName() + " has shared a chunk with you.");
				if (debug) {
					Bukkit.getLogger().info("Region " + myRegion.getId() + " shared with " + Bukkit.getPlayer(args[0]).toString());
				}
				return true;
			}
			player.sendMessage("You don't own a chunk here!");
			if (debug) {
				Bukkit.getLogger().info("No region was shared by sharechunk command.");
			}
			return true;
		}
		return true;
	}
}
