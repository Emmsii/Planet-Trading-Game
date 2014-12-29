package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.component.TextField;
import com.gpg.planettrade.client.menu.Marketplace;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.Network.BuyOffer;
import com.gpg.planettrade.core.TradeOffer;

public class BuyPopup extends Popup{

	private int amount;
	private TradeOffer offer;
	
	private boolean noCredits = false;
	private boolean noStorage = false;
	
	/*
	 * TODO:
	 * Change buy popup based off trade offer type.
	 */
	
	public BuyPopup(int x, int y, Mouse mouse, Keyboard key, Menu menu, TradeOffer offer) {
		super(x, y, mouse, key, menu);
		this.offer = offer;
		
		components.add(new TextButton(x + 370, y, 30, 20, -1, "X"));
		
		components.add(new TextButton(x + 80, y + 160, 47, 20, 1, "+10"));
		components.add(new TextButton(x + 130, y + 160, 40, 20, 2, "-10"));
		components.add(new TextButton(x + 70, y + 185, 57, 20, 3, "+100"));
		components.add(new TextButton(x + 130, y + 185, 50, 20, 4, "-100"));
		components.add(new TextButton(x + 70, y + 210, 57, 20, 5, "+1000"));
		components.add(new TextButton(x + 130, y + 210, 57, 20, 6, "-1000"));
		
		components.add(new TextButton(x + 80, y + 235, 45, 20, 7, "Max"));
		components.add(new TextButton(x + 130, y + 235, 28, 20, 8, "0"));
		
		components.add(new TextButton(x + 300, y + 50, 43, 20, 0, "Buy"));
		
		components.add(new TextField(x + 50, y + 280, 11, TextField.JUST_NUMBERS, key));
		components.add(new TextButton(x + 202, y + 279, 42, 20, 9, "Set"));
		
	}

	@Override
	public void update() {
		for(Component c : components){
			if(c instanceof TextField){
				TextField field = (TextField) c;
				field.update();
				continue;
			}
			Button button = (Button) c;
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			
			if(button.isPressed()){
				if(button.getId() == -1) closed = true;
				
				if(button.getId() == 0){
					//Buy button is pressed!
					if(amount == 0) continue;
					if(Globals.storedCredits >= ((GoodsOffer) offer).quantity + ((GoodsOffer) offer).priceEach){
						//if(there is room in current planets storage)
						if((Globals.currentPlanet.maxStorage() - Globals.currentPlanet.storageLeft()) + ((GoodsOffer) offer).quantity <= Globals.currentPlanet.maxStorage()){
							//send buy packet
							BuyOffer bo = new BuyOffer();
							bo.offer = offer;
							bo.amount = amount;
							((Marketplace) menu).sendBuyOffer(bo);
							closed = true;
						}else{
							noStorage = true;
						}
					}else{
						noCredits = true;
					}
					return;
				}
				
				if(button.pressTime == 8){
					if(button.getId() == 1){
						if(amount + 10 < ((GoodsOffer) offer).quantity) amount += 10;
						else amount = ((GoodsOffer) offer).quantity;

					}
					if(button.getId() == 3){

					}
					if(button.getId() == 5){

					}
					
					if(button.getId() == 2) {
						if(amount - 10 >= 0) amount -= 10;
						else amount = 0;
					}
					if(button.getId() == 4) {
						if(amount - 100 >= 0) amount -= 100;
						else amount = 0;
					}
					if(button.getId() == 6) {
						if(amount - 1000 >= 0) amount -= 1000;
						else amount = 0;
					}
				}
				
				if(button.getId() == 7) amount = ((GoodsOffer)offer).quantity;
				if(button.getId() == 8) amount = 0;
				if(button.getId() == 9){
					for(Component com : components)
						if(com instanceof TextField){
							if(((TextField) com).getText().trim().length() == 0) break;
							int a = (int) Long.parseLong(((TextField) com).getText());
							if(amount + a <= ((GoodsOffer)offer).quantity) amount = a;
							else amount = ((GoodsOffer)offer).quantity;
							((TextField) com).clear();
							break;
					}
					
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x,  y, 400, 430);
		g.setColor(Color.WHITE);
		g.drawRect(x,  y, 400, 430);
		
		Text.render("Buy Goods", x + 20, y + 25, 18, Font.BOLD, g);
		
		Text.render(((GoodsOffer) offer).type, x + 20, y + 50, 15, Font.BOLD, g);
		Text.render(Globals.formatInt(((GoodsOffer) offer).quantity) + " units", x + 20, y + 65, 15, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Used Storage", x + 20, y + 80, 15, Font.BOLD, g);
		Text.render(Globals.formatInt(Globals.currentPlanet.maxStorage() - Globals.currentPlanet.storageLeft()) + "/" + Globals.formatInt(Globals.currentPlanet.maxStorage()) + " units", x + 20, y + 95, 15, Font.BOLD, new Color(150, 150, 150), g);
		
		Text.render(Globals.formatInt(amount) + " units", x + 84, y + 150, 15, Font.BOLD, new Color(150, 150, 150), g); 
		
		
		Text.render("Can buy for:", x + 200, y + 150, 12, Font.BOLD, g);
		Text.render(Globals.toCredits(amount * ((GoodsOffer) offer).priceEach), x + 200, y + 175, 25, Font.BOLD, new Color(81, 151, 201), g);
		Text.render(Globals.toCredits(((GoodsOffer) offer).priceEach) + " each", x + 202, y + 188, 10, Font.BOLD, new Color(81, 151, 201), g);

		if(noCredits) Text.render("Not enough credits.", x + 30, y + 350, 12, Font.BOLD, new Color(179, 27, 27), g);
		if(noStorage) Text.render("Not enough storage.", x + 30, y + 362, 12, Font.BOLD, new Color(179, 27, 27), g);
		
		for(Component c : components){
			if(c instanceof Button){
				Button button = (Button) c;
				if(button.getId() == 0){
					if(amount > 0) button.render(g);
				}else button.render(g);
			}else c.render(g);
		}
	}

}
