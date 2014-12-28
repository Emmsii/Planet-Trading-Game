package com.gpg.planettrade.core;

import java.io.Serializable;

public class TradeOffer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public int id;
	
	public String placedBy;
	public long timePlaced;
	public long length;
	public boolean ended;

}
