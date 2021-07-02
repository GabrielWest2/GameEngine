package com.gabe.GEngine;

import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.gameobject.components.Transform;
import com.gabe.GEngine.objConverter.ModelData;
import com.gabe.GEngine.objConverter.OBJFileLoader;
import com.gabe.GEngine.rendering.Camera;
import com.gabe.GEngine.rendering.RenderEntity;
import com.gabe.GEngine.rendering.Renderer;
import com.gabe.GEngine.shaders.ShaderProgram;
import com.gabe.GEngine.shaders.StaticShader;
import imgui.ImGui;
import org.joml.Vector3f;
import com.gabe.GEngine.textures.Texture;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {
    private static boolean isPrepared = false;
    private static Loader loader = new Loader();
    private static AssetPool assetPool = new AssetPool(loader);
    private static Camera camera;
    private static List<GameObject> gameObjects = new ArrayList<>();

    private static float[] vertices = {
            -0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            0.5f,0.5f,0,

            -0.5f,0.5f,1,
            -0.5f,-0.5f,1,
            0.5f,-0.5f,1,
            0.5f,0.5f,1,

            0.5f,0.5f,0,
            0.5f,-0.5f,0,
            0.5f,-0.5f,1,
            0.5f,0.5f,1,

            -0.5f,0.5f,0,
            -0.5f,-0.5f,0,
            -0.5f,-0.5f,1,
            -0.5f,0.5f,1,

            -0.5f,0.5f,1,
            -0.5f,0.5f,0,
            0.5f,0.5f,0,
            0.5f,0.5f,1,

            -0.5f,-0.5f,1,
            -0.5f,-0.5f,0,
            0.5f,-0.5f,0,
            0.5f,-0.5f,1

    };

    private static int texX = 2, texY = 0;
    private static float width = 1/16f;
    private static float x1 = width * texX, y1 = width * texY, x2 = width + width * texX, y2 = width + width * texY;
    private static float[] textureCoords = {
            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1,

            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1,

            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1,

            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1,

            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1,

            x1,y1,
            x1,y2,
            x2,y2,
            x2,y1
    };

    private static int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22

    };

    private static Renderer renderer;

    public static Renderer getRenderer(){
        return renderer;
    }

    public static boolean isPrepared() {
        return isPrepared;
    }

    public static void main(String[] args) {
        DisplayManager.start();
    }

    private static double lastFrameTime = GLFW.glfwGetTime();

    public static void Init() {


        ShaderProgram shader = new StaticShader();
        camera = new Camera();
        renderer = new Renderer(camera);


        Texture texture = assetPool.getTexture("textureAtlas");
        Material material = assetPool.addMaterial(new Material("Standard Lit", shader, texture));

        ModelData modelData = OBJFileLoader.loadOBJ("dragon");
        RawModel dragonModel = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getIndices());

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);


        GameObject object = new GameObject();
        Transform transform = new Transform();
        ModelRenderer renderer = new ModelRenderer();
        renderer.setMaterial(material);
        renderer.setModel(dragonModel);
        object.addComponents(transform, renderer);

        gameObjects.add(object);

        DisplayManager.setClearColor(131/255f, 178/255f, 252/255f);
        isPrepared = true;
    }

    public static void Update() {

        double deltaTime = (GLFW.glfwGetTime() - lastFrameTime);
        lastFrameTime = GLFW.glfwGetTime();
        DisplayManager.startFrame((float) deltaTime);
       // entity.moveBy(0.01f, 0, 0);
        camera.move((float) deltaTime);
        DisplayManager.clearColor();
        
        renderer.render(gameObjects);

        try {
            ImGuiLayer.renderGui(gameObjects.get(0));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        DisplayManager.endFrame();
    }


    public static void Exit() {
        loader.cleanUp();
    }
}
