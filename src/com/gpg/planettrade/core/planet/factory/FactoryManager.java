package com.gpg.planettrade.core.planet.factory;

import com.gpg.planettrade.server.FileHandler;

public class FactoryManager {

	public static Factory[] factories;
	
	public static void init(){
		factories = FileHandler.loadFactories();
	}
}
