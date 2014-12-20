package com.gpg.planettrade.clientbutts;

import com.gpg.planettrade.clientbutts.gui.FontRenderer;
import com.gpg.planettrade.clientbutts.states.StateInterface;
import com.gpg.planettrade.clientbutts.states.States;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import org.lwjgl.opengl.GL11;

public class Client {

	private final String TITLE = "Planet Trading Game";
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;

	public StateInterface activeState = States.CONNECTION_STATE.getState();

	public FontRenderer font;

	private void init()
	{
		font = new FontRenderer();
	}

	private void update()
	{
		activeState.update(activeState);
	}

	private void render()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.5f, 1.0f, 1.0f, 1f);
			GL11.glVertex2i(100, 100);
			GL11.glVertex2i(200, 100);
			GL11.glVertex2i(100, 200);
			GL11.glVertex2i(200, 200);
		GL11.glEnd();

		activeState.render();
	}

	private void destroy()
	{
		Display.destroy();
	}

	public static void main(String[] args)
	{
		Client client = new Client();
		client.start();
	}

	public void start()
	{

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle(TITLE);
			Display.setResizable(false);
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(8, 8, 0, 8)); // Anti-aliasing ([Alpha bits], [Depth bits], [Stencil bits], [Samples]) (8xAA)
			Keyboard.create();
			Mouse.create();
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		initGL();
		init();

		while (!Display.isCloseRequested())
		{
			render();
			update();

			Display.update();
			Display.sync(144);
		}

		destroy();
	}

	private void initGL()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glClearDepth(1);

//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select Projection mode as current matrix
		GL11.glLoadIdentity(); // Clear current matrix
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1); // Set to orthographic view, covering the screen, in 2D (rendering anything from z-1 to z1)
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Return to model view matrix
	}

}
