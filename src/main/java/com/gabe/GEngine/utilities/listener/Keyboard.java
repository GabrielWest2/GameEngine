package com.gabe.GEngine.utilities.listener;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    private static Map<Integer, Boolean> keysPressedLast = new HashMap<>();
    private static Map<Integer, Boolean> keysPressed = new HashMap<>();

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if (action == GLFW_PRESS)
            keysPressed.put(key, true);
        else if (action == GLFW_RELEASE)
            keysPressed.put(key, false);
    }

    public static void update(){
        keysPressedLast = keysPressed;
    }

    public static boolean isKeyPressed(int key){
        return keysPressed.getOrDefault(key, false);
    }

    public static boolean keyPressed(int key) {
        if(keysPressed.get(key) == null)
            return false;
        return keysPressed.get(key) && (!keysPressedLast.get(key));
    }
}
