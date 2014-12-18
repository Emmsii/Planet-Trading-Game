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
import com.gpg.planettrade.server.market.MarketplaceManager;

public class GameServer {

	private Scanner scanner = new Scanner(System.in);
	private Server server;
	public HashSet<Player> players = new HashSet<Player>();
	
	public static void main(String[] args) throws IOException{
//		Log.set(Log.LEVEL_TRACE);
		new GameServer();
	}
	
	public GameServer() throws IOException{	
		FileHandler.createPropertiesFile(false);
		Globals.init(FileHandler.loadPropertiesFile());
		ResourceManager.init();
		FactoryManager.init();
		MarketplaceManager.init();
		FileHandler.createFolderStructure();
		
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
			command = scanner.nextLine();
			if(command.equalsIgnoreCase("stop")){
				server.stop();
				System.exit(0);
				continue;
			}
			if(command.startsWith("config")){
				String[] split = command.split("\\s+");
				if(split.length == 1){
					Log.info("Usage: config reload | config delete | config start");
					continue;
				}
				if(split[1].equalsIgnoreCase("reload")){
					Log.info("Reloading config file. Note, values that affect galaxy generation wont make any changes.");
					Globals.init(FileHandler.loadPropertiesFile());
					continue;
				}
			}
			if(command.startsWith("galaxy")){
				continue;
			}
			if(command.equalsIgnoreCase("?") || command.equalsIgnoreCase("help")){
				Log.info("Server Commands\n" +
						"stop - Stops the server safely.\n" +
						"kick all, [playername] - Kicks all players or certain players.\n" +
						"ban [playername] [time] - Bans a player, time value is optional (in seconds)." +
						"config reload - Reloads the config file.\n" +
						"galaxy factories stop - Stops all factories in the galaxy processing.");
				continue;
			}
			
			Log.info("That command doesn't exist. Try using 'help' or '?' for a list of commands.");
		}
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
	
	public boolean takeCredits(Player player, int amount){
		Player p = FileHandler.loadPlayer(player.name);		
		if(p.storedCredits - amount >= 0) p.storedCredits -= amount;
		else return false;
		FileHandler.savePlayer(p);
		return true;
	}
	
	static class PlayerConnection extends Connection{
		public Player player;
	}
}
