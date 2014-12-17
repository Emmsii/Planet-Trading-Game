package com.gpg.planettrade.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Player;
import com.gpg.planettrade.core.TradeOffer;
import com.gpg.planettrade.core.planet.Planet;
import com.gpg.planettrade.core.planet.factory.Factory;
import com.gpg.planettrade.core.planet.resource.Resource;

public class FileHandler {

	private static int regions;
	private static int sectors;
	private static int systemsCreated;
	private static int totalTiles;
	private static int planetsCreated;
	
	private static int terran;
	private static int water;
	private static int lava;
	private static int desert;
	private static int gas;
	
	private static int foldersCreated;
	private static double size = 0;
	
	public static void createFolderStructure(){
		double start = System.currentTimeMillis();
		
		File file = new File("players/");
		if(!file.exists()){
			Log.info("Players folder doesn't exist, creating now.");
			file.mkdirs();
		}
		
		file = new File("marketplace/");
		if(!file.exists()){
			Log.info("Marketplace folder doesn't exist, creating now.");
			file.mkdirs();
		}
		
		file = new File("data/");
		if(!file.exists()){
			Log.info("Data folder doesn't exist, creating now.");
			file.mkdirs();
		}else return;	
		
		for(int y = 0; y < Globals.galaxySize; y++){
			for(int x = 0; x < Globals.galaxySize; x++){
				createRegion(x, y);
			}
		}
		
		Log.info("-------------------------------------------------------------------------------------------------------");
		Log.info("Created " + foldersCreated + " folders in " + ((System.currentTimeMillis() - start) / 1000) + " seconds. (" + String.format("%.2f", size) + "mb)");
		Log.info("Regions: " + regions + " | Sectors: " + sectors + " | Systems: " + systemsCreated + " | Planets: " + planetsCreated + " | Chance for system: " + Globals.systemFactor + " out of 30.");
		Log.info("Planet Types - Terran: " + terran + " (" + calcPer(terran) + "%)" +
				            " | Water: " + water + " ("+ calcPer(water) + "%)" +
				             " | Lava: " + lava + " (" + calcPer(lava) + "%)" +
				             " | Desert: " + desert + " (" + calcPer(desert) + "%)" +
				             " | Gas: " + gas + " (" + calcPer(gas) + "%)");
		Log.info("Total Tiles: " + totalTiles);
	}
	
	private static float calcPer(int a){
		return (float) ((a * 100) / planetsCreated);
	}
	
	private static void createRegion(int x, int y){
		String rx = Integer.toString(x);
		String ry = Integer.toString(y);
		File file = new File("data/", "r" + rx + "_" + ry);
		if(!file.exists()){
			file.mkdirs();
			foldersCreated++;
			Log.info("Creating region " + rx + "_" + ry + " [" + foldersCreated + " folders created. " + systemsCreated + " systems created with " + planetsCreated + " planets created] Total size: " + String.format("%.2f", size) + "mb");
		}
		
		for(int sy = 0; sy < Globals.regionSize; sy++){
			for(int sx = 0; sx < Globals.regionSize; sx++){
				createSector("data/r" + rx + "_" + ry + "/", sx, sy, x, y);
			}
		}
		regions++;
	}
	
	private static void createSector(String location, int x, int y, int regionX, int regionY){
		String sx = Integer.toString(x);
		String sy = Integer.toString(y);
		File file = new File(location, "s" + sx + "_" + sy);
		if(!file.exists()){
			file.mkdirs();
			foldersCreated++;
		}
		
		for(int ssY = 0; ssY < Globals.sectorSize; ssY++){
			for(int ssX = 0; ssX < Globals.sectorSize; ssX++){
				totalTiles++;
				if(Globals.random.nextInt(30) <= Globals.systemFactor){
					createSystem(location + "s" + sx + "_" + sy + "/", ssX, ssY, x, y, regionX, regionY);
				}
			}
		}
		sectors++;
	}
	
	private static void createSystem(String location, int x, int y, int sectorX, int sectorY, int regionX, int regionY){
		String sx = Integer.toString(x);
		String sy = Integer.toString(y);
		File file = new File(location, "ss" + sx + "_" + sy);
		if(!file.exists()){
			file.mkdirs();
			foldersCreated++;
		}
		
		int r = Globals.random.nextInt(7) + 1;
		for(int i = 0; i < r; i++){
			createPlanet(location + "/ss" + sx + "_" + sy + "/p" + i + ".dat", i, "r0" + regionX + "0" + regionY + "_s0" + sectorX + "0" + sectorY + "_s0" + sx + "0" + sy + "_p" + i);
		}
		
		systemsCreated++;
	}
	
	/*
	 * Planet Methods
	 */
	
	private static void createPlanet(String location, int id, String subname){
		File file = new File(location);
		ObjectOutputStream oos = null;
		
		Planet planet = new Planet();
		planet.init(id, -1, getRandomPlanetName(), subname + "-" + Math.abs(subname.hashCode()), Globals.random.nextInt(4) + 1, file.getPath());
		
		if(planet.type == 0) terran++;
		else if(planet.type == 1) water++;
		else if(planet.type == 2) lava++;
		else if(planet.type == 3) desert++;
		else if(planet.type == 4) gas++;
		
		try{
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(planet);
			addFileSize(file);
			planetsCreated++;
		}catch(IOException e){
			Log.warn("Could not create planet file.");
			e.printStackTrace();
		}finally{
			try {
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Planet loadPlanet(String location){
		Planet planet = null;
		File file = new File(location);
		if(!file.exists()){
			Log.warn("Could not find planet file at " + location);
			return null;
		}
		
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(file));
			planet = (Planet) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return planet;
	}
	
	public static void savePlanet(Planet p){
		File file = new File(p.filePath);
		if(!file.exists()){
			Log.warn("Cannot save planet file, file doesn't exist.");
			//TODO: Propably shouldn't return, just make the damn file.
			return;
		}
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(p);
			oos.flush();
			oos.close();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Player Methods
	 */
	
	public static boolean savePlayer(Player p){
		File file = new File("players/" + p.name.toLowerCase() + ".dat");
		
		try {
			FileOutputStream out = new FileOutputStream(file);
			if(!file.exists()) file.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(p);
			oos.flush();
			oos.close();
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Player loadPlayer(String name){
		Player result = null;
		File file = new File("players/" + name.toLowerCase() + ".dat");
		
		try {
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(in);
			result = (Player) ois.readObject();
			ois.close();
			in.close();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		return result;
	}
	
	/*
	 * Market Methods
	 */
		
	public static boolean saveTradeOffer(TradeOffer trade){
		File file = new File("marketplace/" + trade.hashCode() + ".dat");		
		try {
			FileOutputStream out = new FileOutputStream(file);
			if(!file.exists()) file.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(trade);
			oos.flush();
			oos.close();
			out.flush();
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static TradeOffer loadOffer(){
		return null;
	}
	
	/*
	 * Resources Methods
	 */
	
	public static Factory[] loadFactories(){
		Factory[] result = null;
		File file = new File("res/factories");
		if(!file.exists()){
			Log.error("Cannot find factory folder at " + file.getPath());
			System.exit(1);
		}
		
		File[] filesFound = file.listFiles();
		result = new Factory[filesFound.length];
		
		for(int i = 0; i < filesFound.length; i++){
			File f = filesFound[i];
			List<String> lines = new ArrayList<String>();
			String line = null;
			
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				while((line = reader.readLine()) != null) lines.add(line);
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Factory factory = new Factory();
			for(String s : lines){
				if(s.startsWith("name")) factory.name = s.substring(5, s.length());
				if(s.startsWith("description")) factory.description = s.substring(12, s.length());
				if(s.startsWith("type")) factory.type = s.substring(5, s.length());
				if(s.startsWith("price")) factory.price = Integer.parseInt(s.substring(6, s.length()));
				if(s.startsWith("input")) factory.input = s.substring(6, s.length());
				if(s.startsWith("ups")) factory.ups = Integer.parseInt(s.substring(4, s.length()));
			}
			result[i] = factory;
		}
		Log.info("Loaded " + result.length + " factories.");
		return result;
	}
	
	public static Resource[] loadResources(){
		Resource[] result = null;

		File file = new File("res/resources");
		
		if(!file.exists()){
			Log.error("Cannot find resources folder at " + file.getPath());
			System.exit(1);
		}
		
		File[] filesFound = file.listFiles();
		result = new Resource[filesFound.length];
		
		for(int i = 0; i < filesFound.length; i++){
			File f = filesFound[i];
			List<String> lines = new ArrayList<String>();
			String line = null;
			
			try {
				BufferedReader reader  = new BufferedReader(new FileReader(f));
				while((line = reader.readLine()) != null) lines.add(line);
				reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Resource res = new Resource();
			
			for(String s : lines){
				if(s.startsWith("name")) res.name = s.substring(5, s.length());
				if(s.startsWith("description")) res.description = s.substring(12, s.length());
				if(s.startsWith("type")) res.type = s.substring(5, s.length());
				if(s.startsWith("value")) res.value = Integer.parseInt(s.substring(6, s.length()));
				if(s.startsWith("spawn_chance")) res.spawnChance = Integer.parseInt(s.substring(13, s.length()));
				
				//Gets list of planet type resource can spawn on.
				if(s.startsWith("spawn_on")){
					String[] spawnOnString = s.substring(9, s.length()).split(",");
					int[] spawnOnInt = new int[spawnOnString.length];
					for(int j = 0; j < spawnOnString.length; j++) spawnOnInt[j] = Integer.parseInt(spawnOnString[j]);
					res.spawnOn = spawnOnInt;
				}
			}
			result[i] = res;
		}
		
		Log.info("Loaded " + result.length + " resources.");
		return result;
	}
	
	/*
	 * Util Methods
	 */
	
	/*
	 * Returns list of planets owned by player. 
	 * Loads planets from data folder based of list of file paths stored in player.
	 */
	public static List<Planet> getOwnedPlanets(String name){
		Player player = loadPlayer(name);
		List<Planet> result = new ArrayList<Planet>();
		
		for(String location : player.ownedPlanets) result.add(loadPlanet(location));
				
		return result;
	}
	
	public static String findRandomPlanet(boolean anyPlanet){
		Planet planet = null;
		
		File randomSectorFolder = null;
		File randomStarSystemFolder = null;
		File randomPlanetFile = null;
		
		while(true){
			while(true){
				while(true){
					String randomSector = "data/r" + Globals.random.nextInt(Globals.galaxySize) + "_" + Globals.random.nextInt(Globals.galaxySize) + 
											  "/s" + Globals.random.nextInt(Globals.sectorSize) + "_" + Globals.random.nextInt(Globals.sectorSize) + "/";
					
					randomSectorFolder = new File(randomSector);
					if(randomSectorFolder.exists()) break;
				}
				
				File[] starSystemFolders = randomSectorFolder.listFiles();
				if(starSystemFolders.length == 0) continue;
				randomStarSystemFolder = starSystemFolders[Globals.random.nextInt(starSystemFolders.length)];
				if(randomStarSystemFolder.exists()) break;
			}
			
			File[] planetFileList = randomStarSystemFolder.listFiles();
			randomPlanetFile = planetFileList[Globals.random.nextInt(planetFileList.length)];
			planet = loadPlanet(randomPlanetFile.getPath());		
			
			if(anyPlanet) break;
			if(planet.ownerId == -1) break;
		}
		return planet.filePath;
	}
	
	private static String getRandomPlanetName(){
		File file = new File("res/planet_names.txt");
		if(!file.exists()) return "null";
		
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) lines.add(line);
			reader.close();
		} catch (FileNotFoundException e) {
			Log.warn("COuld not pick random planet name.");
			e.printStackTrace();
			return "null";
		} catch (IOException e) {
			Log.warn("COuld not pick random planet name.");
			e.printStackTrace();
			return "null";
		}
		
		String name = lines.get(Globals.random.nextInt(lines.size()));
		return name;
	}
	
	public static int getPlayerId(){
		File file = new File("players/");
		if(!file.exists()) return -1;
		return file.listFiles().length;
	}
	
	private static void addFileSize(File file){
		if(file.exists()){
			double bytes = (file.length());
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			size += megabytes;
		}
	}
	
}
