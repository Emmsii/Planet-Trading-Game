package com.gpg.planettrade.core.planet.resource;

import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.server.FileHandler;

public class ResourceManager {

	public static Resource[] resources;
	
	public static void init(){
		resources = FileHandler.loadResources();
	}
	
	public static List<Resource> getResourceByType(int type){
		List<Resource> result = new ArrayList<Resource>();
		for(int i = 0; i < resources.length; i++){
			for(int j = 0; j < resources[i].spawnOn.length; j++){
				if(resources[i].spawnOn[j] == type) result.add(resources[i]);
			}
		}
		return result;
	}
}
