package com.gpg.planettrade.client.menu;

import java.awt.Graphics;
import java.util.List;

import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.util.Mouse;


public abstract class Menu {

	protected Mouse mouse;
	protected MainComponent main;
	
	protected List<Component> components;
	
	public Menu(Mouse mouse, MainComponent main){
		this.mouse = mouse;
		this.main = main;
	}
	
	public abstract void update();
	public abstract void render(Graphics g);
}
