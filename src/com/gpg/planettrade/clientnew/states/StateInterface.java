package com.gpg.planettrade.clientnew.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface StateInterface {

	public void update(GameContainer gameContainer, int delta, StateInterface activeState);
	public void render(GameContainer gameContainer, Graphics graphics);

}
