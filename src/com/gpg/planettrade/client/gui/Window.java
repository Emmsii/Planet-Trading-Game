package com.gpg.planettrade.client.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

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

	public void start(GUI gui)
	{
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setTitle(title);
			Display.create(new PixelFormat(8, 8, 0, 8)); // Anti-aliasing ([Alpha bits], [Depth bits], [Stencil bits], [Samples]) (8xAA)
			windowOpen = true;
		}
		catch (LWJGLException e)
		{
			windowOpen = false;
			e.printStackTrace();
			System.exit(0);
		}

		initGL();

		while (!Display.isCloseRequested())
		{
			windowOpen = true;

			glClear(GL_COLOR_BUFFER_BIT);

			gui.render();

			Display.update();
		}

		windowOpen = false;
		Display.destroy();
	}

	private static void initGL()
	{
		glMatrixMode(GL_PROJECTION); // Select Projection mode as current matrix
		glLoadIdentity(); // Clear current matrix
		glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1); // Set to orthographic view, covering the screen, in 2D (rendering anything from z-1 to z1)
		glMatrixMode(GL_MODELVIEW); // Return to model view matrix

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set clear colour to black

		glDisable(GL_DEPTH_TEST); // Disable the depth test because we're in 2D
	}

	public boolean isOpen()
	{
		return windowOpen;
	}

}
