package com.gpg.planettrade.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.Network.AddOffer;
import com.gpg.planettrade.core.Network.BuyOffer;
import com.gpg.planettrade.core.Network.ChatMessage;
import com.gpg.planettrade.core.Network.Login;
import com.gpg.planettrade.core.Network.MarketOffers;
import com.gpg.planettrade.core.Network.MarketStats;
import com.gpg.planettrade.core.Network.RemovePlayer;
import com.gpg.planettrade.core.Network.StoredCredits;
import com.gpg.planettrade.core.Network.TakeCredits;
import com.gpg.planettrade.core.Network.UpdatePlanet;
import com.gpg.planettrade.core.Player;
import com.gpg.planettrade.core.TradeOffer;
import com.gpg.planettrade.server.GameServer.PlayerConnection;
import com.gpg.planettrade.server.market.MarketplaceManager;

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
		
		if(o instanceof ChatMessage){
			ChatMessage msg = (ChatMessage) o;
			//TODO: Chat channels
			if(msg.from == null || msg.text == null) return;
			if(msg.text.trim().length() == 0) return;
			server.sendToAllTCP(msg);
			return;
		}
		
		/** A new trade offer has been submitted, tell all other players to add to list.*/
		if(o instanceof TradeOffer){
			TradeOffer offer = (TradeOffer) o;
			FileHandler.updateStat("trades", FileHandler.getStat("trades") + 1);
			if(!FileHandler.saveTradeOffer(offer)) Log.warn("Could not save trade offer to data folder.");
			AddOffer addOffer = new AddOffer();
			addOffer.offer = offer;
			addOffer.count = FileHandler.countTradeOffers();
			updateStats(o);
			server.sendToAllTCP(addOffer);
			return;
		}
		
		if(o instanceof MarketStats){
			updateStats(o);
			return;
		}
		
		/** A player has gone onto the marketplace screen, give them the latest 10 offers, based off page number.*/
		if(o instanceof MarketOffers){
			MarketOffers mo = (MarketOffers) o;
			MarketplaceManager.checks();
			
			int page = mo.page;
			int count = FileHandler.countTradeOffers();
			int offersPerPage = mo.offersPerPage;
			int pageOffset = page * offersPerPage;
			
			int startIndex = count - pageOffset;
			int endIndex = (count - offersPerPage - pageOffset);
			if(endIndex < 0) endIndex = 0;
			
			String[] files = FileHandler.loadTradeOfferFiles();
			mo.offers = new TradeOffer[10];
			int j = 0;

			for(int i = startIndex - 1; i >= endIndex; i--){
				mo.offers[j] = FileHandler.loadTradeOffer(files[i]);
				j++;
			}

			mo.count = count;
			updateStats(o);
			c.sendTCP(mo);
			return;
		}
		
		if(o instanceof BuyOffer){
			BuyOffer buy = (BuyOffer) o;
			String seller = buy.offer.placedBy;
			String buyer = player.name;
			Log.info(buyer + " just bought [" + buy.amount + "] " + ((GoodsOffer) buy.offer).type + " from " + seller + " for " + Globals.toCredits(((GoodsOffer)buy.offer).quantity * ((GoodsOffer) buy.offer).priceEach));
			//C equals the person doing the buying.
			//give C the contents of the trade offer.
			//Give the seller (from the trade offer) credits.
			//take credits from C.
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
		
	
	public void updateStats(Object o){
		MarketStats ms = new MarketStats();
		ms.total = FileHandler.getStat("trades");
		ms.ended = FileHandler.getStat("ended");
		server.sendToAllTCP(ms);
	}
}
