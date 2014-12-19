package com.gpg.planettrade.clientnew;

import com.gpg.planettrade.clientnew.gui.FontRenderer;
import com.gpg.planettrade.clientnew.states.ConnectionState;
import com.gpg.planettrade.clientnew.states.MainMenuState;
import com.gpg.planettrade.clientnew.states.StateInterface;
import com.gpg.planettrade.clientnew.states.States;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

public class Client {

	private static final Logger log = Logger.getLogger(Client.class.getName());

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
		activeState.update();
	}

	private void render()
	{
		glClear(GL_COLOR_BUFFER_BIT);

		font.headerFont(24f).drawString(32, 32, "Hello world!");
		font.bodyFont(14f).drawString(32, 64, "This is the body text!");

		activeState.render(font);
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
			log.log(Level.FINE, "LWJGL Version: " + Sys.getVersion());
			Display.setResizable(false);
			Display.setVSyncEnabled(true);
			Display.create(new PixelFormat(8, 8, 0, 8)); // Anti-aliasing ([Alpha bits], [Depth bits], [Stencil bits], [Samples]) (8xAA)
			Keyboard.create();
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
			update();
			render();

			Display.update();
			Display.sync(144);
		}

		destroy();
	}

	private void initGL()
	{
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClearDepth(1);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glViewport(0, 0, WIDTH, HEIGHT);
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION); // Select Projection mode as current matrix
		glLoadIdentity(); // Clear current matrix
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1); // Set to orthographic view, covering the screen, in 2D (rendering anything from z-1 to z1)
		glMatrixMode(GL_MODELVIEW); // Return to model view matrix
	}

}
