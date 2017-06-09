package org.soylentred.economicworldguard;

public class common {
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
}
