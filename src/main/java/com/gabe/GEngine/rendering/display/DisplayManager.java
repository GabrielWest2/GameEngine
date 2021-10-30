package com.gabe.GEngine.rendering.display;

import com.gabe.GEngine.GameLogic;
import com.gabe.GEngine.utilities.listener.Keyboard;
import com.gabe.GEngine.utilities.listener.Mouse;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

	// The window handle
	private static long window;

	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;
	private static final int FPS_CAP = 120;
	private static int width = WIDTH, height = HEIGHT;
	private static Vector3f clearColor = new Vector3f(1, 0, 0);

	// For mouse tracking
	private static final double[] mousePosX = new double[1];
	private static final double[] mousePosY = new double[1];



	private static String glslVersion = null; // We can initialize our renderer with different versions of the GLSL


	public static void start(){

		init();
		GameLogic.Init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free();

	}

	private static void init(){

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_DECORATED, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "GabeEngine", GLFW.glfwGetPrimaryMonitor(), NULL);
		//window = glfwCreateWindow(WIDTH, HEIGHT, "GabeEngine", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");


		glfwSetWindowSizeCallback(window,  (long window, int winWidth, int winHeight)-> {
			width = winWidth;
			height = winHeight;
			if(GameLogic.isPrepared()) {
				GameLogic.getRenderer().updateProjectionMatrix();

				//glViewport(0, 0, width, height);
				glViewport(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
				//Framebuffer.ResizeFrameBuffer();
			}
			System.out.println("Window resized");
		});

		glfwSetKeyCallback(window, Keyboard::keyCallback);
		glfwSetMouseButtonCallback(window, Mouse::mouseButtonCallback);
		glfwSetCursorPosCallback(window, Mouse::cursorPosCallback);

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			assert vidmode != null;
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		//Load icon using stb
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);

			ByteBuffer icon = stbi_load("assets/images/icon.png", w, h, comp, 4);

			glfwSetWindowIcon(window, GLFWImage.mallocStack(1, stack)
					.width(w.get(0))
					.height(h.get(0))
					.pixels(icon)
			);

			stbi_image_free(icon);
		}
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		GL.createCapabilities();
		setClearColor(131/255f, 178/255f, 252/255f);
	}

	public static void clearColor(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private static void loop(){

		//clearColor();
		while ( !glfwWindowShouldClose(window) ) {
			GameLogic.Update();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		GameLogic.Exit();
	}

	public static int getWidth(){
		return width;
	}

	public static int getHeight(){
		return height;
	}

	public static void setClearColor(Vector3f rgb){
		clearColor = rgb;
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 1f);
	}

	public static void setClearColor(float r, float g, float b){
		clearColor = new Vector3f(r, g, b);
		glClearColor(clearColor.x, clearColor.y, clearColor.z, 1f);
	}

	public static long getWindow() {
		return window;
	}

	public static float getAspectRatio(){
		return 16.0f/9.0f;
	}

	private void decideGlGlslVersions() {
		final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
		if (isMac) {
			glslVersion = "#version 150";
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);            // Required on Mac
		} else {
			glslVersion = "#version 130";
			glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
			glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
		}
	}
}
