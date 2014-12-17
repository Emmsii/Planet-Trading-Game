package com.gpg.planettrade.core.planet.resource;

import java.io.Serializable;

public class Resource implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String name;
	public String description;
	public String type;
	public int value;
	public int[] spawnOn;
	public int spawnChance;
	
	//Maybe here?
	public int amount;

}
