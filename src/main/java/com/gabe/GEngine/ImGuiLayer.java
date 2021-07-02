package com.gabe.GEngine;

import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.gameobject.GameObject;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ImGuiLayer {
    public static void renderGui(GameObject object) throws IllegalAccessException {
        ImGui.newFrame();
        ImGui.showDemoWindow();

        ImGui.begin("Inspector");
        for(Component component : object.getComponents()){
            String componentName = component.getClass().getSimpleName();
            if(ImGui.collapsingHeader(componentName)) {
                ImGui.indent();
                Class<?> objClass = component.getClass();

                Field[] fields = objClass.getDeclaredFields();
                for (Field field : fields) {
                    if(Modifier.isPrivate(field.getModifiers()))
                        field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(component);

                    if(value instanceof Integer){
                       int[] ints = {(int) value};
                       if(ImGui.dragInt(name, ints))
                           field.set(component, ints[0]);
                   }
                    if(value instanceof Boolean){
                       boolean bool = (boolean)value;
                       if(ImGui.checkbox(name, bool)){
                           bool = !bool;
                           field.set(component, bool);
                       }

                   }
                    if(value instanceof String){
                        ImString string = new ImString(100);
                        string.set(new ImString((String) value));
                        if(ImGui.inputText(name, string)){
                            field.set(component, string.get());
                        }
                    }
                    if(value instanceof Float){
                        float[] floats = {(float) value};
                        if(ImGui.dragFloat(name, floats))
                            field.set(component, floats[0]);
                    }
                    if(value instanceof Double){
                        double d = (double) value;
                        float[] floats = {(float) d};
                        if(ImGui.dragFloat(name, floats)){
                            double dob = floats[0];
                            field.set(component, dob);
                        }

                    }
                    if(value instanceof Color){
                        Color color = (Color) value;
                        float[] vals = {color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f};
                        if(ImGui.colorEdit4(name, vals))
                            field.set(component, new Color(vals[0], vals[1], vals[2], vals[3]));
                    }
                    if(value instanceof Vector4f){
                        Vector4f vec = (Vector4f) value;
                        float[] floats = {vec.x, vec.y, vec.z, vec.w};
                        if(ImGui.dragFloat4(name, floats))
                            field.set(component, new Vector4f(floats[0], floats[1], floats[2], floats[3]));
                    }
                    if(value instanceof Vector3f){
                        Vector3f vec = (Vector3f) value;
                        float[] floats = {vec.x, vec.y, vec.z};
                        if(ImGui.dragFloat3(name, floats))
                            field.set(component, new Vector3f(floats[0], floats[1], floats[2]));
                    }
                    if(value instanceof Vector2f){
                        Vector2f vec = (Vector2f) value;
                        float[] floats = {vec.x, vec.y};
                        if(ImGui.dragFloat2(name, floats))
                            field.set(component, new Vector2f(floats[0], floats[1]));
                    }
                }
                ImGui.unindent();
            }
        }


        ImGui.end();
        ImGui.render();
    }


}
