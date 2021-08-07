package com.gabe.GEngine.examples;

import com.gabe.GEngine.AssetPool;
import com.gabe.GEngine.Loader;
import com.gabe.GEngine.Material;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.rendering.RawModel;
import com.gabe.GEngine.rendering.Renderer;
import com.gabe.GEngine.rendering.shaders.StaticShader;
import com.gabe.GEngine.textures.Texture;

import java.util.Collections;

public class CubeExample {



    private static GameObject gameObject;
    private static int cubes = 0;

    private static final float[] vertices = {
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,0.5f,-0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f
    };

    private static final float[] textureCoordinates = {
            //Back
            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0,

            //Front
            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0,

            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0,

            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0,

            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0,

            0,0,
            0,0.0625f,
            0.0625f,0.0625f,
            0.0625f,0
    };

    private static final int[] indicies = {
            3,1,0,
            2,1,3,

            4,5,7,
            7,5,6,

            //Right
            11,9,8,
            10,9,11,

            //Left
            12,13,15,
            15,13,14,

            //Top
            19,17,16,
            18,17,19,

            //Bottom
            20,21,23,
            23,21,22
    };


    public static GameObject getCube(Loader loader, AssetPool assetPool){
            cubes++;
            RawModel rawmodel = loader.loadToVAO(vertices, textureCoordinates, indicies);
            Texture texture = assetPool.getTexture("textureAtlas");
            Material material = assetPool.addMaterial(new Material("Standard Unlit", new StaticShader(), texture));
            gameObject = new GameObject("Cube Test " + cubes, new ModelRenderer(rawmodel, material));

        return  gameObject;
    }
}
