/**
 * Set VM options to -Djava.library.path=lib/native/windows
 */

package com.gpg.planettrade.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class lwjglTest {

	private static final String TITLE = "Planet Trading Game";
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	public void start()
	{
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(TITLE);
			Display.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		while (!Display.isCloseRequested())
		{
			// Render OpenGL Here!
			Display.update();
		}

		Display.destroy();
	}

	public static void main(String[] args)
	{
		lwjglTest lwjglTest = new lwjglTest();
		lwjglTest.start();
	}

}
