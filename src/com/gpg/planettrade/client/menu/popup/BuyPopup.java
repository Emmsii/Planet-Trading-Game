package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.component.TextField;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.menu.PlanetMenu;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.GoodsOffer;
import com.gpg.planettrade.core.planet.storage.Container;
import com.gpg.planettrade.core.planet.storage.Storage;

public class BuyPopup extends Popup{

	private Container container;
	private Storage storage;
	private int amount;
	
	public BuyPopup(int x, int y, Mouse mouse, Keyboard key, Menu menu, Storage storage) {
		super(x, y, mouse, key, menu);
		this.storage = storage;
		
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
	
	public void init(Container c){
		//Sell a specific container
		container = c;
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
					GoodsOffer offer = new GoodsOffer();
					offer.placedBy = Globals.username;
					offer.timePlaced = GameTime.currentTimeSeconds;
					offer.length = 60; 
					offer.priceEach = container.type.value;
					offer.quantity = amount;
					offer.type = container.type.name;
					((PlanetMenu) menu).newGoodsOffer(offer, storage);
					closed = true;
					return;
				}
				
				if(button.pressTime == 8){
					if(button.getId() == 1){
						if(amount + 10 <= container.amount) amount += 10;
						else amount = container.amount;
					}
					if(button.getId() == 3){
						if(amount + 100 <= container.amount) amount += 100;
						else amount = container.amount;
					}
					if(button.getId() == 5){
						if(amount + 1000 <= container.amount) amount += 1000;
						else amount = container.amount;
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
				
				if(button.getId() == 7) amount = container.amount;
				if(button.getId() == 8) amount = 0;
				if(button.getId() == 9){
					for(Component com : components)
						if(com instanceof TextField){
							if(((TextField) com).getText() == "") break;
							int a = (int) Long.parseLong(((TextField) com).getText());
							if(amount + a <= container.amount) amount = a;
							else amount = container.amount;
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
		
		Text.render(Globals.formatInt(amount) + " units", x + 84, y + 150, 15, Font.BOLD, new Color(150, 150, 150), g); 
		
		Text.render(container.type.name, x + 20, y + 50, 15, Font.BOLD, g);
		Text.render(Globals.formatInt(container.amount) + " units", x + 20, y + 65, 15, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Can sell for:", x + 200, y + 150, 12, Font.BOLD, g);
		Text.render(Globals.toCredits(amount * container.type.value), x + 200, y + 175, 25, Font.BOLD, new Color(81, 151, 201), g);
		Text.render(Globals.toCredits(container.type.value) + " each", x + 202, y + 188, 10, Font.BOLD, new Color(81, 151, 201), g);
		
		for(Component c : components) c.render(g);
	}

}
