package com.gabe.GEngine.gameobject.components;

import com.gabe.GEngine.gameobject.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

public class Transform extends Component {
    private Vector3f position = new Vector3f(0, 0, -4);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f scale = new Vector3f(1, 1, 1);

    public void moveBy(Vector3f vec){
        position.add(vec);
    }

    public void rotateBy(Vector3f vec){
        rotation.rotateX(vec.x + rotation.x);
        rotation.rotateY(vec.y + rotation.y);
        rotation.rotateZ(vec.z + rotation.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Transform setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Transform setRotation(Vector3f rotation) {
        this.rotation = rotation;
        return this;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Transform setScale(Vector3f scale) {
        this.scale = scale;
        return this;
    }
}
