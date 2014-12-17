package com.gpg.planettrade.client.component;

import java.awt.Graphics;

public abstract class Component {

	protected int x, y;
	
	public abstract void update();
	public abstract void render(Graphics g);
}
