package org.soylentred.economicworldguard;

import org.soylentred.economicworldguard.common;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class commonTest{
	@Test
	public void getChunkLocationShouldReturnCorrectCoords(){
		assertEquals("Test 1.1", 9, common.getChunkLocation(158,60)[0]);
		assertEquals("Test 1.1", 3, common.getChunkLocation(158,60)[1]);
		
		assertEquals("Test 1.2", 11, common.getChunkLocation(184,23)[0]);
		assertEquals("Test 1.2", 1, common.getChunkLocation(184,23)[1]);
		
		assertEquals("Test 1.3", 9, common.getChunkLocation(146,-16)[0]);
		assertEquals("Test 1.3", -2, common.getChunkLocation(146, -16)[1]);
		
		assertEquals("Test 1.4", -1, common.getChunkLocation(-13,-3)[0]);
		assertEquals("Test 1.4", -1, common.getChunkLocation(-13,-3)[1]);
		
		assertEquals("Test 1.5", -1, common.getChunkLocation(-2,5)[0]);
		assertEquals("Test 1.5", 0, common.getChunkLocation(-2,5)[1]);
		
		assertEquals("Test 1.6", 0, common.getChunkLocation(2,3)[0]);
		assertEquals("Test 1.6", 0, common.getChunkLocation(2,3)[1]);
		
		assertEquals("Test 1.7", 0, common.getChunkLocation(1,-5)[0]);
		assertEquals("Test 1.7", -1, common.getChunkLocation(1,-5)[1]);
		
		assertEquals("Test 1.8", 9, common.getChunkLocation(146,3)[0]);
		assertEquals("Test 1.8", 0, common.getChunkLocation(146,3)[1]);
		
		assertEquals("Test 1.9", 9, common.getChunkLocation(155,-5)[0]);
		assertEquals("Test 1.9", -1, common.getChunkLocation(155,-5)[1]);
		
		assertEquals("Test 1.10", 3, common.getChunkLocation(57,-73)[0]);
		assertEquals("Test 1.10", -5, common.getChunkLocation(57,-73)[1]);
	}
	
	@Test
	public void getChunkBoundsShouldReturnCorrectCoords(){
		assertEquals("Test 2.1", 0, common.getChunkBounds(0,0)[0]);
		assertEquals("Test 2.1", 0, common.getChunkBounds(0,0)[1]);
		assertEquals("Test 2.1", 15, common.getChunkBounds(0,0)[2]);
		assertEquals("Test 2.1", 15, common.getChunkBounds(0,0)[3]);
		
		assertEquals("Test 2.2", -1, common.getChunkBounds(-1,0)[0]);
		assertEquals("Test 2.2", 0, common.getChunkBounds(-1,0)[1]);
		assertEquals("Test 2.2", -16, common.getChunkBounds(-1,0)[2]);
		assertEquals("Test 2.2", 15, common.getChunkBounds(-1,0)[3]);
		
		assertEquals("Test 2.3", 0, common.getChunkBounds(0,-1)[0]);
		assertEquals("Test 2.3", -1, common.getChunkBounds(0,-1)[1]);
		assertEquals("Test 2.3", 15, common.getChunkBounds(0,-1)[2]);
		assertEquals("Test 2.3", -16, common.getChunkBounds(0,-1)[3]);
		
		assertEquals("Test 2.4", -1, common.getChunkBounds(-1,-1)[0]);
		assertEquals("Test 2.4", -1, common.getChunkBounds(-1,-1)[1]);
		assertEquals("Test 2.4", -16, common.getChunkBounds(-1,-1)[2]);
		assertEquals("Test 2.4", -16, common.getChunkBounds(-1,-1)[3]);
		
		assertEquals("Test 2.5", 144, common.getChunkBounds(9,7)[0]);
		assertEquals("Test 2.5", 112, common.getChunkBounds(9,7)[1]);
		assertEquals("Test 2.5", 159, common.getChunkBounds(9,7)[2]);
		assertEquals("Test 2.5", 127, common.getChunkBounds(9,7)[3]);
		
		assertEquals("Test 2.6", -113, common.getChunkBounds(-8,8)[0]);
		assertEquals("Test 2.6", 128, common.getChunkBounds(-8,8)[1]);
		assertEquals("Test 2.6", -128, common.getChunkBounds(-8,8)[2]);
		assertEquals("Test 2.6", 143, common.getChunkBounds(-8,8)[3]);
		
		assertEquals("Test 2.7", 112, common.getChunkBounds(7,-9)[0]);
		assertEquals("Test 2.7", -129, common.getChunkBounds(7,-9)[1]);
		assertEquals("Test 2.7", 127, common.getChunkBounds(7,-9)[2]);
		assertEquals("Test 2.7", -144, common.getChunkBounds(7,-9)[3]);
		
		assertEquals("Test 2.8", -161, common.getChunkBounds(-11,-9)[0]);
		assertEquals("Test 2.8", -129, common.getChunkBounds(-11,-9)[1]);
		assertEquals("Test 2.8", -176, common.getChunkBounds(-11,-9)[2]);
		assertEquals("Test 2.8", -144, common.getChunkBounds(-11,-9)[3]);
	}
}
