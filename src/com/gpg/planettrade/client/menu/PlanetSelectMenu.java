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
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.planet.Planet;

public class PlanetSelectMenu extends Menu{

	public List<Planet> ownedPlanets;
	
	public PlanetSelectMenu(Mouse mouse, Keyboard key, MainComponent main) {
		super(mouse, key, main);
		init();
	}
	
	private void init(){
		components = new ArrayList<Component>();
	}

	@Override
	public void update() {
		//If the current ownedPlanets == null, try and get them.
		if(ownedPlanets == null){
			if(Globals.ownedPlanets != null) updateOwnedPlanets(Globals.ownedPlanets);
		}
		
		for(Component c : components){
			Button button = (Button) c;
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			
			if(button.isPressed()){
				//Set the selected planet of the PlanetMenu to whatever the button id was.
				//TODO: Do me later. wink wink.
				Globals.currentPlanet = ownedPlanets.get(button.getId());
				main.switchState(2);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		int xPos = 30;
		int yPos = 110;
		
		if(ownedPlanets== null){
			Text.render("No planets found.", 50, 100, 19, Font.BOLD, g);
			return;
		}
		
		for(int i = 0; i < ownedPlanets.size(); i++){
			Planet p = ownedPlanets.get(i);
			g.drawRect(xPos, yPos + (i * 45), 610, 40);
			
			Text.render(p.name, xPos + 5, yPos + 14 + (i * 45), 12, Font.BOLD, g);
			Text.render(p.subname, xPos + 5, yPos + 24 + (i * 45), 10, Font.BOLD, new Color(150, 150, 150), g);
			Text.render("Worth", xPos + 5, yPos + 36 + (i * 45), 10, Font.BOLD, g);
			Text.render(Globals.toCredits(p.getWorth()), xPos + 37, yPos + 36 + (i * 45), 10, Font.BOLD, new Color(81, 151, 201), g);
			
			g.drawOval(xPos + 192, yPos + 9 + (i * 45), 23, 23);
			if(p.type == 0){
				g.setColor(Color.GREEN);
				Text.render("T", xPos + 201, yPos + 26 + (i * 45), 12, Font.BOLD, new Color(145, 214, 88), g);
			}else if(p.type == 1){
				g.setColor(Color.BLUE);
				Text.render("W", xPos + 198, yPos + 26 + (i * 45), 12, Font.BOLD, new Color(88, 157, 214), g);
			}else if(p.type == 2){
				g.setColor(Color.ORANGE);
				Text.render("L", xPos + 201, yPos + 25 + (i * 45), 12, Font.BOLD, new Color(214, 119, 88), g);
			}else if(p.type == 3){
				g.setColor(Color.YELLOW);
				Text.render("D", xPos + 201, yPos + 26 + (i * 45), 12, Font.BOLD, new Color(219, 208, 118), g);
			}else if(p.type == 4){
				g.setColor(Color.PINK);
				Text.render("G", xPos + 200, yPos + 25 + (i * 45), 12, Font.BOLD, new Color(153, 84, 209), g);
			}
			
			Text.render("Size Class", xPos + 225, yPos + 24 + (i * 45), 10, Font.PLAIN, g);
			Text.render("- " + p.size, xPos + 280, yPos + 24 + (i * 45), 10, Font.BOLD, new Color(150, 150, 150), g);
			
			Text.render("Factories", xPos + 300, yPos + 18 + (i * 45), 10, Font.PLAIN, g);
			Text.render("- " + p.factories.size(), xPos + 359, yPos + 18 + (i * 45), 10, Font.BOLD, new Color(150, 150, 150), g);
			Text.render("Resources", xPos + 300, yPos + 31 + (i * 45), 10, Font.PLAIN, g);
			Text.render("- " + p.resources.size(), xPos + 359, yPos + 31 + (i * 45), 10, Font.BOLD, new Color(150, 150, 150), g);
			
			TextButton button = (TextButton) components.get(i);
			button.setPos(xPos + 538, yPos + 11 + (i * 45));
//			button.render(xPos + 538, yPos + 11 + (i * 45));
			button.render(g);
		}
	}

	public void updateOwnedPlanets(List<Planet> newPlanets){
		//TODO: FIX ME!
		if(newPlanets == null){
			Log.warn("Trying to send a list of null planets. [PLANET SELECT MENU]");
			return;
		}
		
		ownedPlanets = newPlanets;
		for(int i = 0; i < ownedPlanets.size(); i++) components.add(new TextButton(58, 20, i, "Select"));
	}
	

}
