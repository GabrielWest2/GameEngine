package com.gabe.GEngine.rendering.gui;

import com.gabe.GEngine.MainGameLoop;
import com.gabe.GEngine.listener.Keyboard;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.Material;
import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.rendering.Framebuffer;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.*;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;

public class ImGuiLayer {
    private static int index = 0;
    private static String hierarchyPayloadType = "SceneHierarchy";

    public static void renderGui(GameObject selectedObject, List<GameObject> objects) {
        ImGui.newFrame();
        setupDockspace();

        try{
            renderInspector(selectedObject);
            renderHierarchy(objects);
            renderGameView();
        }catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){

        }
        ImGui.render();
    }

    private static void renderGameView() {
        ImGui.begin("Game View");

        ImVec2 availSize = ImGui.getContentRegionAvail();
        int availableWidth = (int) availSize.x, availableHeight = (int) availSize.y;
        float aspectRatio = (float) DisplayManager.getHeight() / (float)DisplayManager.getWidth();

        int height = (int) (availableWidth * aspectRatio);

        int centerVal = (availableHeight - height) / 2;

        ImGui.setCursorPosY(centerVal);
        ImGui.image(Framebuffer.getCurrentFrame().getID(), availableWidth, height, 0, 1, 1, 0);

        ImGui.end();
    }

    private static void renderHierarchy(List<GameObject> objects){
        ImGui.begin("Scene Hierarchy");

        ImVec2 pos = ImGui.getCursorPos();

        ImGui.dummy(ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY());
        ImGui.setItemAllowOverlap();
        makeDroppable();
        ImGui.setCursorPos(pos.x, pos.y);
        index = 0;
        for(GameObject object : objects){
            if(object.getParent() == null){
                startTree(objects, object);
            }
        }
        ImGui.end();
    }

    private static void startTree(List<GameObject> objects, GameObject object){
        List<GameObject> children = new ArrayList<>();
        for(GameObject o : objects){
            if(o.getParent() == object){
                children.add(o);
            }
        }

        if(children.size() > 0){
            boolean treeOpen = treeNode(object);

            if(treeOpen){
                for(GameObject child : children){
                    startTree(objects, child);
                }
                ImGui.treePop();
            }
        }else{
            ImGui.pushStyleColor(ImGuiCol.Button, 1, 1, 1, 0);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 255f / 150f, 255f / 150f, 255f / 150f, 0.666f);
            ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 0);
            if(object.getParent() == null){
                ImGui.button("      " + object.getName());
            }else {
                ImGui.button(" " + object.getName());
            }
            ImGui.popStyleColor(2);
            ImGui.popStyleVar(1);
            makeDraggable(object);
        }

    }
    private static boolean combo = false;

    private static void renderInspector(GameObject object) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ImGui.begin("Inspector");
        if(object == null){
            ImGui.end();
            return;
        }

        ImGui.sameLine();
        ImString nameString = new ImString(100);
        nameString.set(new ImString(object.getName()));
        if (ImGui.inputText("Name", nameString)) {
            if(nameString.getLength() > 0)
                object.setName(nameString.get());
        }

        //Loop through all of the objects components
        for (Component component : object.getComponents()) {
            String componentName = component.getClass().getSimpleName();
            if (ImGui.collapsingHeader(componentName)) {
                ImGui.indent();
                Class<?> objClass = component.getClass();

                Field[] fields = objClass.getDeclaredFields();

                for (Field field : fields) {
                    //Make field public if it is private
                    if (Modifier.isPrivate(field.getModifiers()))
                        field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(component);

                    //Render field if is an int
                    if (value instanceof Integer) {
                        int[] ints = {(int) value};
                        if (ImGui.dragInt(name, ints))
                            field.set(component, ints[0]);
                    }
                    //Render field if is a bool
                    if (value instanceof Boolean) {
                        boolean bool = (boolean) value;
                        if (ImGui.checkbox(name, bool)) {
                            bool = !bool;
                            field.set(component, bool);
                        }

                    }
                    //Render field if is a string
                    if (value instanceof String) {
                        ImString string = new ImString(100);
                        string.set(new ImString((String) value));
                        if (ImGui.inputText(name, string)) {
                            field.set(component, string.get());
                        }
                    }
                    //Render field if is a float
                    if (value instanceof Float) {
                        float[] floats = {(float) value};
                        if (ImGui.dragFloat(name, floats))
                            field.set(component, floats[0]);
                    }
                    //Render field if is a double
                    if (value instanceof Double) {
                        double d = (double) value;
                        float[] floats = {(float) d};
                        if (ImGui.dragFloat(name, floats)) {
                            double dob = floats[0];
                            field.set(component, dob);
                        }

                    }
                    //Render field if is a color
                    if (value instanceof Color) {
                        Color color = (Color) value;
                        float[] vals = {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f};
                        if (ImGui.colorEdit4(name, vals))
                            field.set(component, new Color(vals[0], vals[1], vals[2], vals[3]));
                    }
                    //Render field if is a material
                    if (value instanceof Material) {
                        Material mat = (Material) value;
                        Color color = mat.getColor();
                        float[] vals = {color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
                        if (ImGui.colorEdit3(name, vals)) {
                            mat.setColor(new Color(vals[0], vals[1], vals[2]));
                        }

                    }
                    //Render field if is a vector4f
                    if (value instanceof Vector4f) {
                        Vector4f vec = (Vector4f) value;
                        float[] floats = {vec.x, vec.y, vec.z, vec.w};
                        if (ImGui.dragFloat4(name, floats))
                            field.set(component, new Vector4f(floats[0], floats[1], floats[2], floats[3]));
                    }
                    //Render field if is a vector3f
                    if (value instanceof Vector3f) {
                        Vector3f vec = (Vector3f) value;
                        float[] floats = {vec.x, vec.y, vec.z};
                        if (ImGui.dragFloat3(name, floats))
                            field.set(component, new Vector3f(floats[0], floats[1], floats[2]));
                    }
                    //Render field if is a vector2f
                    if (value instanceof Vector2f) {
                        Vector2f vec = (Vector2f) value;
                        float[] floats = {vec.x, vec.y};
                        if (ImGui.dragFloat2(name, floats))
                            field.set(component, new Vector2f(floats[0], floats[1]));
                    }

                }
                ImGui.unindent();
            }
        }
        ImGui.newLine();
        if(ImGui.button("Add Component +", ImGui.getContentRegionAvailX(), 40)){
            combo = true;
        }
        if(combo){
            Map<String, Class> classes = new HashMap<>();
            for(Class clazz : Component.components){
                classes.put(clazz.getSimpleName(), clazz);
            }
            String[] classNames = classes.keySet().toArray(new String[0]);

            ImInt selected = new ImInt(0);
            if(ImGui.combo("Choose a component...", selected, classNames)){
                String className = classNames[selected.get()];

                Component component = (Component) Class.forName(className).getConstructor().newInstance();
                object.addComponent(component);
                combo = false;
            }
            if(Keyboard.isKeyPressed(GLFW.GLFW_KEY_ESCAPE))
                combo = false;
        }
        ImGui.end();
    }

    private static boolean treeNode(GameObject gameObject){
        index++;

        ImGui.pushID(index);
        boolean nodeOpen = ImGui.treeNodeEx(gameObject.getName(), ImGuiTreeNodeFlags.DefaultOpen |
                ImGuiTreeNodeFlags.FramePadding |
                ImGuiTreeNodeFlags.OpenOnArrow |
                ImGuiTreeNodeFlags.SpanAvailWidth, gameObject.getName());
        ImGui.popID();

        makeDraggable(gameObject);

        return nodeOpen;
    }

    private static void makeDraggable(GameObject gameObject){
        if (ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload(hierarchyPayloadType, gameObject);

            ImGui.text(gameObject.getName());

            ImGui.endDragDropSource();
        }

        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload(hierarchyPayloadType);

            if (payload != null) {
                if (payload.getClass().isAssignableFrom(GameObject.class)) {
                    GameObject dragObject = (GameObject) payload;
                    if (!isChild(gameObject, dragObject))
                        dragObject.setParent(gameObject);

                }
            }

            ImGui.endDragDropTarget();
        }

        if(ImGui.isItemClicked()){
            MainGameLoop.setSelectedObject(gameObject);
        }
    }

    private static void makeDroppable(){
        if (ImGui.beginDragDropTarget()) {
            Object payload = ImGui.acceptDragDropPayload(hierarchyPayloadType);

            if (payload != null) {
                if (payload.getClass().isAssignableFrom(GameObject.class)) {
                    GameObject dragObject = (GameObject) payload;
                    dragObject.setParent(null);
                }
            }

            ImGui.endDragDropTarget();
        }
    }

    private static boolean isChild(GameObject child, GameObject parent){
        if(child == null)
            return false;
        GameObject res = child;

        while (!(res.getParent() == null || res.getParent() == parent)){
            res = res.getParent();
        }
        return res == parent;

    }

    private static void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoInputs | ImGuiWindowFlags.NoDecoration;
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(DisplayManager.getWidth(), DisplayManager.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoBringToFrontOnFocus|
                ImGuiWindowFlags.NoNavFocus;
        ImGui.begin("Dockspace Window", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(3);

        ImGui.dockSpace(ImGui.getID("Dockspace"));
        ImGui.end();
    }



}
