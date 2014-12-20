package com.gpg.planettrade.clientbutts.gui;

import org.lwjgl.opengl.GL11;

public class Button extends GUIElement {

	protected String label;
	protected int labelWidth;
	protected boolean isHovered = false;
	protected boolean isPressed = false;

	public Button(int x, int y, String label)
	{
		this.x = x;
		this.y = y;
		this.label = label;
	}

	@Override
	public void update()
	{
//		if ((mouseX > this.x) && (mouseX < this.x) && (mouseY > this.y) && (mouseY < this.y)) this.isHovered = true; System.out.println("Hovered");
//		if (this.isHovered && leftButtonDown) this.isPressed = true; System.out.println("Pressed");
	}

	@Override
	public void render()
	{
		this.labelWidth = font.bodyFont(14f).getWidth(label);

		GL11.glColor4f(0, 0, 1.0f, 1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2i(x, y);
			GL11.glVertex2i(x + this.labelWidth, y);
			GL11.glVertex2i(x, y + 20);
			GL11.glVertex2i(x + this.labelWidth, y + 20);
		GL11.glEnd();

		font.bodyFont(14f).drawString(x + 10, y + 10, label);
	}

}
