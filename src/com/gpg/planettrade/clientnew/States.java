package com.gpg.planettrade.clientnew;

import com.gpg.planettrade.clientnew.states.ConnectionState;
import com.gpg.planettrade.clientnew.states.MainMenuState;
import com.gpg.planettrade.clientnew.states.StateInterface;

public enum States {
	CONNECTION_STATE (new ConnectionState()),
	MAIN_MENU_STATE (new MainMenuState())
	;

	private final StateInterface state;

	States(StateInterface state)
	{
		this.state = state;
	}

	public StateInterface getState()
	{
		return state;
	}
}