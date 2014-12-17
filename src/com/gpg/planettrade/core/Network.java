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

public class Network {

	public static final int PORT = 25565;
	
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
		
		//Register classes
		k.register(Player.class);
		k.register(Planet.class);
		k.register(Resource.class);
		k.register(Factory.class);
		k.register(Storage.class);
		k.register(Factory[].class);
		k.register(Container.class);	
		
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
}
