package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.menu.PlanetMenu;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.storage.Storage;

public class BuildStoragePopup extends Popup{
	
	public BuildStoragePopup(int x, int y, Mouse mouse, Keyboard key, Menu menu) {
		super(x, y, mouse, key, menu);
		
		components.add(new TextButton(0, 0, 50, 20, 0, "Build"));
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
					//Make a new storage depo.
					Storage storage = new Storage();
					storage.maxStorage = 25000;
					storage.price = 2500;
					((PlanetMenu) menu).addStorage(storage);
					closed = true;
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x,  y, 400, 200);
		g.setColor(Color.WHITE);
		g.drawRect(x,  y, 400, 200);
				
		Text.render("New Storage Depo", x + 20, y + 25, 18, Font.BOLD, g);
		
		int xPos = 40;
		int yPos = 50;
		
		for(int i = 0; i < 1; i++){
			g.drawRect(x + xPos - 10, y + yPos + (i * 50), 330, 50);
			Text.render("Small Storage Depo", x + xPos, y + yPos + 18 + (i * 50), 12, Font.BOLD, g);
			Text.render("Can store any materials.", x + xPos, y + yPos + 31 + (i * 50), 11, Font.PLAIN, new Color(150, 150, 150), g);
			Text.render("Cost:", x + xPos, y + yPos + 43 + (i * 50), 11, Font.PLAIN, new Color(150, 150, 150), g);
			Text.render(Globals.toCredits(2500), x + xPos + 28, y + yPos + 43 + (i * 50), 12, Font.BOLD, new Color(81, 151, 201), g);
			
			TextButton button = (TextButton) components.get(i);
			button.setPos(x + 290, y + yPos + 14 + (i * 50));
			button.render(g);
		}
		
		//May be rendering X button twice...
		for(Component c : components) c.render(g);
	}

}
