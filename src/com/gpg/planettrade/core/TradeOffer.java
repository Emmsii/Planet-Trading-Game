package com.gpg.planettrade.core;

import java.io.Serializable;

public class TradeOffer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String placedBy;
	public long timePlaced;
	
	public int quantity;
	
	//TODO Maybe use resource instead of string type.
	public String type;
	public int priceEach;
}
