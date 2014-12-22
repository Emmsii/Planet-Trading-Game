package com.gpg.planettrade.client.component;

import java.awt.Graphics;

public class Button extends Component{

	protected final int w, h;
	protected String text;
	protected boolean hover = false;
	public int pressTime = 0;
	public int waitTime = 10;
	
	protected int mouseX, mouseY, mouseB;
	
	protected int id;
	
	public Button(int x, int y, int w, int h, int id, String text){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.id = id;
		this.text = text;
	}
	
	public void update(){
		
	}
	
	@Override
	public void render(Graphics g) {
		
	}
	
	public boolean isPressed(){
		if(pressTime > 0) return true;
		return false;
	}
	
	public int getId(){
		return id;
	}
	
	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setMouse(int x, int y, int mouseB){
		mouseX = x;
		mouseY = y;
		this.mouseB = mouseB;
	}

}
