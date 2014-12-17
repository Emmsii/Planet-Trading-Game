package com.gpg.planettrade.core.planet.factory;

import java.io.Serializable;

public class Factory implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*
	 * TODO: IF YOU ADD A NEW VARIABLE, ADD IT TO THE LIST IN THE FACTORYPOPUP MENU!
	 */
	
	public String name;
	public String description;
	public String type;
	public int price;
	public String input;
		
	public long startTime;
	public int ups;
	
	public long actuallyProcessed;
	public long shouldHaveBeenProcessed;
	public long secondsPassed;
	

}
