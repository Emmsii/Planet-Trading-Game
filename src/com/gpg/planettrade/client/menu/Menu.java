package com.gpg.planettrade.client.menu;

import java.awt.Graphics;
import java.util.List;

import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;


public abstract class Menu {

	protected Mouse mouse;
	protected Keyboard key;
	protected MainComponent main;
	
	protected List<Component> components;
	
	public Menu(Mouse mouse, Keyboard key, MainComponent main){
		this.mouse = mouse;
		this.key = key;
		this.main = main;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
}
