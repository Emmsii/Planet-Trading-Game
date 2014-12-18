package com.gpg.planettrade.core;

import java.text.NumberFormat;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.gpg.planettrade.core.planet.Planet;

public class Globals {

	public static Random random = new Random();
	
	public static int galaxySize;
	public static int regionSize;
	public static int sectorSize;
	public static int systemFactor;
	
	public static int startingCredits;
	public static int startingPlanets;
	
	public static int resourceMultiplier;
	
	public static void init(Properties prop){
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
	public static int storedCredits;
	
	public static List<Planet> ownedPlanets;
	public static Planet currentPlanet;
	
	public static String toCredits(int value){
		return "c" + formatInt(value);
	}
	
	public static int getWorth(){
		int worth = 0;
		for(Planet p : ownedPlanets) worth += p.worth;
		return worth;
	}
	
	//TODO: Replace all NumberFormat bits with this method.
	public static String formatInt(int value){
		return NumberFormat.getIntegerInstance().format(value);
	}
}
