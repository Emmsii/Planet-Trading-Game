package com.gpg.planettrade.core;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.gpg.planettrade.core.planet.Planet;
import com.gpg.planettrade.core.planet.factory.Factory;
import com.gpg.planettrade.core.planet.resource.Resource;
import com.gpg.planettrade.core.planet.storage.Container;
import com.gpg.planettrade.core.planet.storage.Storage;
import com.gpg.planettrade.server.FileHandler;

public class Network {

	public static final int PORT = FileHandler.getPort();
	
	public static void register(EndPoint endPoint){
		Kryo k = endPoint.getKryo();
		
		//Register packets
		k.register(Login.class);
		k.register(AddPlayer.class);
		k.register(RemovePlayer.class);
		k.register(OwnedPlanets.class);
		k.register(Time.class);
		k.register(StoredCredits.class);
		k.register(Factories.class);
		k.register(TakeCredits.class);
		k.register(GiveCredits.class);
		k.register(UpdatePlanet.class);
		k.register(ChatMessage.class);
		k.register(MarketOffers.class);
		k.register(OffersCount.class);
		k.register(BuyOffer.class);
		k.register(AddOffer.class);
		k.register(RemoveOffer.class);
		k.register(MarketStats.class);
		
		//Register classes
		k.register(Player.class);
		k.register(Planet.class);
		k.register(Resource.class);
		k.register(Factory.class);
		k.register(Storage.class);
		k.register(Factory[].class);
		k.register(Container.class);
		k.register(TradeOffer.class);
		k.register(TradeOffer[].class);
		k.register(PlanetOffer.class);
		k.register(GoodsOffer.class);
		
		//Register misc
		k.register(ArrayList.class);
		k.register(int[].class);
		
		/*
		 * REGISTER DEM CLASSES!
		 */
	}
		
	/*
	 * Misc Packets
	 */
	
	/** Returns server time.*/
	public static class Time{
		public long time;
	}
	
	/*
	 * Player Packets
	 */
	
	/** Username of player to login. */
	public static class Login{
		public String name;
	}
	
	/** Username to add to each players list of players, when user logs in.*/
	public static class AddPlayer{
		public String name;
	}
	
	/** Username to remove from each players list of players, when user logs out.*/
	public static class RemovePlayer{
		public String name;
	}
	
	/** Returns list of planets owned by specific player.*/
	public static class OwnedPlanets{
		public List<Planet> planets;
	}
		
	/** Returns amount of credits player has stored.*/
	public static class StoredCredits{
		public long credits;
	}
	
	/*
	 * Planet Packets
	 */
	
	/** Returns list of factories on specific planet.*/
	public static class Factories{
		public Factory[] factories;
	}
	
	/** Save changes to planet to file.*/
	public static class UpdatePlanet{
		public Planet planet;
	}
	
	/*
	 * Money Packets
	 */
	
	/** Take certain amount of credits from player.*/
	public static class TakeCredits{
		public long amount;
	}
	
	/** Give certain amount of credits to player.*/
	public static class GiveCredits{
		public long amount;
	}
	
	/*
	 * Chat Packets
	 */
	
	/** Contains chat info: who message is from & content of message.*/
	public static class ChatMessage{
		public String from;
		public String text;
	}
	
	/*
	 * Marketplace Packets
	 */
	
	public static class MarketStats{
		public int total;
		public int ended;
	}
	
	/** Returns amount of active trade offers (have not ended).*/
	public static class OffersCount{
		public int count;
	}
	
	/** Returns list of trade offers based off page number.*/
	public static class MarketOffers{
		public int page;
		public int count;
		public int offersPerPage;
		public TradeOffer[] offers;
	}
	
	/** Add an offer to all players list of offers.*/
	public static class AddOffer{
		public TradeOffer offer;
		public int count;
	}

	/** Remove an offer from all players list of offers.*/
	public static class RemoveOffer{
		public TradeOffer offer;
		//TODO: Maybe change this ^^
	}
		
	/** Tells server that specific trade offer has been bought.*/
	public static class BuyOffer{
		public TradeOffer offer;
		public int amount;
	}
}

