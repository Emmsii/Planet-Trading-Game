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
import com.gpg.planettrade.client.menu.popup.BuyPopup;
import com.gpg.planettrade.client.menu.popup.Popup;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.Network.BuyOffer;
import com.gpg.planettrade.core.Network.MarketOffers;
import com.gpg.planettrade.core.TradeOffer;

public class Marketplace extends Menu{

	protected List<TradeOffer> offers;

	private int xPos = 50;
	private int yPos = 80;
	
	private int page = 0;
	private int tradeOffersCount = 0;
	private int waitTime = 0;
	
	private Popup popup;
	
	public Marketplace(Mouse mouse, Keyboard key, MainComponent main) {
		super(mouse, key, main);

		components = new ArrayList<Component>();
		components.add(new TextButton(50, 675, 50, 20, -9, "Back"));
		components.add(new TextButton(xPos + 475, yPos + 550, 50, 20, -10, "Next"));
		components.add(new TextButton(xPos + 10, yPos + 550, 49, 20, -11, "Prev"));
		
		MarketOffers mo = new MarketOffers();
		mo.page = page;
		main.gameClient.client.sendTCP(mo);
		
	}

	public void init(List<TradeOffer> offersIn, int count){
		tradeOffersCount = count;
		this.offers = offersIn;
		//Load trade offers
//		goods = FileHandler.loadGoodsOffers();
//		planets = FileHandler.loadPlanetOffers();
		
		//Request Marketplace packets
		//Request trade offers by page.
		
		for(TradeOffer o : offers){
			if(o.timePlaced + o.length < GameTime.currentTimeSeconds){
				Log.info("Placed: " + o.timePlaced);
				Log.info("Length: " + o.length);
				Log.info("Cuttof: " + (o.timePlaced + o.length) + " is >= " + GameTime.currentTimeSeconds);
				Log.info("Trade has ended.");
				o.ended = true;
			}
			else o.ended = false;
		}
		
		initButtons();
	}
	
	@Override
	public void update() {
		if(waitTime > 0) waitTime--;
		if(components == null || offers == null) return;
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			if(popup == null){
				if(button.isPressed()){
					if(button.getId() == -9){
						main.switchState(2);
					}else if(button.getId() == -10){
						if(10 + (page * 10) <= tradeOffersCount){
							page++;
							changePage();
						}
					}else if(button.getId() == -11){
						if(waitTime != 0) continue;
						if(page - 1 < 0) page = 0;
						else{
							page--;
							changePage();
						}
					}else{
						popup = new BuyPopup(410, 250, mouse, key, this, offers.get(button.getId()));
					}
					
				}
			}
		}
		
		if(popup != null){
			popup.update();
			if(popup.isClosed()) popup = null;
		}

		for(int i = offers.size() - 1; i >= 0; i--){
			if(offers.get(i).ended){
				Log.info("ENDED");
				offers.remove(i);
				initButtons();
			}
		}
		
		if(GameTime.time % 120 == 0){
			MarketOffers mo = new MarketOffers();
			mo.page = page;
			main.gameClient.client.sendTCP(mo);
		}
	}

	@Override
	public void render(Graphics g) {
		if(components == null || offers == null) return;
//		for(Component c : components) c.render(g);
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			if(button.getId() == -10){
				if(page < (tradeOffersCount / 10)) button.render(g);
			}else if(button.getId() == -11){
				if(page - 1 >= 0) button.render(g);
			}else{
				button.render(g);
			}
		}
		
		Text.render("Page " + (page + 1) + "/" + ((tradeOffersCount / 10) + 1) + " (" + tradeOffersCount + " total trade offers)", 10, 70, 15, Font.BOLD, g);
		
		for(int i = 0; i < offers.size(); i++){
			TradeOffer o = offers.get(i);			
			g.drawRect(xPos, yPos + (i * 55), 530, 35);
			
			Text.render("Seller", xPos + 5, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(o.placedBy, xPos + 4, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Goods", xPos + 100, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(((GoodsOffer) o).type, xPos + 98, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			
			//TODO: Change based of instanceof type thing
			Text.render("Quantity", xPos + 180, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(Globals.formatInt(((GoodsOffer) o).quantity), xPos + 180, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Time Left", xPos + 280, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			
			long time = Math.abs(GameTime.currentTimeSeconds - (o.timePlaced + o.length));
			if(time == 0) o.ended = true;
			if(!o.ended) Text.render(GameTime.getTimeString(time), xPos + 280, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			else Text.render("ENDED", xPos + 280, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(179, 27, 27), g);
			
			Text.render(Globals.toCredits(((GoodsOffer) o).priceEach * ((GoodsOffer) o).quantity), xPos + 380, yPos + 20 + (i * 55), 18, Font.BOLD, new Color(81, 151, 201), g);
			Text.render(Globals.toCredits(((GoodsOffer) o).priceEach) + " each", xPos + 382, yPos + 29 + (i * 55), 10, Font.BOLD, new Color(81, 151, 201), g);
		}
			
		if(popup != null) popup.render(g);
	}
	
	private void initButtons(){
		//Called when trade offers change
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			if(button.getId() == -10 || button.getId() == -11 || button.getId() == -9) continue;
			else components.remove(i);
		}
		for(int i = 0; i < offers.size(); i++) if(!offers.get(i).ended) components.add(new TextButton(xPos + 475, yPos + 8 + (i * 55), 43, 20, i, "Buy"));
	}
	
	private void changePage(){
		waitTime = 20;
		MarketOffers mo = new MarketOffers();
		mo.page = page;
		main.gameClient.client.sendTCP(mo);
	}
	
	public void sendBuyOffer(BuyOffer bo){
		main.gameClient.client.sendTCP(bo);
	}

}
