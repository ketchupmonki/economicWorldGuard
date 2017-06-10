package org.soylentred.economicworldguard;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

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
	
	public static int[] getChunkBounds(int chunkX, int chunkY){
		int[] chunkBounds = new int[4];
		chunkBounds[0] = chunkX * 16;
		chunkBounds[1] = chunkY * 16;
		chunkBounds[2] = chunkBounds[0] + 15;
		chunkBounds[3] = chunkBounds[1] + 15;
		
		if (chunkX < 0){
			chunkBounds[0] = chunkBounds[0] + 15;
			chunkBounds[2] = chunkBounds[2] - 15;
		}
		if (chunkY < 0){
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
	public static boolean usesNamingConvention(int chunkX, int chunkY, String name, ProtectedRegion region){
		if (region.getId().equalsIgnoreCase(name + Integer.toString(chunkX) + Integer.toString(chunkY))){
			return true;
		}
		return false;
	}
}
