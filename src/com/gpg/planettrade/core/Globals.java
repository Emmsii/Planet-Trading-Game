package com.gpg.planettrade.core;

import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

import com.gpg.planettrade.core.planet.Planet;

public class Globals {

	public static Random random = new Random();
	
	public static int galaxySize = 3;
	public static int regionSize = 3;
	public static int sectorSize = 5;
	public static int systemFactor = 3;
	
	public static int startingCredits = 50000;
	public static int startingPlanets = 10;
	
	public static int resourceFrequency = 4;
	
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
