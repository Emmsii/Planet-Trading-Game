package com.gpg.planettrade.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

	protected final int w, h;
	protected final String title;

	private boolean windowOpen = false;

	public Window(int w, int h, String title)
	{
		this.w = w;
		this.h = h;
		this.title = title;

	}

	public void start()
	{
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setTitle(title);
			Display.create();
			windowOpen = true;
		}
		catch (LWJGLException e)
		{
			windowOpen = false;
			e.printStackTrace();
			System.exit(0);
		}

		while (!Display.isCloseRequested())
		{
			windowOpen = true;
			Display.update();
		}

		windowOpen = false;
		Display.destroy();
	}

	public boolean isOpen()
	{
		return windowOpen;
	}

}
