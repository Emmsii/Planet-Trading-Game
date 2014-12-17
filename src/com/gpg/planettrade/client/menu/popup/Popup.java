package com.gpg.planettrade.client.menu.popup;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;

public abstract class Popup {

	protected Mouse mouse;
	protected Keyboard key;
	protected Menu menu;
	protected int x, y;
	protected boolean closed = false;
	
	protected List<Component> components = new ArrayList<Component>();
	
	public Popup(int x, int y, Mouse mouse, Keyboard key, Menu menu){
		this.x = x;
		this.y = y;
		this.mouse = mouse;
		this.key = key;
		this.menu = menu;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
	
	public boolean isClosed(){
		return closed;
	}
	
}
