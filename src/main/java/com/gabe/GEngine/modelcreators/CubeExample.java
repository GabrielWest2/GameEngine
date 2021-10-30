package com.gabe.GEngine.modelcreators;

import com.gabe.GEngine.utilities.AssetPool;
import com.gabe.GEngine.utilities.Loader;
import com.gabe.GEngine.rendering.Material;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.rendering.RawModel;

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
            Material material = assetPool.getMaterial("Terrain");
            gameObject = new GameObject("Cube Test " + cubes, new ModelRenderer(rawmodel, material));

        return  gameObject;
    }
}
