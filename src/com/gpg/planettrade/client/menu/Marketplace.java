package com.gpg.planettrade.client.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

	private SimpleDateFormat df = new SimpleDateFormat("dd:MM:yy @ HH:mm");

	protected TradeOffer[] offers = new TradeOffer[10];
	
	private int xPos = 50;
	private int yPos = 80;
	
	private int offersPerPage = 10;
	private int page = 0;
	private int totalTradeOffers = 0;
	private int waitTime = 0;
	
	private Popup popup;
	
	public Marketplace(Mouse mouse, Keyboard key, MainComponent main) {
		super(mouse, key, main);

		components = new ArrayList<Component>();
		components.add(new TextButton(50, 675, 50, 20, -9, "Back"));
		components.add(new TextButton(xPos + 475, yPos + 550, 50, 20, -10, "Next"));
		components.add(new TextButton(xPos + 10, yPos + 550, 49, 20, -11, "Prev"));
		
		//Tell the server the player wants all offers.
		MarketOffers mo = new MarketOffers();
		mo.page = page;
		mo.offersPerPage = offersPerPage;
		main.gameClient.client.sendTCP(mo);
		
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
						if(waitTime != 0) continue;
						if(offersPerPage + (page * offersPerPage) <= totalTradeOffers - 1){
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
						popup = new BuyPopup(410, 240, mouse, key, this, offers[button.getId()]);
					}
					
				}
			}
		}
		
		if(popup != null){
			popup.update();
			if(popup.isClosed()) popup = null;
		}

		for(int i = offers.length - 1; i >= 0; i--){
			if(offers[i] == null) continue;
			if(offers[i].ended){
				Log.info("ENDED");
				offers[i] = null;
				initButtons();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(components == null || offers == null) return;
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			if(button.getId() == -10){
				if(page <= (totalTradeOffers / offersPerPage) - 1) button.render(g);
			}else if(button.getId() == -11){
				if(page - 1 >= 0) button.render(g);
			}else{
				button.render(g);
			}
		}
		
		Text.render("Page " + (page + 1) + "/" + ((totalTradeOffers / offersPerPage) + 1), 10, 70, 15, Font.BOLD, g);
		
		for(int i = 0; i < offers.length; i++){
			TradeOffer o = offers[i];		
			if(o == null) continue;
			g.drawRect(xPos, yPos + (i * 55), 710, 35);
			
			Text.render("[" + o.id + "]", 10, yPos + 10 + (i * 55), 10, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Seller", xPos + 5, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(o.placedBy, xPos + 4, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Goods", xPos + 100, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(((GoodsOffer) o).type, xPos + 98, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			//TODO: Change based of instanceof type thing
			Text.render("Quantity", xPos + 180, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			Text.render(Globals.formatInt(((GoodsOffer) o).quantity), xPos + 180, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Placed On", xPos + 280, yPos + 15 + (i * 55), 12, Font.BOLD, g);
						
			Text.render(df.format(new Date(o.timePlaced * 1000)) + "", xPos + 280, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Time Left", xPos + 420, yPos + 15 + (i * 55), 12, Font.BOLD, g);
			long time = Math.abs(GameTime.currentTimeSeconds - (o.timePlaced + o.length));
			if(time == 0) o.ended = true;
			if(!o.ended){
				if(time > 1800) Text.render(GameTime.getTimeString(time), xPos + 420, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(120, 209, 69), g);
				else Text.render(GameTime.getTimeString(time), xPos + 420, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(209, 118, 69), g);
			}
			else Text.render("ENDED", xPos + 420, yPos + 30 + (i * 55), 15, Font.BOLD, new Color(179, 27, 27), g);
			
			Text.render(Globals.toCredits(((GoodsOffer) o).priceEach * ((GoodsOffer) o).quantity), xPos + 510, yPos + 20 + (i * 55), 18, Font.BOLD, new Color(81, 151, 201), g);
			Text.render(Globals.toCredits(((GoodsOffer) o).priceEach) + " each", xPos + 510, yPos + 29 + (i * 55), 10, Font.BOLD, new Color(81, 151, 201), g);
			

		}
		
		Text.render("Stats", 800, 90, 20, Font.BOLD, g);
		Text.render("Total Trades:", 800, 102, 12, Font.BOLD, g);
		Text.render("" + Globals.totalTrades, 800, 120, 20, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Trades Expired:", 800, 132, 12, Font.BOLD, g);
		Text.render("" + Globals.totalEndedTrades, 800, 152, 20, Font.BOLD, new Color(179, 27, 27), g);
		
		if(popup != null) popup.render(g);
	}
	
	private void initButtons(){
		//Called when trade offers change
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			if(button.getId() == -10 || button.getId() == -11 || button.getId() == -9) continue;
			else components.remove(i);
		}
		for(int i = 0; i < offers.length; i++) if(offers[i] != null && !offers[i].ended){
			if(!offers[i].placedBy.equalsIgnoreCase(Globals.username)) components.add(new TextButton(xPos + 650, yPos + 8 + (i * 55), 43, 20, i, "Buy"));
		}
	}
	
	private void changePage(){
		waitTime = 20;
		MarketOffers mo = new MarketOffers();
		mo.page = page;
		mo.offersPerPage = offersPerPage;
		main.gameClient.client.sendTCP(mo);
	}
	
	public void sendBuyOffer(BuyOffer bo){
		main.gameClient.client.sendTCP(bo);
	}
	
	public void replaceOffers(TradeOffer[] offers, int count){
		totalTradeOffers = count;
		this.offers = offers;
		initButtons();
	}
	
	public void addOffer(TradeOffer offer, int count){
		totalTradeOffers = count;
		if(offer == null){
			Log.warn("Trade offer is null. [Marketplace Class");
			return;
		}
		
		for(int i = offers.length - 2; i >= 0; i--) offers[i + 1] = offers[i];
		offers[0] = offer;
		initButtons();
	}
}
