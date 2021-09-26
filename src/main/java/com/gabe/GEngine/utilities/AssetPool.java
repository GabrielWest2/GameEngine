package com.gabe.GEngine.utilities;

import com.gabe.GEngine.Material;
import com.gabe.GEngine.textures.Texture;

import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private Map<String, Texture> textures = new HashMap<>();
    private Map<String, Material> materials = new HashMap<>();
    private final Loader loader;
    public AssetPool(Loader loader){
        this.loader = loader;
    }

    /**
     * Adds a material if it has not already been added.
     *
     * @param material The material to add.
     *
     */
    public Material addMaterial(Material material){
        if(materials.containsKey(material.getName()))
            return material;

        materials.put(material.getName(), material);
        return material;
    }

    /**
     * Gets a material if it has been added
     * @param name The material to get.
     * @return The material.
     */
    public Material getMaterial(String name){
        if(!materials.containsKey(name))
            return null;

        return materials.get(name);
    }

    /**
     * Loads a texture if it has not already been loaded.
     *
     * @param filename The file to load.
     */
    private void loadTexture(String filename){
        Texture texture = new Texture(filename, loader);
        textures.put(filename, texture);
    }

    /**
     * Gets a texture if it has been loaded
     * @param filename The name of the texture to get.
     * @return The texture.
     */
    public Texture getTexture(String filename){
        if(!textures.containsKey(filename))
            loadTexture(filename);

        return textures.get(filename);
    }
}
