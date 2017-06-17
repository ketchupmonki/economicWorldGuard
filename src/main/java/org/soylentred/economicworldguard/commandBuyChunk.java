package org.soylentred.economicworldguard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.milkbowl.vault.economy.Economy;


public class commandBuyChunk implements CommandExecutor {
	public boolean debug = false;
	private int buyPrice = 250;
	Economy bank;
	List<String> ignoreRegions;

	public commandBuyChunk(boolean debug, Economy bank, int buyPrice, List<String> list) {
		this.debug = debug;
		this.buyPrice = buyPrice;
		this.bank = bank;
		this.ignoreRegions = list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Command can't be used on the terminal.");
		} else {
			Player player = (Player) sender;

			if (debug) {
				Bukkit.getLogger().info("Command BuyChunk called by " + player.getDisplayName() + " at " + player.getLocation() + ".");
			}

			// Get regions at the player's location
			RegionContainer container = common.worldGuardInst.getRegionContainer();
			RegionQuery query = container.createQuery();
			ApplicableRegionSet regions = query.getApplicableRegions(player.getLocation());
			int currentRegions = 0;
			
			//Filter out any regions that should be ignored as set in the plugin's config file.
			for (ProtectedRegion region : regions) {
				boolean filterOut = false;
				for (String ignore : ignoreRegions) {
					if (ignore.equalsIgnoreCase(region.getId())){
						filterOut = true;
					}
				}
				if (!filterOut){
					currentRegions ++;
				}
			}
			
			// Check for existing regions
			if (currentRegions > 0) {
				player.sendMessage("Chunk can not be bought. There is already a region in this location.");
				if (debug) {
					Bukkit.getLogger().info("Region could not be created as there is already a region in this location.");
				}
				return true;
			}

			// Check the player can afford a chunk
			if (bank.getBalance(player) < buyPrice) {
				player.sendMessage("Chunk can not be bought. You need a bank balance of at least " + buyPrice + bank.currencyNamePlural() + "to buy the chunk. You only have " + bank.getBalance(player) + bank.currencyNamePlural() + ".");
				if (debug) {
					Bukkit.getLogger().info("Region could not be created as the player's balance is too low.");
				}
				return true;
			}

			// Buy the chunk
			// Withdraw the money
			bank.withdrawPlayer(player, buyPrice);
			common.createChunk(player);
			player.sendMessage("Chunk purchased for " + buyPrice + bank.currencyNamePlural() + ".");
			if (debug) {
				Bukkit.getLogger().info("User purchased chunk.");
			}
		}
		return true;
	}
}
