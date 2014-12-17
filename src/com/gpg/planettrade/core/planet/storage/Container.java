package com.gpg.planettrade.core.planet.storage;

import java.io.Serializable;

import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.resource.Resource;

public class Container implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int amount;
	public Resource type;
	
	public void init(int amount, Resource type){
		this.amount = amount;
		this.type = type;
	}
	
	public String getWorth(){
		return Globals.toCredits(amount * type.value);
	}
}
