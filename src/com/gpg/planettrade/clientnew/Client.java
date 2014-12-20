package com.gpg.planettrade.clientnew;

import com.gpg.planettrade.clientnew.util.Magicalness;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Magicalness {

	public Client(String name)
	{
		super(name);
	}

	/**
	 * @param gameContainer The container holing this game
	 * @see org.newdawn.slick.Game#init(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void init(GameContainer gameContainer) throws SlickException
	{
		//
	}

	/**
	 * @param gameContainer The container holing this game
	 * @param delta         Time elapsed since last update
	 * @see org.newdawn.slick.Game#update(org.newdawn.slick.GameContainer, int)
	 */
	@Override
	public void update(GameContainer gameContainer, int delta) throws SlickException
	{
		activeState.update(gameContainer, delta, activeState);
	}

	/**
	 * Render the game's screen here.
	 *
	 * @param gameContainer The container holing this game
	 * @param graphics      The graphics context that can be used to render. However, normal rendering routines can also be used.
	 * @throws org.newdawn.slick.SlickException Throw to indicate a internal error
	 */
	@Override
	public void render(GameContainer gameContainer, Graphics graphics) throws SlickException
	{
		activeState.render(gameContainer, graphics);
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appGameContainer;
			appGameContainer = new AppGameContainer(new Client("Planet Trading Game"));
			appGameContainer.setDisplayMode(1280, 720, false);
			appGameContainer.start();
		}
		catch (SlickException e)
		{
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
