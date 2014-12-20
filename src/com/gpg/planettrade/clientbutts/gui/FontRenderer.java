package com.gpg.planettrade.clientbutts.gui;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class FontRenderer {

	private Font headerFont;
	private Font bodyFont;

	public FontRenderer()
	{
		// Load header font from file
		try
		{
			InputStream headerFontInputStream = ResourceLoader.getResourceAsStream("res/assets/fonts/kenvector_future.ttf");
			headerFont = Font.createFont(Font.TRUETYPE_FONT, headerFontInputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Load body font from file
		try
		{
			InputStream bodyFontInputStream = ResourceLoader.getResourceAsStream("res/assets/fonts/Laconic_Regular.otf");
			bodyFont = Font.createFont(Font.TRUETYPE_FONT, bodyFontInputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public TrueTypeFont headerFont(float fontSize)
	{
		headerFont = headerFont.deriveFont(fontSize);
		return new TrueTypeFont(headerFont, true);
	}

	public TrueTypeFont bodyFont(float fontSize)
	{
		bodyFont = bodyFont.deriveFont(fontSize);
		return new TrueTypeFont(bodyFont, true);
	}

}