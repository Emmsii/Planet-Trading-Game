package com.gpg.planettrade.core;

import com.gpg.planettrade.core.planet.resource.Resource;

public class GoodsOffer extends TradeOffer{

	private static final long serialVersionUID = 1L;
	
	public int quantity;
	
	//TODO Maybe use resource instead of string type.
	public Resource type;
	public long priceEach;

}
