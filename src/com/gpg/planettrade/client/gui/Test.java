/**
 * https://www.youtube.com/watch?v=QCetOCrRxFQ&index=4&list=PL513808FE7D9A5D68
 * http://forum.lwjgl.org/index.php?topic=2951.0
 */

package com.gpg.planettrade.client.gui;

import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Test implements GUI {

	private TextRenderer text;

	public static void main(String[] args)
	{
		Window mainWindow = new Window(1280, 720, "Planet Trading Game");
		mainWindow.start(new Test());
	}

	public void initLoop()
	{
		text = new TextRenderer();
	}

	public void update()
	{
		//
	}

	public void render()
	{
		text.getFontA(32f).drawString(32, 32, "Hello world!", Color.cyan);

//		glColor3f(0.25f, 0.75f, 0.5f);
//		drawRect(64, 64, 64, 256);
//
//		glColor3f(0.25f, 0.5f, 0.5f);
//		drawRect(64, 64, 256, 64, 24);
//
//		glColor3f(0.5f, 0.75f, 0.25f);
//		drawRectFromCenter(256, 325, 64, 128, 50);
	}

	private void drawRect(float x, float y, float width, float height)
	{
		drawRect(x, y, width, height, 0);
	}

	private void drawRect(float x, float y, float width, float height, float rotate)
	{
		glPushMatrix();
		{
			glTranslatef(x, y, 0);
			glRotatef(rotate, 0, 0, 1);

			glBegin(GL_QUADS);
			{
				glVertex2f(0, 0);
				glVertex2f(0, height);
				glVertex2f(width, height);
				glVertex2f(width, 0);
			}
			glEnd();
		}
		glPopMatrix();
	}

	private void drawRectFromCenter(float x, float y, float width, float height, float rotate)
	{
		glPushMatrix();
		{
			width /= 2;
			height /= 2;

			glTranslatef(x, y, 0);
			glRotatef(rotate, 0, 0, 1);

			glBegin(GL_QUADS);
			{
				glVertex2f(-width, -height);
				glVertex2f(-width, height);
				glVertex2f(width, height);
				glVertex2f(width, -height);
			}
			glEnd();
		}
		glPopMatrix();
	}

}