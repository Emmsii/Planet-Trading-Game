package com.gpg.planettrade.clientbutts.states;

import com.gpg.planettrade.clientbutts.gui.Button;

public class ConnectionState extends State implements StateInterface {

	Button button = new Button(10, 10, "Button");

	@Override
	public void update(StateInterface activeState)
	{
		button.update();
	}

	@Override
	public void render()
	{
//		font.headerFont(32f).drawString(32, 32, "Login & Connect");

		button.render();
	}
}