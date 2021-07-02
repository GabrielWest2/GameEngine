package com.gabe.GEngine.rendering;

import com.gabe.GEngine.Material;
import com.gabe.GEngine.RawModel;
import org.joml.Vector3f;

public class RenderEntity {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private RawModel model;
    private Material material;


    public RenderEntity(Vector3f position, Vector3f rotation, Vector3f scale, RawModel model, Material material) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.model = model;
        this.material = material;
    }


    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public RawModel getModel() {
        return model;
    }

    public void setModel(RawModel model) {
        this.model = model;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void rotateBy(float x, float y, float z) {
        this.rotation.add( new Vector3f(x, y, z));
    }

    public void moveBy(float x, float y, float z) {
        this.position.add( new Vector3f(x, y, z));
    }

    public void scaleBy(float x, float y, float z) {
        this.scale.add( new Vector3f(x, y, z));
    }
}
