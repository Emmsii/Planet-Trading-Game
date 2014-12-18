package com.gpg.planettrade.client.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.PlanetOffer;
import com.gpg.planettrade.server.FileHandler;

public class Marketplace extends Menu{
	
	/*
	 * TODO:
	 * List all trades.
	 */

	protected List<GoodsOffer> goods;
	protected List<PlanetOffer> planets;
	
	private int xPos = 50;
	private int yPos = 80;
	
	public Marketplace(Mouse mouse, Keyboard key, MainComponent main) {
		super(mouse, key, main);
		init();
	}

	private void init(){
		//Load trade offers
		goods = FileHandler.loadGoodsOffers();
		planets = FileHandler.loadPlanetOffers();
		
		/*
		 * TODO:
		 * The hell are you doing! DON'T LOAD IN OFFERS FROM A FILE!!!
		 * THEY ARE ON THE SERVER! NOT THE CLIENT!
		 * moron...
		 */
		
		Log.info("Goods Offers Found: " + goods.size());
		Log.info("Planet Offers Found: " + planets.size());
		
		components = new ArrayList<Component>();
		for(int i = 0; i < goods.size(); i++){
			components.add(new TextButton(xPos + 475, yPos + 8 + (i * 55), 43, 20, i, "Buy"));
		}
	}
	
	@Override
	public void update() {
		for(Component c : components){
			Button button = (Button) c;
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
		}
		
		for(int i = 0; i < goods.size(); i++){
			GoodsOffer g = goods.get(i);
			if(g.ended){
				//Trade has ended, 
				for(int j = components.size() - 1; j >= 0; j--){
					Button button = (Button) components.get(j);
					if(button.getId() == i) components.remove(j);
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {

		for(int i = 0; i < goods.size(); i++){
			GoodsOffer o = goods.get(i);			
			g.drawRect(xPos, yPos + (i * 55), 530, 35);
			
			Text.render("Seller", xPos + 5, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(o.placedBy, xPos + 4, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Goods", xPos + 100, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(o.type, xPos + 98, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Quantity", xPos + 180, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(Globals.formatInt(o.quantity), xPos + 180, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Time Left", xPos + 280, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			
			long time = Math.abs(GameTime.currentTimeSeconds - (o.timePlaced + o.length));
			if(time == 0) o.ended = true;
			if(!o.ended) Text.render(GameTime.getTimeString(time), xPos + 280, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			else Text.render("ENDED", xPos + 280, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(179, 27, 27), g);
			
			Text.render(Globals.toCredits(o.priceEach * o.quantity), xPos + 380, yPos + 20 + (i * 55), 18, Font.BOLD, new Color(81, 151, 201), g);
			Text.render(Globals.toCredits(o.priceEach) + " each", xPos + 382, yPos + 29 + (i * 55), 10, Font.BOLD, new Color(81, 151, 201), g);
		}
		
		for(Component c : components) c.render(g);
	}

}
