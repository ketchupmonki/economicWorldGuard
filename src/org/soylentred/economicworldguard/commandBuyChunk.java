package org.soylentred.economicworldguard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

import net.milkbowl.vault.economy.Economy;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;

public class commandBuyChunk implements CommandExecutor {
	public boolean debug = false;
	public WorldGuardPlugin worldGuard;
	private int buyPrice = 250;
	Economy bank;

	public commandBuyChunk(boolean debug, Economy bank, int buyPrice, WorldGuardPlugin worldGuard) {
		this.debug = debug;
		this.worldGuard = worldGuard;
		this.buyPrice = buyPrice;
		this.bank = bank;
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
			RegionContainer container = worldGuard.getRegionContainer();
			RegionQuery query = container.createQuery();
			ApplicableRegionSet regions = query.getApplicableRegions(player.getLocation());

			// Check for existing regions
			if (regions.size() > 0) {
				player.sendMessage("Chunk can not be bought. There is already a region in this location.");
				if (debug) {
					Bukkit.getLogger()
							.info("Region could not be created as there is already a region in this location.");
				}
				return false;
			}

			// Check the player can afford a chunk
			if (bank.getBalance(player.getName()) < buyPrice) {
				player.sendMessage("Chunk can not be bought. You need a bank balance of at least " + buyPrice + " " + bank.currencyNamePlural() + "to buy the chunk. You only have " + bank.getBalance(player.getName()) + " " + bank.currencyNamePlural() + ".");
				if (debug) {
					Bukkit.getLogger().info("Region could not be created as the player's balance is too low.");
				}
				return false;
			}

			// Buy the chunk
			// Withdraw the money
			bank.withdrawPlayer(player.getName(), buyPrice);

			//Collect basic info for the region
			int chunkX = (int) (16 * (Math.floor(Math.abs(player.getLocation().getBlockX() / 16))));
			int chunkZ = (int) (16 * (Math.floor(Math.abs(player.getLocation().getBlockZ() / 16))));

			String regionName = player.getName() + (chunkX / 16) + (chunkZ / 16);

			// Create the region
			BlockVector loc1 = new BlockVector(chunkX, 0, chunkZ);
			BlockVector loc2 = new BlockVector(chunkX + 15, 256, chunkZ + 15);
			ProtectedRegion newRegion = new ProtectedCuboidRegion(regionName, loc1, loc2);

			// Set the owner
			DefaultDomain regionOwner = new DefaultDomain();
			regionOwner.addPlayer(player.getName());
			newRegion.setOwners(regionOwner);
			newRegion.setMembers(regionOwner);

			// Start manager
			RegionManager regionsManage = container.get(player.getWorld());

			// Set the parent chunk as 'economicworldguard'
			try {
				newRegion.setParent(regionsManage.getRegion("economicWorldGuard"));
			} catch (CircularInheritanceException e) {
				Bukkit.getLogger().info("Bad world.");
			}

			regionsManage.addRegion(newRegion);
			player.sendMessage("Chunk purchased.");
			if (debug) {
				Bukkit.getLogger().info("User purchased chunk.");
			}
		}
		return false;
	}
}
