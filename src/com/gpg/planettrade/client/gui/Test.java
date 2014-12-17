package com.gpg.planettrade.client.gui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Test implements GUI {

	public static void main(String[] args)
	{
		Window mainWindow = new Window(1280, 720, "Planet Trading Game");
		mainWindow.start(new Test());
	}

	public void render()
	{
		glLoadIdentity();

		glColor3f(0.25f, 0.75f, 0.5f);

		glTranslatef(64, 64, 0);
		glRotatef(45,0,0,1);

		glBegin(GL_QUADS);
		{
			glVertex2f(0,0);
			glVertex2f(0,64);
			glVertex2f(64,64);
			glVertex2f(64,0);
		}
		glEnd();
	}

}
