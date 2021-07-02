package com.gabe.GEngine;

import com.gabe.GEngine.listener.Keyboard;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

	// The window handle
	private static long window;

	private static final int WIDTH = 856;
	private static final int HEIGHT = 482;
	private static final int FPS_CAP = 120;
	private static int width = WIDTH, height = HEIGHT;
	private static Vector3f clearColor = new Vector3f(1, 0, 0);

	// For mouse tracking
	private static final double[] mousePosX = new double[1];
	private static final double[] mousePosY = new double[1];

	// Mouse cursors provided by GLFW
	private static final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

	// LWJGL3 renderer (SHOULD be initialized)
	private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	private static String glslVersion = null; // We can initialize our renderer with different versions of the GLSL


	public static void start(){
		init();
		initImGui();
		MainGameLoop.Init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
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

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Editor", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");


		glfwSetWindowSizeCallback(window,  (long window, int winWidth, int winHeight)-> {
			width = winWidth;
			height = winHeight;
			if(MainGameLoop.isPrepared()) {
				MainGameLoop.getRenderer().updateProjectionMatrix();
				glViewport(0, 0, width, height);
			}
		});



		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					window,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		GL.createCapabilities();
	}

	public static void clearColor(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	private static void loop(){

		//clearColor();
		glEnable(GL_DEPTH_TEST);


		while ( !glfwWindowShouldClose(window) ) {
			MainGameLoop.Update();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		MainGameLoop.Exit();
		destroyImGui();
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

	private static void initImGui() {
		// IMPORTANT!!
		// This line is critical for Dear ImGui to work.
		ImGui.createContext();

		// ------------------------------------------------------------
		// Initialize ImGuiIO config
		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename("ui.ini"); // We don't want to save .ini file
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
		io.setBackendPlatformName("imgui_java_impl_glfw");

		// ------------------------------------------------------------
		// Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
		keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
		keyMap[ImGuiKey.End] = GLFW_KEY_END;
		keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
		keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
		keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
		keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = GLFW_KEY_A;
		keyMap[ImGuiKey.C] = GLFW_KEY_C;
		keyMap[ImGuiKey.V] = GLFW_KEY_V;
		keyMap[ImGuiKey.X] = GLFW_KEY_X;
		keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
		keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
		io.setKeyMap(keyMap);

		// ------------------------------------------------------------
		// Mouse cursors mapping
		mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

		// ------------------------------------------------------------
		// GLFW callbacks to handle user input

		glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				io.setKeysDown(key, true);
			} else if (action == GLFW_RELEASE) {
				io.setKeysDown(key, false);
			}

			io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
			io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
			io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
			io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
		});

		glfwSetCharCallback(window, (w, c) -> {
			if (c != GLFW_KEY_DELETE) {
				io.addInputCharacter(c);
			}
		});

		glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
			final boolean[] mouseDown = new boolean[5];

			mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
			mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
			mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
			mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
			mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

			io.setMouseDown(mouseDown);

			if (!io.getWantCaptureMouse() && mouseDown[1]) {
				ImGui.setWindowFocus(null);
			}
		});

		glfwSetScrollCallback(window, (w, xOffset, yOffset) -> {
			io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
			io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
		});

		io.setSetClipboardTextFn(new ImStrConsumer() {
			@Override
			public void accept(final String s) {
				glfwSetClipboardString(window, s);
			}
		});

		io.setGetClipboardTextFn(new ImStrSupplier() {
			@Override
			public String get() {
				final String clipboardString = glfwGetClipboardString(window);
				if (clipboardString != null) {
					return clipboardString;
				} else {
					return "";
				}
			}
		});

		// ------------------------------------------------------------
		// Fonts configuration
		// Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

		final ImFontAtlas fontAtlas = io.getFonts();
		final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

		// Glyphs could be added per-font as well as per config used globally like here
		fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

		// fontConfig.setPixelSnapH(true);


		fontAtlas.addFontFromFileTTF("assets/fonts/Ruda-Bold.ttf", 18, fontConfig);

		fontConfig.destroy(); // After all fonts were added we don't need this config more

		// Method initializes LWJGL3 renderer.
		// This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
		// ImGui context should be created as well.
		imGuiGl3.init(glslVersion);
		setImGuiTheme();
	}

	public static void startFrame(final float deltaTime) {


		// We SHOULD call those methods to update Dear ImGui state for the current frame
		final ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(width, height);
		io.setDisplayFramebufferScale(1, 1);
		io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
		io.setDeltaTime(deltaTime);

		glfwGetCursorPos(window, mousePosX, mousePosY);

		// Update the mouse cursor
		final int imguiCursor = ImGui.getMouseCursor();
		glfwSetCursor(window, mouseCursors[imguiCursor]);
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	public static void endFrame() {
		// After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
		// At that moment ImGui will be rendered to the current OpenGL context.
		imGuiGl3.renderDrawData(ImGui.getDrawData());

	}

	// If you want to clean a room after yourself - do it by yourself
	private static void destroyImGui() {
		imGuiGl3.dispose();
		ImGui.destroyContext();
	}

	private static void setImGuiTheme(){
		ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 15, 15);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding ,  5.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 5, 5);
		ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 12, 8);
		ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, 8, 6);
		ImGui.pushStyleVar(ImGuiStyleVar.IndentSpacing, 25.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarSize, 15.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 9.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.GrabMinSize,5.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, 3.0f);
		
		ImGui.pushStyleColor(ImGuiCol.Text, 0.80f, 0.80f, 0.83f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.TextDisabled, 0.24f, 0.23f, 0.29f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.06f, 0.05f, 0.07f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.07f, 0.07f, 0.09f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.PopupBg, 0.07f, 0.07f, 0.09f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.Border, 0.80f, 0.80f, 0.83f, 0.88f);
		ImGui.pushStyleColor(ImGuiCol.BorderShadow, 0.92f, 0.91f, 0.88f, 0.00f);
		ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.24f, 0.23f, 0.29f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.TitleBgCollapsed, 1.00f, 0.98f, 0.95f, 0.75f);
		ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 0.07f, 0.07f, 0.09f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.MenuBarBg, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, 0.80f, 0.80f, 0.83f, 0.31f);
		ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.CheckMark, 0.80f, 0.80f, 0.83f, 0.31f);
		ImGui.pushStyleColor(ImGuiCol.SliderGrab, 0.80f, 0.80f, 0.83f, 0.31f);
		ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.Button, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.24f, 0.23f, 0.29f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.Header, 0.10f, 0.09f, 0.12f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0.06f, 0.05f, 0.07f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.Separator, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.SeparatorHovered, 0.24f, 0.23f, 0.29f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.SeparatorActive, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ResizeGrip, 0.00f, 0.00f, 0.00f, 0.00f);
		ImGui.pushStyleColor(ImGuiCol.ResizeGripHovered, 0.56f, 0.56f, 0.58f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.ResizeGripActive, 0.06f, 0.05f, 0.07f, 1.00f);
		//ImGui.pushStyleColor(ImGuiCol.CloseButton, 0.40f, 0.39f, 0.38f, 0.16f);
		//ImGui.pushStyleColor(ImGuiCol.CloseButtonHovered, 0.40f, 0.39f, 0.38f, 0.39f);
		//ImGui.pushStyleColor(ImGuiCol.CloseButtonActive, 0.40f, 0.39f, 0.38f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.PlotLines, 0.40f, 0.39f, 0.38f, 0.63f);
		ImGui.pushStyleColor(ImGuiCol.PlotLinesHovered, 0.25f, 1.00f, 0.00f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.PlotHistogram, 0.40f, 0.39f, 0.38f, 0.63f);
		ImGui.pushStyleColor(ImGuiCol.PlotHistogramHovered, 0.25f, 1.00f, 0.00f, 1.00f);
		ImGui.pushStyleColor(ImGuiCol.TextSelectedBg, 0.25f, 1.00f, 0.00f, 0.43f);
		ImGui.pushStyleColor(ImGuiCol.ModalWindowDimBg , 1.00f, 0.98f, 0.95f, 0.73f);
	}
}
