package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.milkbowl.vault.economy.Economy;

public class commandSellChunk implements CommandExecutor {
	public boolean debug = false;
	private int sellPrice = 125;
	Economy bank;

	public commandSellChunk(boolean debug, Economy bank, int sellPrice) {
		this.debug = debug;
		this.sellPrice = sellPrice;
		this.bank = bank;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;

			if (debug) {
				Bukkit.getLogger().info("Command SellChunk called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}

			// Get player region
			ProtectedRegion myRegion = common.getMyRegion(player);
			if (myRegion != null){
				//Delete it and refund player
				RegionManager regionMan = common.worldGuardInst.getRegionContainer().get(player.getWorld());
				regionMan.removeRegion(myRegion.getId(), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
				bank.depositPlayer(player, sellPrice);
				player.sendMessage("Region " + myRegion.getId() + " sold for " + sellPrice + bank.currencyNamePlural() + ".");
				if (debug) {
					Bukkit.getLogger().info("Region " + myRegion.getId() + " removed.");
				}
				return true;
			}
			player.sendMessage("You don't own a chunk here!");
			if (debug) {
				Bukkit.getLogger().info("No region was removed by sellchunk command.");
			}
			return true;
		}
		return true;
	}
}