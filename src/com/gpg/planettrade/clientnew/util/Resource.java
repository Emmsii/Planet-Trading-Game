package com.gpg.planettrade.clientnew.util;

public class Resource {

	private final String resourcePath;

	public Resource(String fileName)
	{
		this.resourcePath = "res/assets/" + fileName;
	}

	public String getResourcePath()
	{
		return resourcePath;
	}

}
