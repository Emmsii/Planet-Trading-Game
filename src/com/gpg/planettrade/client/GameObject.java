package com.gpg.planettrade.client;

public abstract class GameObject {

	// Vars
	protected float x, y;

	// Functions
	abstract void update();

	abstract void render();

	// Getters
	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

}
