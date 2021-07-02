package com.gabe.GEngine.listener;

import imgui.ImGui;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {
    public static boolean isKeyPressed(int key){
        return ImGui.isKeyDown(key);
    }
}
