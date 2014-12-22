package com.gpg.planettrade.client.component;

import java.awt.Font;
import java.awt.Graphics;

import com.gpg.planettrade.client.util.Text;

public class TextButton extends Button{
	
	public TextButton(int x, int y, int w, int h, int id, String text) {
		super(x, y, w, h, id, text);
	}
	
	public TextButton(int w, int h, int id, String text){
		super(0, 0, w, h, id, text);
	}

	public void update(){
		if(mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h){
			hover = true;
			if(mouseB != -1) pressTime = waitTime;
		}else{
			hover = false;
		}
		
		if(pressTime > 0) pressTime--;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawRect(x, y, w, h);
		if(hover) g.drawRect(x + 2, y + 2, w - 4, h - 4);
		if(pressTime > 0){
			g.drawRect(x + 2, y + 2, w - 4, h - 4);
			g.drawRect(x + 3, y + 3, w - 6, h - 6);
		}
		Text.render(text, x + 12, y + 15, 12, Font.BOLD, g);
	}
	
	public void render(int xa, int ya, Graphics g){
		g.drawRect(xa, ya, w, h);
		if(hover) g.drawRect(xa + 2, ya + 2, w - 4, h - 4);
		if(pressTime > 0){
			g.drawRect(xa + 2, ya + 2, w - 4, h - 4);
			g.drawRect(xa + 3, ya + 3, w - 6, h - 6);
		}
		Text.render(text, xa + 12, ya + 15, 12, Font.BOLD, g);
	}
}
