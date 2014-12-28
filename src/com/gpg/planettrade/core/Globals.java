package com.gpg.planettrade.core;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.gpg.planettrade.core.planet.Planet;

public class Globals {

	public static Random random = new Random();
	
	/*
	 * Server Locals
	 */
	
	public static List<String> currentTrades;
	
	public static int galaxySize;
	public static int regionSize;
	public static int sectorSize;
	public static int systemFactor;
	
	public static long startingCredits;
	public static int startingPlanets;
	
	public static int resourceMultiplier;
	
	public static void init(Properties prop){
		currentTrades = new ArrayList<String>();
		
		galaxySize = Integer.parseInt(prop.getProperty("galaxy_size"));
		regionSize = Integer.parseInt(prop.getProperty("region_size"));
		sectorSize = Integer.parseInt(prop.getProperty("sector_size"));
		systemFactor = Integer.parseInt(prop.getProperty("system_factor"));
		
		startingCredits = Integer.parseInt(prop.getProperty("starting_credits"));
		startingPlanets = Integer.parseInt(prop.getProperty("starting_planets"));
		
		resourceMultiplier = Integer.parseInt(prop.getProperty("resource_multiplier"));
	}
	
	/*
	 * Client Locals
	 */
	
	public static String username;
	public static long storedCredits;
	
	public static List<Planet> ownedPlanets;
	public static Planet currentPlanet;
	
	/*
	 * Stats
	 */
	
	public static int totalTrades;
	public static int totalEndedTrades;
	
	public static String toCredits(long value){
		return "c" + formatInt(value);
	}
	
	public static int getWorth(){
		if(ownedPlanets == null || ownedPlanets.isEmpty()) return 0;
		int worth = 0;
		for(Planet p : ownedPlanets) worth += p.worth;
		return worth;
	}
	
	//TODO: Replace all NumberFormat bits with this method.
	public static String formatInt(long value){
		return NumberFormat.getIntegerInstance().format(value);
	}
}
