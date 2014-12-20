package com.gpg.planettrade.clientnew.gui;

import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class FontRenderer {

	private TrueTypeFont headerFont;

	public FontRenderer() {

		try
		{
			BufferedInputStream headerFontFile = new BufferedInputStream(FontRenderer.class.getResourceAsStream("assets/fonts/kenvector_future.ttf"));
			Font startHeaderFont = Font.createFont(Font.TRUETYPE_FONT, headerFontFile);
			Font baseHeaderFont = startHeaderFont.deriveFont(Font.PLAIN, 32);
			headerFont = new TrueTypeFont(baseHeaderFont, true);
		}
		catch (FontFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public TrueTypeFont headerFont()
	{
		return headerFont;
	}

}
