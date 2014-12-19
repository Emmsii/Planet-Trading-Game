package com.gpg.planettrade.clientnew.states;

import com.gpg.planettrade.clientnew.gui.FontRenderer;

public class ConnectionState implements StateInterface {

	@Override
	public void update()
	{
		//
	}

	@Override
	public void render(FontRenderer font)
	{
		font.bodyFont(12f).drawString(100, 100, "Hello world!");
	}
}
