package com.gpg.planettrade.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network;
import com.gpg.planettrade.core.Network.AddPlayer;
import com.gpg.planettrade.core.Network.Factories;
import com.gpg.planettrade.core.Network.OwnedPlanets;
import com.gpg.planettrade.core.Network.StoredCredits;
import com.gpg.planettrade.core.Network.Time;
import com.gpg.planettrade.core.Player;
import com.gpg.planettrade.core.planet.factory.FactoryManager;
import com.gpg.planettrade.core.planet.resource.ResourceManager;
import com.gpg.planettrade.server.market.MarketManager;

public class GameServer {

	private Scanner scanner = new Scanner(System.in);
	private Server server;
	public HashSet<Player> players = new HashSet<Player>();
	
	
	public static void main(String[] args) throws IOException{
		new GameServer();
	}
	
	private void init(){
		FileHandler.createPropertiesFile(false);
		Globals.init(FileHandler.loadPropertiesFile());
		ResourceManager.init();
		FactoryManager.init();
		FileHandler.createFolderStructure();
		FileHandler.createStatsFile(false);
		MarketManager.checks();
		
		Log.info("Found " + FileHandler.countTradeOffers() + " active trade offers out of " + FileHandler.getStat("trades") + ".");
	}
	
	public GameServer() throws IOException{
		init();
		server = new Server(10250, 10250){
			protected Connection newConnection(){
				return new PlayerConnection();
			}
		};
		Network.register(server);
		
		server.addListener(new ServerListener(this, server));
		server.bind(Network.PORT);
		server.start();
		
		
		
		String command = "";
		
		while(true){
//			MarketplaceManager.checks();
			command = scanner.nextLine();
			doCommands(command);
			/*
			 * TODO:
			 * MarketplaceManager
			 * Every minute (for example) check all trade offers
			 * If offer has expired, remove it. 
			 */
		}
	}
	
	private void doCommands(String command){
		if(command == null || command.trim() == "") return;
		
		if(command.equalsIgnoreCase("stop")){
			server.stop();
			System.exit(0);
			return;
		}
		
		if(command.startsWith("config")){
			String[] split = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: config reload | config delete | config start");
				return;
			}
			if(split[1].equalsIgnoreCase("reload")){
				Log.info("Reloading config file. Note, values that affect galaxy generation wont make any changes.");
				Globals.init(FileHandler.loadPropertiesFile());
				return;
			}
			Log.info("Usage: config reload | config delete | config start");
			return;
		}
		
		if(command.startsWith("kick")){
			String[] split = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: kick all | kick [playername]");
				return;
			}
			
			if(split[1].equalsIgnoreCase("all")){
				for(Connection c : server.getConnections()) c.close();
				Log.info("All players have been kicked.");
				return;
			}else{
				boolean kicked = false;
				for(Connection c : server.getConnections()){
					PlayerConnection pc = (PlayerConnection) c;
					if(pc.player.name.equalsIgnoreCase(split[1])){
						Log.info(pc.player.name + " has been kicked.");
						pc.close();
						kicked = true;
						break;
					}
				}
				if(!kicked){
					Log.info("Could not find player " + split[1]);
					return;
				}
				return;
			}
		}
		
		if(command.startsWith("ban")){
			String split[] = command.split("\\s+");
			if(split.length == 1){
				Log.info("Usage: ban [playername] [time]");
				Log.info("Time value is in seconds (optional).");
				return;
			}
			if(split.length == 2){
				//kick player
				//add player to bans list
			}
			if(split.length == 3){
				//kick player
				//add player to bans list
				//add time of ban to ban list.
			}
			return;
		}
		
		if(command.equalsIgnoreCase("list")){
			String result = "";
			for(Connection c : server.getConnections()){
				PlayerConnection pc = (PlayerConnection) c;
				result = result + pc.player.name + ", ";
			}
			Log.info("Connected Players [" + server.getConnections().length + "] " + result);
			return;
		}
		
		if(command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")){
			Log.info("Server Commands\n" +
					"stop - Stops the server safely.\n" +
					"kick all, [playername] - Kicks all players or certain players.\n" +
					"ban [playername] [time] - Bans a player, time value is optional (in seconds)." +
					"config reload - Reloads the config file.\n" +
					"list - Shows list of connected player.");
			return;
		}
		
		Log.info("That command doesn't exist. Try using 'help' or '?' for a list of commands.");
	}
	
	public void loggedIn(PlayerConnection c, Player player){
		c.player = player;
		
		for(Player p : players){
			AddPlayer addPlayer = new AddPlayer();
			addPlayer.name = p.name;
			c.sendTCP(addPlayer);
		}
		
		players.add(player);
			
		AddPlayer addPlayer = new AddPlayer();
		addPlayer.name = player.name;
		server.sendToAllTCP(addPlayer);
		
		Time time = new Time();
		time.time = System.currentTimeMillis() / 1000;
		c.sendTCP(time);
		
		StoredCredits storedCredits = new StoredCredits();
		storedCredits.credits = player.storedCredits;
		c.sendTCP(storedCredits);
		
		Factories fact = new Factories();
		fact.factories = FactoryManager.factories;
		c.sendTCP(fact);
		
		OwnedPlanets ownedPlanets = new OwnedPlanets();
		ownedPlanets.planets = FileHandler.getOwnedPlanets(player.name);
		c.sendTCP(ownedPlanets);
	}
	
	public boolean takeCredits(Player player, long amount){
		Player p = FileHandler.loadPlayer(player.name);		
		if(p.storedCredits - amount >= 0) p.storedCredits -= amount;
		else return false;
		FileHandler.savePlayer(p);
		return true;
	}
	
	public void giveCredits(Player player, long amount){
		player.storedCredits += amount;
		FileHandler.savePlayer(player);
	}
	
	static class PlayerConnection extends Connection{
		public Player player;
	}
}
