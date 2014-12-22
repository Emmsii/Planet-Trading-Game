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
				
		//Register classes
		k.register(Player.class);
		k.register(Planet.class);
		k.register(Resource.class);
		k.register(Factory.class);
		k.register(Storage.class);
		k.register(Factory[].class);
		k.register(Container.class);
		k.register(TradeOffer.class);
		k.register(PlanetOffer.class);
		k.register(GoodsOffer.class);
		
		//Register misc
		k.register(ArrayList.class);
		k.register(int[].class);
		
		/*
		 * REGISTER DEM CLASSES!
		 */
	}
		
	public static class Login{
		public String name;
	}
	
	public static class AddPlayer{
		public String name;
	}
	
	public static class RemovePlayer{
		public String name;
	}
	
	public static class OwnedPlanets{
		public List<Planet> planets;
	}
	
	public static class Time{
		public long time;
	}
	
	public static class StoredCredits{
		public int credits;
	}
	
	public static class Factories{
		public Factory[] factories;
	}
	
	public static class UpdatePlanet{
		public Planet planet;
	}
	
	public static class TakeCredits{
		public int amount;
	}
	
	public static class GiveCredits{
		public int amount;
	}
	
	public static class ChatMessage{
		public String from;
		public String text;
	}
	
	public static class OffersCount{
		public int count;
	}
	
	public static class MarketOffers{
		public int page;
		public int count;
		public List<TradeOffer> offers;
	}
	
	public static class BuyOffer{
		public TradeOffer offer;
		public int amount;
	}
}

