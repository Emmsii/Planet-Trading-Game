package com.gpg.planettrade.client.gui;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class TextRenderer {

	private Font fontAFont;

	public TextRenderer()
	{
		// Load font A from file
		try
		{
			InputStream fontAInputStream = ResourceLoader.getResourceAsStream("res/assets/fonts/kenvector_future.ttf");
			fontAFont = Font.createFont(Font.TRUETYPE_FONT, fontAInputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public TrueTypeFont getFontA(float fontSize)
	{
		fontAFont = fontAFont.deriveFont(fontSize);
		return new TrueTypeFont(fontAFont, true);
	}

}
