package org.soylentred.economicworldguard;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

public class common {
    public static final StateFlag EconomicWorldGuard = new StateFlag("ewg", false);
    public static WorldGuardPlugin worldGuardInst;
    
	public static int[] getChunkLocation(int blockX, int blockZ){
		
		int[] chunkLoc = new int[2];
		
		chunkLoc[0] = (int) (16 * (Math.floor(Math.abs(blockX / 16))) / 16);
		if(blockX < 0){
			chunkLoc[0] = chunkLoc[0] * -1;
			chunkLoc[0] --;
		}
		chunkLoc[1] = (int) (16 * (Math.floor(Math.abs(blockZ / 16))) / 16);
		if(blockZ < 0){
			chunkLoc[1] = chunkLoc[1] * -1;
			chunkLoc[1] --;
		}
		return chunkLoc;
	}
	
	public static int[] getChunkBounds(int chunkX, int chunkZ){
		int[] chunkBounds = new int[4];
		chunkBounds[0] = chunkX * 16;
		chunkBounds[1] = chunkZ * 16;
		chunkBounds[2] = chunkBounds[0] + 15;
		chunkBounds[3] = chunkBounds[1] + 15;
		
		if (chunkX < 0){
			chunkBounds[0] = chunkBounds[0] + 15;
			chunkBounds[2] = chunkBounds[2] - 15;
		}
		if (chunkZ < 0){
			chunkBounds[1] = chunkBounds[1] + 15;
			chunkBounds[3] = chunkBounds[3] - 15;
		}
		return chunkBounds;
	}
	
	/**
	 * @return Returns first chunk in the player's position that the player owns. Returns null if they don't own a region on that location.
	 */
	public static ProtectedRegion getMyRegion(Player player){
		RegionContainer container = worldGuardInst.getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet regions = query.getApplicableRegions(player.getLocation());

		for (ProtectedRegion region : regions) {
			if (region.getFlag(common.EconomicWorldGuard) == StateFlag.State.ALLOW ){
				if (region.getOwners().contains(player.getUniqueId())) {
					return region;
				}
			}
		}
		return null;
	}
	
	/**
	 * @return Returns true if the name of the current region is using the naming standard for economicWorldGuard 0.1
	 */
	public static boolean usesNamingConvention(int chunkX, int chunkZ, String name, ProtectedRegion region){
		if (region.getId().equalsIgnoreCase(name + Integer.toString(chunkX) + Integer.toString(chunkZ))){
			return true;
		}
		return false;
	}
	
	/**
	 * @param Create region at location of player
	 */
	public static boolean createChunk(Player player){
		int[] chunkLocation = common.getChunkLocation(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
		String regionName = player.getName() + Integer.toString(chunkLocation[0]) + Integer.toString(chunkLocation[1]);
		return createChunk(player, regionName);
	}
	
	/**
	 * @param Create region at location of player with specific name
	 */
	public static boolean createChunk(Player player, String name){
		return createChunk(player, name, player.getUniqueId());
	}
	
	public static boolean createChunk(Player player, String name, UUID owner){
		RegionContainer container = common.worldGuardInst.getRegionContainer();
		
		//Collect basic info for the region
		int[] chunkLocation = common.getChunkLocation(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
		int[] chunkBounds = common.getChunkBounds(chunkLocation[0], chunkLocation[1]);
		
		String regionName = name;

		// Create the region
		BlockVector loc1 = new BlockVector(chunkBounds[0], 0, chunkBounds[1]);
		BlockVector loc2 = new BlockVector(chunkBounds[2], 256, chunkBounds[3]);
		ProtectedRegion newRegion = new ProtectedCuboidRegion(regionName, loc1, loc2);

		// Set the owner
		DefaultDomain regionOwner = new DefaultDomain();
		regionOwner.addPlayer(owner);
		newRegion.setOwners(regionOwner);
		newRegion.setMembers(regionOwner);
		newRegion.setFlag(common.EconomicWorldGuard, StateFlag.State.ALLOW);
		// Start manager
		RegionManager regionsManage = container.get(player.getWorld());

		// Set the parent chunk as 'economicworldguard'
		try {
			newRegion.setParent(regionsManage.getRegion("economicWorldGuard"));
		} catch (CircularInheritanceException e) {
			Bukkit.getLogger().info("Bad world.");
		}

		regionsManage.addRegion(newRegion);
		
		return true;
	}
}
