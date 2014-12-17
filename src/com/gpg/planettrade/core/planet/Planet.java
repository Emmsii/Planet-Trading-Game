package com.gpg.planettrade.core.planet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.factory.Factory;
import com.gpg.planettrade.core.planet.resource.Resource;
import com.gpg.planettrade.core.planet.resource.ResourceManager;
import com.gpg.planettrade.core.planet.storage.Storage;


public class Planet implements Serializable{

	private static final long serialVersionUID = 1L;

	public String filePath;
	
	public int id;
	public int ownerId;
	public String name;
	public String subname;
	
	public int size;
	public int type;
	public int worth;
	
	public List<Resource> resources = new ArrayList<Resource>();
	public List<Factory> factories = new ArrayList<Factory>(); 
	public List<Storage> storage = new ArrayList<Storage>();
	
	public void init(int id, int ownerId, String name, String subname, int size, String filePath){
		this.id = id;
		this.ownerId = ownerId;
		this.name = name;
		this.subname = subname;
		this.size = size;
		this.filePath = filePath;
		
		double r = Math.random() * 100;
		if(r < 10) type = 0;
		else if(r < 25) type = 1;
		else if(r < 50) type = 2;
		else if(r < 65) type = 3;
		else if(r < 100) type = 4;
		else type = 0;
		
		//Get list of potential resources that can spawn on this planet type.
		List<Resource> tempResList = ResourceManager.getResourceByType(type);
		
		if(tempResList.size() == 0){
			//This planet cannot spawn resources.
			return;
		}
		
		while(resources.size() < size){
			for(Resource res : tempResList){
				r = Math.random() * 100;
				if(r < res.spawnChance){
					if(!alreadyHasResource(res)) continue;
					res.amount = (int) (((Globals.random.nextInt(4500 * size) + 500) * (size * 0.654)) * Globals.resourceFrequency);
					resources.add(res);
				}
			}
		}
	}
	
	
	public int getWorth(){
		worth = 0;
		for(Resource r : resources) worth += r.value * r.amount;
		for(Storage s : storage) worth += s.getWorthInt();
		return worth;
	}
	
	public int takeResource(int amount, String input){
		
		for(Resource r : resources){
			if(r.name.toLowerCase().equalsIgnoreCase(input)){
				//If the planet has the resource type the planet wants to process, carry on.
				if(r.amount - amount >= 0){
					int addingToStorage = addToStorage(amount, r);
					r.amount -= addingToStorage;
					return addingToStorage;
				}else{
					//The amount the factory wants to take is < 0
					int addingToStorage = addToStorage(r.amount, r);
					r.amount -= addingToStorage;
					return addingToStorage;
				}
			}
		}
		return 0;
		
//		for(Resource r : resources){
//			//If resource = the type the factory wants to take. Take the amount.
//			if(r.name.toLowerCase().equalsIgnoreCase(input)){
//				//Only take if will remain above 0;
//				if(r.amount - amount >= 0){
//					int addingToStorage = addToStorage(amount);
//					r.amount -= addingToStorage;
//					return addingToStorage;
//				}else{
//					int addingToStorage = addToStorage(amount);
//					return addingToStorage;
//				}
//			}
//		}
//		return 0;
	}
	
	public int addToStorage(int amount, Resource type){
		for(Storage s : storage){
			//If storage is full, move onto next storage
			if(s.getStored() == s.maxStorage) continue;
			
			
			if(s.getStored() + amount <= s.maxStorage){
				//If the amount to store is less than max storage. Add it.
				s.addContainer(amount, type);
				
				//Return the amount stored.
				return amount;
			}else{
				//The amount is greater than the max storage
				int difference = s.maxStorage - s.getStored();
				s.addContainer(difference, type);
				
				//Return what was stored.
				return difference;
			}
			
//			if(s.stored + amount <= s.maxStorage){
//				s.stored += amount;
//				return amount;
//			}else{
//				//Amount stored will be greater than max storage
//				int difference = s.maxStorage - s.stored;
//				s.stored = s.maxStorage;
//				return difference;
//			}
		}
		return 0;
	}
	
	public boolean isStorageFull(){
		for(Storage s : storage) if(s.getStored() != s.maxStorage) return false;
		return true;
	}
	
	public void addStorage(Storage s){
		storage.add(s);
	}
	
	public void addFactory(Factory f){
		factories.add(f);
	}
	
	private boolean alreadyHasResource(Resource res){
		for(Resource r : resources) if(r.name.equalsIgnoreCase(res.name)) return false;
		return true;
	}
	
}
