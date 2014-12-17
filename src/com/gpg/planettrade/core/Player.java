package com.gpg.planettrade.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.server.FileHandler;

public class Player implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int id;
	public String name;
	public int storedCredits;
	
	//STORE LIST OF FILE PATHS, INSTEAD OF PLANET OBJECTS.
	public List<String> ownedPlanets = new ArrayList<String>();
	
	public void init(int id, String name, int storedCredits){
		this.id = id;
		this.name = name;
		this.storedCredits = storedCredits;
		
		//Will make sure player doesn't get previously owned planet.
		while(ownedPlanets.size() <= Globals.startingPlanets){
			String p = FileHandler.findRandomPlanet(false);
			boolean same = false;
			for(String s : ownedPlanets) if(s.equalsIgnoreCase(p)) same = true;
			if(!same) ownedPlanets.add(p);
		}
	}
}
