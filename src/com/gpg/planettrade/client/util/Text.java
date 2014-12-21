package com.gpg.planettrade.client.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Text {
	
	public static void render(String text, int x, int y, int size, int type, Graphics g){
		if(text == null || text.equalsIgnoreCase("")) text = "null";
		g.setFont(new Font("Arial", type, size));
		g.drawString(text, x, y);
	}
	
	public static void render(String text, int x, int y, int size, int type, Color color, Graphics g){
		g.setColor(color);
		if(text == null || text.equalsIgnoreCase("")) text = "null";
		g.setFont(new Font("Arial", type, size));
		g.drawString(text, x, y);
		g.setColor(Color.WHITE);
	}
}
