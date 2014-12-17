package com.gpg.planettrade.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network.Login;
import com.gpg.planettrade.core.Network.RemovePlayer;
import com.gpg.planettrade.core.Network.StoredCredits;
import com.gpg.planettrade.core.Network.TakeCredits;
import com.gpg.planettrade.core.Network.UpdatePlanet;
import com.gpg.planettrade.core.Player;
import com.gpg.planettrade.server.GameServer.PlayerConnection;

public class ServerListener extends Listener{

	private GameServer gameServer;
	private Server server;
	
	public ServerListener(GameServer gameServer, Server server){
		this.gameServer = gameServer;
		this.server = server;
	}
	
	public void received(Connection c, Object o){		
		PlayerConnection connection = (PlayerConnection) c;
		Player player = connection.player;

		
		if(o instanceof Login){
			if(player != null) return;
			
			String name = ((Login) o).name;
			
			if(!isValid(name)){
				c.close();
				return;
			}
			
			player = FileHandler.loadPlayer(name);
			
			//If player doesn't exist in storage, they must be new. Create new player!
			if(player == null){
				player = new Player();
				player.init(FileHandler.getPlayerId(), name, Globals.startingCredits);
				
				if(!FileHandler.savePlayer(player)){
					Log.warn("Could not save player file.");
					c.close();
					return;
				}
			}
			
			for(Player p : gameServer.players){
				if(p.name.equalsIgnoreCase(name)){
					c.close();
					return;
				}
			}
			gameServer.loggedIn(connection, player);
			return;
		}
		
		if(o instanceof UpdatePlanet){
			UpdatePlanet updatePlanet = (UpdatePlanet) o;
			FileHandler.savePlanet(updatePlanet.planet);
		}
		
		if(o instanceof TakeCredits){
			TakeCredits take = (TakeCredits) o;
			if(!gameServer.takeCredits(player, take.amount)){
				//SEND ERROR POPUP TO PLAYER.
				//TODO: Make popup packet in client listener.
				Log.warn("Couldn't take credits from player.");
			}
			
			StoredCredits storedCredits = new StoredCredits();
			storedCredits.credits = FileHandler.loadPlayer(player.name).storedCredits;
			c.sendTCP(storedCredits);
			return;
		}
	}
	
	public void disconnected(Connection c){
		PlayerConnection connection = (PlayerConnection) c;
		if(connection.player != null){
			gameServer.players.remove(connection.player);
			
			RemovePlayer removePlayer = new RemovePlayer();
			removePlayer.name = connection.player.name;
			server.sendToAllTCP(removePlayer);
		}
	}
	
	public void connected(Connection c){
		
	}
	
	public boolean isValid(String value){
		if(value == null) return false;
		value = value.trim();
		if(value.length() == 0) return false;
		return true;
	}
			
}
