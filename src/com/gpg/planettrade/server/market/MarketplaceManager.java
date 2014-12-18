package com.gpg.planettrade.server.market;

import com.esotericsoftware.minlog.Log;

public class MarketplaceManager {

	private static long lastCheckTime;
	
	public static void init(){
		lastCheckTime = System.currentTimeMillis() / 1000;
	}
	
	public static void checks(){

	}
}
