package com.gpg.planettrade.client.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.component.Button;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextButton;
import com.gpg.planettrade.client.menu.popup.BuildFactoryPopup;
import com.gpg.planettrade.client.menu.popup.BuildStoragePopup;
import com.gpg.planettrade.client.menu.popup.Popup;
import com.gpg.planettrade.client.menu.popup.StoragePopup;
import com.gpg.planettrade.client.util.GameTime;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network.TakeCredits;
import com.gpg.planettrade.core.Network.UpdatePlanet;
import com.gpg.planettrade.core.TradeOffer;
import com.gpg.planettrade.core.planet.factory.Factory;
import com.gpg.planettrade.core.planet.resource.Resource;
import com.gpg.planettrade.core.planet.storage.Container;
import com.gpg.planettrade.core.planet.storage.Storage;

public class PlanetMenu extends Menu{

	public Popup popup = null;
	
	public PlanetMenu(Mouse mouse, Keyboard key, MainComponent main) {
		super(mouse, key, main);
		components = new ArrayList<Component> ();
		components.add(new TextButton(10, 675, 50, 20, 0, "Back"));
		components.add(new TextButton(425, 164, 94, 20, 1, "Build Factory"));
		components.add(new TextButton(815, 164, 94, 20, 2, "Build Storage"));
		
		for(int i = 0; i < Globals.currentPlanet.storage.size(); i++){
			components.add(new TextButton(870, 203 + (i * 35), 50, 20, 100 + i, "Open"));
		}
	}

	@Override
	public void update() {
		for(int i = components.size() - 1; i >= 0; i--){
			Button button = (Button) components.get(i);
			button.setMouse(mouse.getX(), mouse.getY(), mouse.getButton());
			button.update();
			
			if(popup == null){
				if(button.isPressed()){
					if(button.getId() == 0){
						//Back button
						
						//MAYBE UPDATE PLANET INFO?
						
						main.switchState(1);
					}
					
					if(button.getId() == 1){
						//Pressed BUILD FACTORY BUTTON.
						if(popup == null) popup = new BuildFactoryPopup(410, 180, mouse, key, this);
					}
					
					if(button.getId() == 2){
						if(popup == null) popup = new BuildStoragePopup(410, 250, mouse, key, this);
					}
					
					if(button.getId() >= 100){
						if(Globals.currentPlanet.storage.get(button.getId() - 100) == null) Log.warn("Trying to get null storage depo: " + (button.getId() - 100));
						else if(popup == null) popup = new StoragePopup(410, 180, mouse, this, key, Globals.currentPlanet.storage.get(button.getId() - 100));
					}
	
				}
			}else{
				popup.update();
				if(popup.isClosed()) popup = null;
			}
		}
		
		processResources();
	}

	@Override
	public void render(Graphics g) {
		for(Component c : components) c.render(g);
		
		Text.render(Globals.currentPlanet.name, 20, 75, 30, Font.BOLD, g);
		Text.render(Globals.currentPlanet.subname, 20, 87, 10, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Size class - " + Globals.currentPlanet.size, 19, 97, 10, Font.BOLD, new Color(150, 150, 150), g);
		Text.render("Worth", 19, 118, 22, Font.BOLD, g);
		Text.render(Globals.toCredits(Globals.currentPlanet.getWorth()), 19, 132, 15, Font.BOLD, new Color(81, 151, 201), g);
		
		Text.render("Resources", 27, 180, 18, Font.BOLD, g);
		Text.render("Factories", 325, 180, 18, Font.BOLD, g);
		Text.render("Storage", 725, 180, 18, Font.BOLD, g);
		
		int xPos = 20;
		int yPos = 195;
		
		for(int i = 0; i < Globals.currentPlanet.resources.size(); i++){
			Resource r = Globals.currentPlanet.resources.get(i);
			g.drawRect(xPos, yPos + (i * 35), 250, 35);
			Text.render(r.name, xPos + 5, yPos + 17 + (i * 35), 13, Font.BOLD, g);
			Text.render(r.description, xPos + 5, yPos + 28 + (i * 35), 10, Font.BOLD, new Color(150, 150, 150), g);
			Text.render(NumberFormat.getIntegerInstance().format(r.amount) + " units", xPos + 100, yPos + 17 + (i * 35), 13, Font.BOLD, new Color(150, 150, 150), g);
		}
		
		xPos = 310;
				
		for(int i = 0; i < Globals.currentPlanet.factories.size(); i++){
			Factory f = Globals.currentPlanet.factories.get(i);
			g.drawRect(xPos, yPos + (i * 35), 350, 35);
			Text.render(f.name, xPos + 10, yPos + 15 + (i * 35), 12, Font.BOLD, g);
			Text.render(f.description, xPos + 10, yPos + 28 + (i * 35), 11, Font.PLAIN, new Color(150, 150, 150), g);
			Text.render("Processed", xPos + 150, yPos + 15 + (i * 35), 11, Font.BOLD, g);
			Text.render("- " + NumberFormat.getIntegerInstance().format(f.actuallyProcessed), xPos + 215, yPos + 15 + (i * 35), 11, Font.BOLD, new Color(150, 150, 150), g);
			Text.render(f.ups + "/ups", xPos + 149, yPos + 25 + (i * 35), 10, Font.PLAIN, new Color(150, 150, 150), g);
			Text.render(GameTime.getTimeString(f.secondsPassed), xPos + 200, yPos + 25 + (i * 35), 10, Font.BOLD, new Color(150, 150, 150), g);
		}
		
		xPos = 710;

		for(int i = 0; i < Globals.currentPlanet.storage.size(); i++){
			Storage s = Globals.currentPlanet.storage.get(i);
			g.drawRect(xPos, yPos + (i * 35), 350, 35);
			Text.render("Small Storage Depo", xPos + 10, yPos + 15 + (i * 35), 12, Font.BOLD, g);
			Text.render(NumberFormat.getIntegerInstance().format(s.getStored()) + "/" + NumberFormat.getIntegerInstance().format(s.maxStorage), xPos + 10, yPos + 28 + (i * 35), 12, Font.BOLD, new Color(150, 150, 150), g);
		}
		
		if(popup != null) popup.render(g);
	}
	
	private void processResources(){
		if(Globals.currentPlanet.storage == null || Globals.currentPlanet.storage.size() == 0) return;
		
		for(Factory f : Globals.currentPlanet.factories){
			//Add materials that need to br processed.
			if(f.actuallyProcessed < f.shouldHaveBeenProcessed){
				long difference = f.shouldHaveBeenProcessed - f.actuallyProcessed;
				int resourcesTaken = Globals.currentPlanet.takeResource((int) difference, f.input);
				f.actuallyProcessed += resourcesTaken;
			}	
			
			//Process resources every second.
			if(GameTime.time % 60 == 0){
				f.secondsPassed = GameTime.currentTimeSeconds - f.startTime;
				if(!Globals.currentPlanet.isStorageFull()){
					f.shouldHaveBeenProcessed = f.ups * f.secondsPassed;
					int resourcesTaken = Globals.currentPlanet.takeResource(f.ups, f.input);
					f.actuallyProcessed += resourcesTaken;
				}
			}
		}
	}
	
	
	
	public void addStorage(Storage s){
		if(s == null){
			Log.warn("Cannot create a null storage depo.");
			return;
		}
		
		if(Globals.storedCredits >= s.price){
			components.add(new TextButton(870, 203 + (Globals.currentPlanet.storage.size() * 35), 50, 20, 100 + Globals.currentPlanet.storage.size(), "Open"));
			Globals.currentPlanet.addStorage(s);
			
			TakeCredits takeCredits = new TakeCredits();
			takeCredits.amount = s.price;
			main.gameClient.client.sendTCP(takeCredits);
			
			UpdatePlanet updatePlanet = new UpdatePlanet();
			updatePlanet.planet = Globals.currentPlanet;
			main.gameClient.client.sendTCP(updatePlanet);
			Log.info("Creating new storage depo on planet " + Globals.currentPlanet.name);		
		}else{
			Log.info("Player cannot afford to build another storage depo.");
		}
	}
	
	public void addFactory(Factory f){
		if(f == null){
			Log.warn("Cannot create a null factory.");
			return;
		}
		
		if(Globals.currentPlanet.storage == null || Globals.currentPlanet.storage.size() == 0){
			JOptionPane.showMessageDialog(null, "You should build some storage depo's first.", "", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		//If player can afford to build factory.
		if(Globals.storedCredits >= f.price){
			f.startTime = GameTime.currentTimeSeconds;
			Globals.currentPlanet.addFactory(f);
			
			TakeCredits takeCredits = new TakeCredits();
			takeCredits.amount = f.price;
			main.gameClient.client.sendTCP(takeCredits);
			
			UpdatePlanet updatePlanet = new UpdatePlanet();
			updatePlanet.planet = Globals.currentPlanet;
			main.gameClient.client.sendTCP(updatePlanet);
			
			Log.info("Creating new factory on planet " + Globals.currentPlanet.name);
		}else{
			Log.info("Player cannot afford to build another factory.");
		}
	}
	
	public void newTradeOffer(TradeOffer offer, Storage storage){
		if(offer == null){
			Log.warn("Cannot create null trade offer.");
			return;
		}

		for(Container c : storage.containers){
			if(c.type.type.equalsIgnoreCase(offer.type)){
				Log.info("Removing Resources");
				Log.info("Taking " + offer.quantity + " from " + c.amount);
				Log.info("Trade container: " + offer.type + " from " + c.type.type);
				c.amount -= offer.quantity;
				break;
			}
		}
		main.gameClient.client.sendTCP(offer);
	}

}

