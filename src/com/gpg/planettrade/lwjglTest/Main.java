package com.gpg.planettrade.lwjglTest;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

	// Callback instances
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	// Window handle
	private long window;

	public void run()
	{
		try
		{
			init();
			loop();

			// Release window & window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		}
		finally
		{
			// Terminate GLFW and release the GLFWerrorfun
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void init()
	{
		// Error Callback (prints to System.err by default)
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		// Init GLFW
		if (glfwInit() != GL11.GL_TRUE) throw new IllegalStateException("Unable to init GLFW. Shit.");

		// Configure window!
		glfwDefaultWindowHints(); // "optional, the current window hints are already the default"
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Hide the window on initial creation
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // Not resizeable

		// Create the window
		int HEIGHT = 720;
		int WIDTH = 1280;
		String TITLE = "Planet Trading Game";

		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// Key Callback (called everytime a key is pressed, repeated, or released)
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, GL_TRUE); // Detected in rendering loop
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center the window
		glfwSetWindowPos(
				window,
				(GLFWvidmode.width(vidmode)-  WIDTH) / 2,
				(GLFWvidmode.height(vidmode)-  HEIGHT) / 2
		);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);

		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible!
		glfwShowWindow(window);
	}

	private void loop()
	{
		// Make GLFW's OpenGL context available for use by LWJGL
		GLContext.createFromCurrent();

		// Set the clear colour (Black, in this case)
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Run the render loop until the window is closed (or escape is pressed, see above)
		while (glfwWindowShouldClose(window) == GL_FALSE)
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the frame buffer

			glfwSwapBuffers(window); // Swap the colour buffers

			// Poll for window events. The key callback (above) will only be invoked during this call
			glfwPollEvents();
		}
	}

	public static void main(String[] args)
	{
		new Main().run();
	}

}
