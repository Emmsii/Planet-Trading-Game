package com.gpg.planettrade.clientnew.states;

import com.gpg.planettrade.clientnew.gui.Button;
import com.gpg.planettrade.clientnew.gui.FontRenderer;

public class ConnectionState implements StateInterface {

	Button button = new Button(10, 10, "Button");

	@Override
	public void update(StateInterface activeState)
	{
		button.update();
	}

	@Override
	public void render(FontRenderer font)
	{
		font.headerFont(32f).drawString(32, 32, "Login & Connect");
		button.render();
	}
}
