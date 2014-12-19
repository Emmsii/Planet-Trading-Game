package com.gpg.planettrade.clientnew.gui;

import org.lwjgl.opengl.GL11;

public class Button extends GUIElement {

	protected int x, y;
	protected String label;
	protected boolean isHovered = false;
	protected boolean isPressed = false;

	public Button(int x, int y, String label)
	{
		this.x = x;
		this.y = y;
		this.label = label;
	}

	public void update()
	{
		//
	}

	public void render()
	{
		int labelWidth = font.bodyFont(14f).getWidth(label);

		GL11.glColor3f(0, 0, 1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(x, y);
			GL11.glVertex2i(x + labelWidth, y);
			GL11.glVertex2i(x, y + 20);
			GL11.glVertex2i(x + labelWidth, y + 20);
		GL11.glEnd();

		font.bodyFont(14f).drawString(x + 10, y + 10, label);
	}

}
