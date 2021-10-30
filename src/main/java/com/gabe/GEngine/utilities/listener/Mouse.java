package com.gabe.GEngine.utilities.listener;


import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static double lastX, lastY;
    private static double mouseX, mouseY;
    private static boolean leftButtonPressed = false;
    private static boolean rightButtonPressed = false;

    public static void cursorPosCallback(long window, double x, double y){
        mouseX = x;
        mouseY = y;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(button == GLFW_MOUSE_BUTTON_LEFT){
            if(action == GLFW_PRESS)
                leftButtonPressed = true;
            else
                leftButtonPressed = false;
        }
        else if(button == GLFW_MOUSE_BUTTON_RIGHT){
            if(action == GLFW_PRESS)
                rightButtonPressed = true;
            else
                rightButtonPressed = false;
        }
    }

    public static boolean isLeftDown(){
        return leftButtonPressed;
    }

    public static boolean isRightDown(){
        return rightButtonPressed;
    }

    public static double getMouseX(){
        return mouseX;
    }
    public static double getMouseY(){
        return mouseY;
    }

    public static void update(){
        lastX = mouseX;
        lastY = mouseY;
    }

    public static double getMouseDX(){
        return mouseX - lastX;
    }
    public static double getMouseDY(){
        return mouseY - lastY;
    }
}
