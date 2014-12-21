package com.gpg.planettrade.client.menu;

import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Text;

public class LoadingMenu extends Menu{

	private MainComponent main;
	
	public LoadingMenu(MainComponent main) {
		super(null, null, null);
		this.main = main;
	}

	@Override
	public void update() {
		/*
		 * TODO:
		 * Do all checks...
		 * 
		 * If all checks pass, then this menu can be closed.
		 * 
		 * 
		 */
		
		if(GameTime.currentTimeSeconds != -1){
			main.switchState(main.STARTING_STATE);
		}
	}

	@Override
	public void render(Graphics g) {
		Text.render("Loading...", 580, 350, 22, Font.BOLD, g);
	}

}
