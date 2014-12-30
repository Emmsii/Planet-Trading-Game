package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.menu.PlanetMenu;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.storage.Container;
import com.gpg.planettrade.core.planet.storage.Storage;

public class StoragePopup extends Popup{

	private Storage storage;
	private List<Component> components = new ArrayList<Component>();
	
	public StoragePopup(int x, int y, Mouse mouse, Menu menu, Keyboard key, Storage storage) {
		super(x, y, mouse, key, menu);
		this.storage = storage;
				
		for(int i = 0; i < storage.containers.size(); i++) components.add(new TextButton(0, 0, 45, 20, i, "Sell"));
		components.add(new TextButton(x + 370, y, 30, 20, -1, "X"));
	}

	@Override
	public void update() {
		for(Component c : components){
			Button button = (Button) c;
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			
			if(button.isPressed()){
				if(button.getId() == -1) closed = true;
				else{
					SellPopup sellPop = new SellPopup(x, y, mouse, key, menu, storage);
					sellPop.init(storage.containers.get(button.getId()));
					((PlanetMenu) menu).popup = sellPop;
					closed = true;
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
				
		Text.render("Small Storage Depo", x + 20, y + 25, 18, Font.BOLD, g);
		Text.render(Globals.formatNumber(storage.getStored()) + "/" + Globals.formatNumber(storage.maxStorage), x + 23, y + 47, 15, Font.BOLD, new Color(150, 150, 150), g);
		
		int xPos = 40;
		int yPos = 70;
		
		for(int i = 0; i < storage.containers.size(); i++){
			Container c = storage.containers.get(i);
			g.drawRect(x + xPos, y + yPos + (i * 35), 200, 35);
			Text.render(c.type.name, x + xPos + 5, y + yPos + 18+ (i * 35), 15, Font.BOLD, g);
			Text.render(Globals.formatNumber(c.amount) + " units", x + xPos + 5, y + yPos + 29 + (i * 35), 11, Font.BOLD, new Color(150, 150, 150), g);
			
			TextButton button = (TextButton) components.get(i);
			if(button.getId() == -1) continue;
			button.setPos(x + 180, y + yPos + 8 + (i * 35));
			button.render(g);
		}
		
		for(Component c : components) c.render(g);
	}
	
}
