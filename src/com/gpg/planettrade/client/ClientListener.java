package com.gpg.planettrade.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.Ping;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network.AddOffer;
import com.gpg.planettrade.core.Network.AddPlayer;
import com.gpg.planettrade.core.Network.ChatMessage;
import com.gpg.planettrade.core.Network.Factories;
import com.gpg.planettrade.core.Network.MarketOffers;
import com.gpg.planettrade.core.Network.MarketStats;
import com.gpg.planettrade.core.Network.OwnedPlanets;
import com.gpg.planettrade.core.Network.RemovePlayer;
import com.gpg.planettrade.core.Network.StoredCredits;
import com.gpg.planettrade.core.Network.Time;
import com.gpg.planettrade.core.Network.UpdateOffer;
import com.gpg.planettrade.core.planet.factory.FactoryManager;

public class ClientListener extends Listener{

	private GameClient gameClient;
	private MainComponent main;
	
	public ClientListener(GameClient gameClient, MainComponent main){
		this.gameClient = gameClient;
		this.main = main;
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Ping){
			Ping ping = (Ping) o;
			if(ping.isReply) main.ping = c.getReturnTripTime();
			c.updateReturnTripTime();
			return;
		}
		
		if(o instanceof AddPlayer){
			AddPlayer addPlayer = (AddPlayer) o;
			gameClient.players.add(addPlayer.name);
			Log.info(addPlayer.name + " has connected!");
			return;
		}
		
		if(o instanceof RemovePlayer){
			RemovePlayer removePlayer = (RemovePlayer) o;
			gameClient.removePlayer(removePlayer.name);
			return;
		}
		
		if(o instanceof OwnedPlanets){
			OwnedPlanets ownedPlanets = (OwnedPlanets) o;
			Globals.ownedPlanets = ownedPlanets.planets;
			return;
		}
		
		if(o instanceof StoredCredits){
			StoredCredits storedCredits = (StoredCredits) o;
			Globals.storedCredits = storedCredits.credits;
			return;
		}
		
		if(o instanceof Time){
			Time time = (Time) o;
			GameTime.serverTimeSeconds = time.time;
			return;
		}
		
		if(o instanceof Factories){
			Factories fact = (Factories) o;
			FactoryManager.factories = fact.factories;
			return;
		}
		
		if(o instanceof ChatMessage){
			ChatMessage msg = (ChatMessage) o;
			main.addMessage(msg);
			return;
		}
		
		if(o instanceof MarketStats){
			MarketStats ms = (MarketStats) o;
			Globals.totalTrades = ms.total;
			Globals.totalSold = ms.sold;
			Globals.quantity = ms.quantity;
			Globals.creditsExchanged = ms.creditsExchanged;
			return;
		}
		
		if(o instanceof MarketOffers){
			MarketOffers mo = (MarketOffers) o;
			main.addTradeOffers(mo.offers, mo.count);
			return;
		}
		
		if(o instanceof AddOffer){
			AddOffer addOffer = (AddOffer) o;
			main.addTradeOffer(addOffer.offer, addOffer.count);
		}
		
		if(o instanceof UpdateOffer){
			UpdateOffer update = (UpdateOffer) o;
			main.updateTradeOffer(update.offer, update.sold);
		}
	}
	
	public void disconnected(Connection c){
		Log.info("You have been disconnected.");
		System.exit(0);
	}
	
	public void connected(Connection c){
		c.updateReturnTripTime();
	}
}
