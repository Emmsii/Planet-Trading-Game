package com.gpg.planettrade.clientnew.states;

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
