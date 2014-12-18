package com.gpg.planettrade.client;

import com.gpg.planettrade.client.gui.*;

import java.util.ArrayList;

public class Test implements GUI {

	/** The Game objects **/
	private ArrayList<GameObject> gameObjects;

	/** Font Renderer **/
	public FontRenderer font;

	public static void main(String[] args)
	{
		Window mainWindow = new Window(1280, 720, "Planet Trading Game");
		mainWindow.start(new Test());
	}

	public void initLoop()
	{
		gameObjects = new ArrayList<GameObject>();
		font = new FontRenderer();
	}

	public void getInput()
	{
		//
	}

	public void update()
	{
		for (GameObject go : gameObjects)
			go.update();
	}

	public void render()
	{
		for (GameObject go : gameObjects)
			go.render();

		font.headerFont(24f).drawString(32, 32, "Hello world!");
		font.bodyFont(14f).drawString(32, 64, "This is the body text!");
	}

}