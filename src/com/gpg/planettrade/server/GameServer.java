package com.gpg.planettrade.server;

import java.io.IOException;
import java.util.HashSet;

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

	private Server server;
	private CommandListener commandListener;
	
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
		commandListener = new CommandListener(server);
		commandListener.start();
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
