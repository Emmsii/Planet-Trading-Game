package com.gpg.planettrade.client.menu.popup;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.esotericsoftware.minlog.Log;
import com.gpg.planettrade.client.MainComponent;
import com.gpg.planettrade.client.component.Component;
import com.gpg.planettrade.client.component.TextField;
import com.gpg.planettrade.client.menu.Menu;
import com.gpg.planettrade.client.util.Keyboard;
import com.gpg.planettrade.client.util.Mouse;
import com.gpg.planettrade.client.util.Text;
import com.gpg.planettrade.core.Globals;
import com.gpg.planettrade.core.Network.ChatMessage;

public class ChatPopup extends Popup{
	
	private MainComponent main;
	
	private String[] messages = new String[19];
	
	boolean enter = false;
	
	public ChatPopup(int x, int y, Mouse mouse, Keyboard key, Menu menu, MainComponent main) {
		super(x, y, mouse, key, menu);
		this.main = main;
		components.add(new TextField(x, y + 280, 40, TextField.BOTH, key));
	} 
	
	@Override
	public void update() {
		if(key.enter.pressed) enter = true;
		for(Component c : components){
			c.update();
			
			if(c instanceof TextField){
				TextField field = (TextField) c;
				
				if(enter){
					if(field.getText().trim().length() > 0){
						//Send message to server
						ChatMessage msg = new ChatMessage();
						msg.from = Globals.username;
						msg.text = field.getText();
						main.gameClient.client.sendTCP(msg);
						field.clear();
					}
					enter = false;
				}
				
				
			}
		}
		key.release();
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(x,  y, 300, 300);
		g.setColor(Color.WHITE);
		g.drawRect(x,  y, 300, 300);
		
		Text.render("Global Chat", x + 3, y + 11, 11, Font.BOLD, g);
		
		for(int i = 0; i < messages.length; i++){
			if(messages[i] != null) Text.render(messages[i], x + 5, y + 23 + (i * 14), 12, Font.BOLD, g);
		}
		
		for(Component c : components) c.render(g);
	}
	
	public void addMessage(String text){
		
		
//		public void addLog(String a){
//			for(int i = (3 - 1); i >= 0; i--) log[i + 1] = log[i];
//			log[0] = a;
//		}
		
		for(int i = messages.length - 2; i >= 0; i--) messages[i + 1] = messages[i];
		messages[0] = text;
		
//		for(int i = 0; i < messages.length; i++){
//			if(messages[i] == null){
//				messages[i] = text;
//				return;
//			}
//			//Messages are full, shift everything over one make messages[0] top
//		}
	}

}
