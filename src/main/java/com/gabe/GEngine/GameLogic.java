package com.gabe.GEngine;

import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.gameobject.components.Transform;
import com.gabe.GEngine.modelcreators.TerrainGenerator;
import com.gabe.GEngine.gameobject.Component;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.rendering.*;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.rendering.shaders.StaticShader;
import com.gabe.GEngine.utilities.AssetPool;
import com.gabe.GEngine.utilities.Loader;
import com.gabe.GEngine.utilities.listener.Keyboard;
import com.gabe.GEngine.utilities.listener.Mouse;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogic {
    private static boolean isPrepared = false;
    private static Loader loader = new Loader();
    private static AssetPool assetPool = new AssetPool(loader);
    private static List<GameObject> gameObjects = new ArrayList<>();
    private static GameObject selectedObject = null;
    private static double lastFrameTime = GLFW.glfwGetTime();
    private static float deltaTime;
    private static Renderer renderer;

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
        GameLogic.selectedObject = selectedObject;
    }

    //Wireframe mode
    public static boolean wasPressedLast = false;

    private static void loadTextures(){
        assetPool.getTexture("textureAtlas");
        //assetPool.getTexture("arrow");
       // assetPool.getTexture("grassTexture");
        assetPool.getTexture("wireframe");
    }

    private static void loadMaterials(){

        assetPool.addMaterial(new Material("Terrain", new StaticShader(), assetPool.getTexture("grassTexture")));

    }

    public static void Init() {
        renderer = new Renderer();
        Framebuffer.SetupFrameBuffer(loader);
        Component.initComponents();

        loadTextures();
        loadMaterials();


        gameObjects.add(TerrainGenerator.getTerrain(loader, assetPool));
        gameObjects.add(new GameObject("coolCube", new Transform().setPosition(new Vector3f(0, 0, -5)), new ModelRenderer(loader.loadBlockBenchObj("test_object"), new Material("TestModel", new StaticShader(), assetPool.getTexture("texture")))));

        isPrepared = true;
    }

    private static void getDeltaTime(){
        deltaTime = (float) (GLFW.glfwGetTime() - lastFrameTime);
        lastFrameTime = GLFW.glfwGetTime();
    }

    public static void Update()  {
        getDeltaTime();
        Camera.update(deltaTime);
        if(Keyboard.keyPressed(GLFW.GLFW_KEY_R)) {
            renderer.toggleWireframeMode();
        }
        if(Mouse.isLeftDown()){
            glfwSetInputMode(DisplayManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }

        if(Keyboard.keyPressed(GLFW.GLFW_KEY_ESCAPE)){
            glfwSetInputMode(DisplayManager.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }

        //System.out.println(picker.getMouseRay());
        //Start rendering scene to frame buffer
        //Framebuffer.bind();

        DisplayManager.clearColor();
        renderer.render(gameObjects);

        //Stop rendering to the frame buffer
       // Framebuffer.unbind();

        Mouse.update();
        Keyboard.update();
    }



    public static void Exit() {
        loader.cleanUp();
    }
}
