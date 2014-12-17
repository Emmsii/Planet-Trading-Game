package com.gpg.planettrade.core;

import java.io.Serializable;

import com.gpg.planettrade.core.planet.storage.Container;

public class TradeOffer implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public String placedBy;
	public int timePlaced;
	public Container container;
}
