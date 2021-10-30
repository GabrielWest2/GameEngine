package com.gabe.GEngine.rendering;

import com.gabe.GEngine.rendering.shaders.ShaderProgram;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import com.gabe.GEngine.textures.Texture;

import java.awt.*;

public class Material {
    private String name;
    private Texture texture;
    private Color color = new Color(1, 1, 1, 1);
    private ShaderProgram shader;
    private int zIndex = 0;

    public Material(String name, ShaderProgram shader, Texture texture) {
        this.name = name;
        this.shader = shader;
        this.texture = texture;
    }



    public Material(String name, ShaderProgram shader) {
        this.name = name;
        this.shader = shader;
    }
    public Material(String name, ShaderProgram shader, Texture texture, Color color) {
        this.name = name;
        this.shader = shader;
        this.texture = texture;
        this.color = color;
    }

    public Material(String name, ShaderProgram shader, Color color) {
        this.name = name;
        this.shader = shader;
        this.color = color;
    }


    public void bindTexture(){
        if(this.texture == null)
            return;
        texture.bind();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
