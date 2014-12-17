package com.gpg.planettrade.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gpg.planettrade.core.Network;
import com.gpg.planettrade.core.Network.AddPlayer;
import com.gpg.planettrade.core.Network.Factories;
import com.gpg.planettrade.core.Network.OwnedPlanets;
import com.gpg.planettrade.core.Network.StoredCredits;
import com.gpg.planettrade.core.Network.Time;
import com.gpg.planettrade.core.Player;
import com.gpg.planettrade.core.planet.factory.FactoryManager;
import com.gpg.planettrade.core.planet.resource.ResourceManager;

public class GameServer {

	private Scanner scanner = new Scanner(System.in);
	private Server server;
	public HashSet<Player> players = new HashSet<Player>();
	
	public static void main(String[] args) throws IOException{
//		Log.set(Log.LEVEL_TRACE);
		new GameServer();
	}
	
	public GameServer() throws IOException{	
		ResourceManager.init();
		FactoryManager.init();
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
			}
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
