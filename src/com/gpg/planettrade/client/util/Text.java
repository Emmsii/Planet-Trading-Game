package com.gpg.planettrade.client.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.esotericsoftware.minlog.Log;

public class Text {

	private static Font font;
	private static 	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	
	public static void init(){
		Log.info("Init font");
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/assets/fonts/kenvector_future.ttf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ge.registerFont(font);
	
	}
	
	public static void render(String text, int x, int y, int size, int type, Graphics g){
		if(text == null || text.equalsIgnoreCase("")) text = "null";
//		g.setFont(new Font("Arial", type, size));
		g.setFont(font.deriveFont((float)(size)));
		g.drawString(text, x, y);
	}
	
	public static void render(String text, int x, int y, int size, int type, Color color, Graphics g){
		g.setColor(color);
		if(text == null || text.equalsIgnoreCase("")) text = "null";
//		g.setFont(new Font("Arial", type, size));
		g.setFont(font.deriveFont((float)(size)));
		g.drawString(text, x, y);
		g.setColor(Color.WHITE);
	}
}
