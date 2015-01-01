package com.gpg.planettrade.server;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.PlanetOffer;
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
	
	private static int tilesInSector;
	private static int tilesInRegion;
	private static int tilesInGalaxy;
	private static int centerX;
	private static int centerY;
	
	private static int foldersCreated;
	private static double size = 0;
	
	private static int[][] imageId;
	
	private static final String DATA_FOLDER = "data/";
	
	public static void createFolderStructure(){
		double start = System.currentTimeMillis();
		
		File file = new File(DATA_FOLDER + "players/");
		if(!file.exists()){
			Log.info("Players folder doesn't exist, creating now.");
			file.mkdirs();
		}
		
		file = new File(DATA_FOLDER + "marketplace/");
		if(!file.exists()){
			Log.info("Marketplace folder doesn't exist, creating now.");
			file.mkdirs();
		}
		
		file = new File(DATA_FOLDER + "logs/marketplace/");
		if(!file.exists()){
			Log.info("Logs folder doesn't exist, creating now.");
			file.mkdirs();
		}
		
		file = new File(DATA_FOLDER + "galaxy/");
		if(!file.exists()){
			Log.info("Data folder doesn't exist, creating now.");
			file.mkdirs();
		}else return;	
				
		tilesInSector = Globals.sectorSize;
		tilesInRegion = (Globals.regionSize * tilesInSector);
		tilesInGalaxy = (Globals.galaxySize * tilesInRegion);
		
		imageId = new int[tilesInGalaxy][tilesInGalaxy];
		
		for(int i = 0; i < tilesInGalaxy; i++) for(int j = 0; j < tilesInGalaxy; j++) imageId[i][j] = -1;
		
		centerX = tilesInGalaxy / 2;
		centerY = tilesInGalaxy / 2;
		
		for(int y = 0; y < Globals.galaxySize; y++){
			for(int x = 0; x < Globals.galaxySize; x++){
				createRegion(x, y);
			}
		}
				
		saveImage(imageId, "map");

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
		
	private static void createRegion(int x, int y){
		String rx = Integer.toString(x);
		String ry = Integer.toString(y);
		File file = new File(DATA_FOLDER + "galaxy/", "r" + rx + "_" + ry);
		if(!file.exists()){
			file.mkdirs();
			foldersCreated++;
		}
		
		for(int sy = 0; sy < Globals.regionSize; sy++){
			for(int sx = 0; sx < Globals.regionSize; sx++){
				createSector(DATA_FOLDER + "galaxy/r" + rx + "_" + ry + "/", sx, sy, x, y);
			}
		}
		
		Log.info("Created region " + rx + "_" + ry + " [" + foldersCreated + " folders created. " + systemsCreated + " systems created with " + planetsCreated + " planets created] Total size: " + String.format("%.2f", size) + "mb");
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
		
		int xp = 0;
		int yp = 0;
		
		for(int ssY = 0; ssY < Globals.sectorSize; ssY++){
			for(int ssX = 0; ssX < Globals.sectorSize; ssX++){
				imageId[(tilesInRegion * regionX) + (tilesInSector * x) + ssX][(tilesInRegion * regionY) + (tilesInSector * y) + ssY] = 0;
				totalTiles++;
				xp = (tilesInRegion * regionX) + (tilesInSector * x) + ssX;
				yp = (tilesInRegion * regionY) + (tilesInSector * y) + ssY;
				
				int distance = getDistance(xp, yp, centerX, centerY);			
				
				if((int) (tilesInGalaxy * 0.5) > distance){
					if(Globals.random.nextInt((tilesInGalaxy / 2) - distance) - Globals.random.nextInt(Globals.systemSpacing) + Globals.random.nextInt(5) >= Globals.random.nextInt(Globals.systemSpacing * 3)){
						createSystem(location + "s" + sx + "_" + sy + "/", ssX, ssY, x, y, regionX, regionY);
						imageId[(tilesInRegion * regionX) + (tilesInSector * x) + ssX][(tilesInRegion * regionY) + (tilesInSector * y) + ssY] = 1;
					}
				}
			}
		}
		
		imageId[tilesInSector * x + (regionX * tilesInRegion)][tilesInSector * y + (regionY * tilesInRegion)] = -3;
		imageId[regionX * tilesInRegion][regionY * tilesInRegion] = -2;
		
		saveImage(imageId, "map_" + sectors + ".png");
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
			createPlanet(location + "ss" + sx + "_" + sy + "/p" + i + ".dat", i, "r0" + regionX + "0" + regionY + "_s0" + sectorX + "0" + sectorY + "_s0" + sx + "0" + sy + "_p" + i);
		}
		
		systemsCreated++;
	}
	
	/*
	 * Planet Methods
	 */
	
	private static void createPlanet(String location, int id, String subname){
		File file = new File(location);
		ObjectOutputStream oos = null;

		String[] splitLocation = location.split("/");
		String regionString = "";
		String sectorString = "";
		String systemString = "";
		
		regionString = splitLocation[2].replaceAll("[a-z]", "");
		sectorString = splitLocation[3].replaceAll("[a-z]", "");
		systemString = splitLocation[4].replaceAll("[a-z]", "");
		
		int regionX = Integer.parseInt(regionString.split("_")[0]); //Position of region in galaxy.
		int regionY = Integer.parseInt(regionString.split("_")[1]);
		int sectorX = Integer.parseInt(sectorString.split("_")[0]); //Position of sector in region.
		int sectorY = Integer.parseInt(sectorString.split("_")[1]);
		int systemX = Integer.parseInt(systemString.split("_")[0]); //Position of system in sector.
		int systemY = Integer.parseInt(systemString.split("_")[1]);

		int x = (tilesInRegion * regionX) + (tilesInSector * sectorX) + systemX; 
		int y = (tilesInRegion * regionY) + (tilesInSector * sectorY) + systemY;

		Planet planet = new Planet();
		planet.init(id, x, y, -1, getRandomPlanetName(), subname + "-" + Math.abs(subname.hashCode()), Globals.random.nextInt(4) + 1, file.getPath());
		
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
		File file = new File(DATA_FOLDER + "players/" + p.name.toLowerCase() + ".dat");
		
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
		File file = new File(DATA_FOLDER + "players/" + name.toLowerCase() + ".dat");
		
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

	public static void moveTradeOffer(String name){
		Path source = Paths.get(name);
		Path target = Paths.get(DATA_FOLDER + "/logs/marketplace/" + source.getFileName());
		
		try {
			Files.move(source, target, ATOMIC_MOVE);
		} catch (IOException e) {
			Log.warn("Could not move file [" + name + "] logs folder.");
			e.printStackTrace();
		}
	}
	
	public static boolean saveTradeOffer(TradeOffer trade, boolean override){
		int id = getStat("trades");
		trade.id = id;
		String idName = Integer.toString(id);
		if(id < 10) idName = "0" + idName;
		String name = idName + "_type_here";
		if(trade instanceof GoodsOffer) name = name + ".gt";
		else if(trade instanceof PlanetOffer) name = name + ".pt";
		File file = new File(DATA_FOLDER + "marketplace/" + name);		
		try {
			FileOutputStream out = new FileOutputStream(file);
			//TODO: THIS ISN'T OVERRIDING THE OLD FILE, IT MAKES A NEW ONE CAUSE THE HASHCODE IS DIFFERENT.
			if(override) if(!file.exists()) file.createNewFile();
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
	
	public static String[] loadTradeOfferFiles(){
		File file = new File(DATA_FOLDER + "/marketplace/");
		if(!file.exists()){
			Log.warn("Cannot find marketplace folder.");
			return null;
		}
		
		File[] filesFound = file.listFiles();
		String[] result = new String[filesFound.length];
		for(int i = 0; i < filesFound.length; i++){ 
			if(filesFound[i].isFile())result[i] = filesFound[i].getPath();
		}
		return result;
	}
	
	public static int countTradeOffers(){
		int result = 0;
		File file = new File(DATA_FOLDER + "/marketplace/");
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++) if(files[i].isFile()) result++;
		return result;
	}
	
	public static List<TradeOffer> loadTradeOffers(int page){
		int maxOffersPerPage = 10;
		List<TradeOffer> result = new ArrayList<TradeOffer>();
		File file = new File(DATA_FOLDER + "/marketplace/");
		if(!file.exists()){
			Log.warn("Couldn't find marketplace folder.");
			return null;
		}
		
		File[] filesFound = file.listFiles();
		
		//If length == 0, no trade offers exist.
		if(filesFound.length == 0) return null;
		File[] filesToLoad = new File[maxOffersPerPage];
		
		for(int i = 0; i < maxOffersPerPage; i++){
			if(i + (maxOffersPerPage * page) >= filesFound.length) break;
			filesToLoad[i] = filesFound[i + (maxOffersPerPage * page)];
		}
		
		for(File f : filesToLoad){
			if(f == null) continue;
			result.add(loadTradeOffer(f.getPath()));
		}
		
		return result;
	}
		
	public static TradeOffer loadTradeOffer(String location){
		TradeOffer result = null;
		File file = new File(location);
		
		try {
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(in);
			result = (TradeOffer) ois.readObject();
			ois.close();
			in.close();
		} catch (FileNotFoundException e) {
			Log.warn("FileNotFoundException | Couldn't load trade offer properly " + location);
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.warn("IOException | Couldn't load trade offer properly " + location);
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			Log.warn("ClassNotFoundException | Couldn't load trade offer properly " + location);
			e.printStackTrace();
			return null;
		}
		return result;
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
				if(s.startsWith("value")) res.value = Long.parseLong(s.substring(6, s.length()));
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
	 * Stats Methods
	 */
	
	public static Integer getStat(String key){
		Properties stats = loadStatsFile();	
		if(stats.containsKey(key)) return Integer.parseInt((String) stats.get(key));
		return 0;
	}
	
	public static void updateStat(String key, Integer value){
		Properties stats = loadStatsFile();
		String valueStr = value.toString();
		
		if(stats.containsKey(key)){
//			stats.replace(key, valueStr);
			stats.setProperty(key, valueStr);
		}
		else stats.put(key, valueStr);
		
		File file = new File(DATA_FOLDER + "/logs/marketplace/stats.dat");
		if(!file.exists()){
			Log.warn("Stats file doesn't exist, creating now.");
			createStatsFile(false);
		}
		
		try {
			OutputStream out = new FileOutputStream(file);
			stats.store(out, null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Properties loadStatsFile(){
		File file = new File(DATA_FOLDER + "/logs/marketplace/stats.dat");
		if(!file.exists()){
			Log.warn("Stats file doesn't exist, creating now.");
			createStatsFile(false);
		}
		
		Properties result = new Properties();
		
		try {
			InputStream in = new FileInputStream(file);
			result.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void createStatsFile(boolean override){
		Properties stats = new Properties();
		OutputStream out = null;
		
		File file = new File(DATA_FOLDER + "/logs/marketplace/stats.dat");
		if(file.exists()) return;
		try {
			out = new FileOutputStream(file);
			stats.store(out, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Properties Methods
	 */
	
	public static void createPropertiesFile(boolean override){
		Properties prop = new Properties();
		OutputStream out = null;
		
		File file = new File("config.prop");
		if(!override) if(file.exists()) return;
		Log.info("Creating properties file.");
		try {
			out = new FileOutputStream(file);
			prop.setProperty("port", "25565");
			prop.setProperty("galaxy_size", "3");
			prop.setProperty("region_size", "3");
			prop.setProperty("sector_size", "5");
			prop.setProperty("system_factor", "10");
			prop.setProperty("system_spacing", "30");
			prop.setProperty("starting_credits", "50000");
			prop.setProperty("starting_planets", "10");
			prop.setProperty("resource_multiplier", "4");
			prop.store(out, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.info("Could not save properties file.");
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Properties loadPropertiesFile(){
		Properties prop = new Properties();
		InputStream in = null;
		
		File file = new File("config.prop");
		if(!file.exists()){
			Log.warn("Properties file could not be found.");
			createPropertiesFile(true);
		}
		
		try {
			in = new FileInputStream(file);
			prop.load(in);
			return prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.warn("Could not load properties file.");
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
					String randomSector = DATA_FOLDER + "galaxy/r" + Globals.random.nextInt(Globals.galaxySize) + "_" + Globals.random.nextInt(Globals.galaxySize) + 
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
			Log.warn("Could not pick random planet name.");
			e.printStackTrace();
			return "null";
		} catch (IOException e) {
			Log.warn("Could not pick random planet name.");
			e.printStackTrace();
			return "null";
		}
		
		String name = lines.get(Globals.random.nextInt(lines.size()));
		return name;
	}
	
	public static int getPort(){
		Properties prop = loadPropertiesFile();
		return Integer.parseInt(prop.getProperty("port"));
	}
	
	public static int getPlayerId(){
		File file = new File(DATA_FOLDER + "players/");
		if(!file.exists()) return -1;
		return file.listFiles().length;
	}
	
	private static float calcPer(int a){
		if(planetsCreated == 0) return 0;
		return (float) ((a * 100) / planetsCreated);
	}
	
	private static void addFileSize(File file){
		if(file.exists()){
			double bytes = (file.length());
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			size += megabytes;
		}
	}

	private static void saveImage(int[][] array, String name){
		BufferedImage image = new BufferedImage(array.length, array.length, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < array.length; i++){
			for(int j = 0; j < array.length; j++){
				if(array[i][j] == -3){
					int c = new Color(0, 0, 255).getRGB();
					image.setRGB(i, j, c);
				}
				if(array[i][j] == -2){
					int c = new Color(0, 255, 0).getRGB();
					image.setRGB(i, j, c);
				}
				if(array[i][j] == -1){
					int c = new Color(190, 10, 10).getRGB();
					image.setRGB(i, j, c);
				}
				if(array[i][j] == 0){
					int c = new Color(10, 10, 10).getRGB();
					image.setRGB(i, j, c);
				}else if(array[i][j] == 1){
					int c = new Color(200, 200, 200).getRGB();
					image.setRGB(i, j, c);
				}
			}
		}
		File file = new File("images/" + name + ".png");
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getDistance(int xa, int ya, int xb, int yb){
		return (int) Math.sqrt((xa - xb) * (xa - xb) + (ya - yb) * (ya - yb));
	}
}
