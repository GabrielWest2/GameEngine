package com.gabe.GEngine;

import com.gabe.GEngine.examples.CubeExample;
import com.gabe.GEngine.examples.TerrainGenerator;
import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.rendering.*;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.rendering.gui.ImGuiLayer;
import com.gabe.GEngine.rendering.shaders.StaticShader;
import com.gabe.GEngine.rendering.shaders.WireframeShader;
import com.gabe.GEngine.textures.Texture;
import com.gabe.GEngine.utilities.AssetPool;
import com.gabe.GEngine.utilities.Loader;
import com.gabe.GEngine.utilities.MousePicker;
import com.gabe.GEngine.utilities.listener.Keyboard;
import com.gabe.GEngine.utilities.listener.Mouse;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {
    private static boolean isPrepared = false;
    private static Loader loader = new Loader();
    private static AssetPool assetPool = new AssetPool(loader);
    private static Camera camera;
    private static List<GameObject> gameObjects = new ArrayList<>();
    private static GameObject selectedObject = null;
    private static double lastFrameTime = GLFW.glfwGetTime();
    private static float deltaTime;
    private static Renderer renderer;
    private static MousePicker picker;

    public static void main(String[] args) {
        DisplayManager.start();
    }

    public static Renderer getRenderer(){
        return renderer;
    }

    public static boolean isPrepared() {
        return isPrepared;
    }

    public static void setSelectedObject(GameObject selectedObject) {
        MainGameLoop.selectedObject = selectedObject;
    }

    //Wireframe mode
    public static boolean wasPressedLast = false;


    public static void Init() {
        camera = new Camera();
        renderer = new Renderer(camera);
        //picker = new MousePicker(camera, renderer.getProjectionMatrix());
        Framebuffer.SetupFrameBuffer(loader);
        Component.initComponents();


        renderer.setWireframeTexture(assetPool.getTexture("wireframe"));
        Texture texture = assetPool.getTexture("textureAtlas");
        Texture grassTexture = assetPool.getTexture("grassTexture");
        Material material = assetPool.addMaterial(new Material("Standard Unlit", new StaticShader(), grassTexture));

        gameObjects.add(CubeExample.getCube(loader, assetPool));
        gameObjects.add(CubeExample.getCube(loader, assetPool));
        gameObjects.add(TerrainGenerator.getTerrain(loader, assetPool));
        gameObjects.add(CubeExample.getCube(loader, assetPool));

        DisplayManager.setClearColor(131/255f, 178/255f, 252/255f);
        isPrepared = true;
    }

    private static void getDeltaTime(){
        deltaTime = (float) (GLFW.glfwGetTime() - lastFrameTime);
        lastFrameTime = GLFW.glfwGetTime();
    }

    public static void Update()  {
        getDeltaTime();
        DisplayManager.startFrame(deltaTime);
        camera.move(deltaTime);
        if(Keyboard.isKeyPressed(GLFW.GLFW_KEY_R)) {
            if(!wasPressedLast)
                renderer.toggleWireframeMode();
            wasPressedLast = true;
        }else{
            wasPressedLast = false;
        }
        //picker.update();
        //System.out.println(picker.getMouseRay());
        //Start rendering scene to frame buffer
        Framebuffer.bind();

        DisplayManager.clearColor();
        renderer.render(gameObjects);

        //Stop rendering to the frame buffer
        Framebuffer.unbind();

        renderGUI();
    }

    private static void renderGUI(){
        ImGuiLayer.renderGui(selectedObject, gameObjects);
        DisplayManager.endFrame();
    }


    public static void Exit() {
        loader.cleanUp();
    }
}
