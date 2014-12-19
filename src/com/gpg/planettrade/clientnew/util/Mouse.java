package com.gpg.planettrade.clientnew.util;

import org.lwjgl.LWJGLException;

public class Mouse {

	public int mouseX;
	public int mouseY;

	public boolean leftButtonDown;
	public boolean rightButtonDown;

	public Mouse()
	{
		try
		{
			org.lwjgl.input.Mouse.create();

			mouseX = org.lwjgl.input.Mouse.getX();
			mouseY = org.lwjgl.input.Mouse.getY();

			leftButtonDown = org.lwjgl.input.Mouse.isButtonDown(0);
			rightButtonDown = org.lwjgl.input.Mouse.isButtonDown(1);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

}
