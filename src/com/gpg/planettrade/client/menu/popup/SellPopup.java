package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.storage.Container;

public class SellPopup extends Popup{

	private Container container;
	
	public SellPopup(int x, int y, Mouse mouse, Menu menu) {
		super(x, y, mouse, menu);
		
		components.add(new TextButton(x + 370, y, 30, 20, -1, "X"));
		
	}
	
	public void init(Container c){
		//Sell a specific container
		container = c;
	}

	@Override
	public void update() {
		for(Component c : components){
			Button button = (Button) c;
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			
			if(button.isPressed()){
				if(button.getId() == -1) closed = true;
				
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x,  y, 400, 430);
		g.setColor(Color.WHITE);
		g.drawRect(x,  y, 400, 430);
		
		Text.render("Sell Goods", x + 20, y + 25, 18, Font.BOLD, g);
		
		Text.render(container.type.name, x + 20, y + 50, 15, Font.BOLD, g);
		Text.render(Globals.formatInt(container.amount) + " units", x + 20, y + 65, 15, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Can sell for:", x + 20, y + 90, 12, Font.BOLD, g);
		Text.render(container.getWorth(), x + 20, y + 113, 25, Font.BOLD, new Color(81, 151, 201), g);
		Text.render(Globals.toCredits(container.type.value) + " each", x + 20, y + 127, 12, Font.BOLD, new Color(81, 151, 201), g);
		
		for(Component c : components) c.render(g);
	}

}
