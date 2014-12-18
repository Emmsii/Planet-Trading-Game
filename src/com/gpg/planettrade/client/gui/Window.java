package com.gpg.planettrade.client.gui;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

public class Window {

	protected final int w, h;
	protected final String title;

	public Window(int w, int h, String title)
	{
		this.w = w;
		this.h = h;
		this.title = title;
	}

	public void start(GUI gui)
	{
		initGL();

		gui.initLoop();

		double delta = 1;
		double nextTime = (double) System.nanoTime() / 1000000000.0;
		double maxTimeDiff = 0.5;
		int skippedFrames = 1;
		int maxSkippedFrames = 5;

		while (!Display.isCloseRequested())
		{
			double currTime = (double) System.nanoTime() / 1000000000.0;
			if ((currTime - nextTime) > maxTimeDiff) nextTime = currTime;
			if (currTime >= nextTime)
			{
				nextTime += delta;
				gui.update();

				if ((currTime < nextTime) || (skippedFrames > maxSkippedFrames))
				{
					if (Display.isActive() && Display.isVisible())
					{
						glClear(GL_COLOR_BUFFER_BIT);
						glLoadIdentity();
						gui.render();
						skippedFrames = 1;
					}
				}
				else
				{
					skippedFrames++;
				}
			}
			else
			{
				int sleepTime = (int)(1000.0 * (nextTime - currTime));

				if (sleepTime > 0)
				{
					try
					{
						Thread.sleep(sleepTime);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}

			Display.update();
		}

		Display.destroy();
	}

	private void initGL()
	{
		try {
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.setTitle(title);
			Display.create(new PixelFormat(8, 8, 0, 8)); // Anti-aliasing ([Alpha bits], [Depth bits], [Stencil bits], [Samples]) (8xAA)
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glViewport(0, 0, w, h);
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION); // Select Projection mode as current matrix
		glLoadIdentity(); // Clear current matrix
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1); // Set to orthographic view, covering the screen, in 2D (rendering anything from z-1 to z1)
		glMatrixMode(GL_MODELVIEW); // Return to model view matrix

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set clear colour to black

		glDisable(GL_DEPTH_TEST); // Disable the depth test because we're in 2D
	}

}
