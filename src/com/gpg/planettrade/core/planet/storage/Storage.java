package com.gpg.planettrade.core.planet.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.core.planet.resource.Resource;

public class Storage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int price;
	
//	public int stored;
	public List<Container> containers = new ArrayList<Container>();
	public int maxStorage;
	
	//TODO: Store different resources with unique amounts.
	
	/*
	 * Storage Depo's and Containers:
	 * 
	 * When resource is put into storage, it goes into a container.
	 * 
	 */
	
	public void addContainer(int amount, Resource type){
		for(Container c : containers){
			if(c.type.name.equalsIgnoreCase(type.name)){
				//Already have this resource type. Add to it.
				c.amount += amount;
				return;
			}
		}
		
		Container con = new Container();
		con.init(amount, type);
		containers.add(con);
	}
	
	public int getStored(){
		int result = 0;
		for(Container c : containers) result += c.amount;
		return result;
	}
	
	public long getWorthLong(){
		long worth = 0;
		for(Container c : containers) worth += c.type.value * c.amount;
		return worth;
	}

}
