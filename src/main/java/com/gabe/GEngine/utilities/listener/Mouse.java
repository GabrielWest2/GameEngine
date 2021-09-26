package com.gabe.GEngine.utilities.listener;

import imgui.ImGui;

public class Mouse {

    public static boolean isLeftDown(){
        return ImGui.isMouseDown(0);
    }

    public static boolean isRightDown(){
        return ImGui.isMouseDown(1);
    }

    public static boolean isMouseDown(int b){
        return ImGui.isMouseDown(b);
    }

    public static float getMouseX(){
        return ImGui.getMousePosX();
    }
    public static float getMouseY(){
        return ImGui.getMousePosY();
    }

    public static float getMouseDX(){
        return ImGui.getMouseDragDelta(1, 0).x;
    }
    public static float getMouseDY(){
        return ImGui.getMouseDragDelta(1, 0).y;
    }


}
