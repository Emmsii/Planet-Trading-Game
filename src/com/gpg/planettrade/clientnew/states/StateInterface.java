package com.gpg.planettrade.clientnew.states;

import com.gpg.planettrade.clientnew.gui.FontRenderer;

public interface StateInterface {

	public void update(StateInterface activeState);
	public void render(FontRenderer font);

}
