package com.gabe.GEngine.modelcreators;

import com.gabe.GEngine.rendering.Material;
import com.gabe.GEngine.gameobject.GameObject;
import com.gabe.GEngine.gameobject.components.ModelRenderer;
import com.gabe.GEngine.rendering.RawModel;
import com.gabe.GEngine.utilities.AssetPool;
import com.gabe.GEngine.utilities.FastNoiseLite;
import com.gabe.GEngine.utilities.Loader;

public class TerrainGenerator {
    private static GameObject terrain;

    public static GameObject getTerrain(Loader loader, AssetPool assetPool){
        FastNoiseLite noise = new FastNoiseLite();
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        if(terrain != null)
            return terrain;
        int VERTEX_COUNT = 20;
        float SIZE = 10;
        float NOISE_DENSITY = 10;
        float NOISE_SCALE = 2;
        float TEXTURE_TILING = 10;

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                vertices[vertexPointer*3+1] = noise.GetNoise(i * NOISE_DENSITY, j * NOISE_DENSITY) * NOISE_SCALE;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1) * TEXTURE_TILING;
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1) * TEXTURE_TILING;
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        RawModel rawmodel = loader.loadToVAO(vertices, textureCoords, indices, normals);
        Material material = assetPool.getMaterial("Terrain");
        terrain = new GameObject("Terrain", new ModelRenderer(rawmodel, material));

        return terrain;
    }
}
