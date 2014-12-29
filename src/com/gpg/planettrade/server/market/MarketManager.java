package com.gpg.planettrade.server.market;

import com.gpg.planettrade.core.TradeOffer;
import com.gpg.planettrade.server.FileHandler;


public class MarketManager {
	
	public static void checks(){
		//Will check all files in the marketplace folder, if they have ended move them to log.
		String[] files = FileHandler.loadTradeOfferFiles();
		
		for(int i = 0; i < files.length; i++){
			TradeOffer o = FileHandler.loadTradeOffer(files[i]);
			if(o.ended || o.timePlaced + o.length < System.currentTimeMillis() / 1000){
				o.ended = true;
				FileHandler.moveTradeOffer(files[i]);
				//TODO: UPDATE STATS FILE
			}
		}
	}
}
