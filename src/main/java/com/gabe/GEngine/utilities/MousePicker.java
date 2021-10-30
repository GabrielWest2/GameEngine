package com.gabe.GEngine.utilities;

import com.gabe.GEngine.rendering.Camera;
import com.gabe.GEngine.rendering.display.DisplayManager;
import com.gabe.GEngine.utilities.listener.Mouse;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MousePicker {
    private Vector3f mouseRay;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    private Camera camera;

    public MousePicker(Camera camera, Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = MatrixMath.createViewMatrix();
        this.camera = camera;
    }

    public Vector3f getMouseRay(){
        return mouseRay;
    }

    public void update(){
        this.viewMatrix = MatrixMath.createViewMatrix();
        mouseRay = calculateMouseRay();
    }

    private Vector3f calculateMouseRay() {
        float mouseX = (float) Mouse.getMouseX();
        float mouseY = (float) Mouse.getMouseY();
        Vector2f normalizedCoordinates = getNormalizedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoordinates = new Vector4f(normalizedCoordinates.x, normalizedCoordinates.y, -1f, 1f);
        Vector4f eyeCoordinates = toEyeCoordinates(clipCoordinates);
        Vector3f worldRay = toWorldSpace(eyeCoordinates);
        return worldRay;
    }

    private Vector3f toWorldSpace(Vector4f eyeCoordinates){
        Matrix4f inverseView = viewMatrix.invert();
        Vector4f worldCoords = inverseView.transform(eyeCoordinates);
        Vector3f mouseRay = new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeCoordinates(Vector4f clipCoordinates){
        Matrix4f inverseProjection = projectionMatrix.invert();
        Vector4f eyeCoords = inverseProjection.transform(clipCoordinates);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY){
        float x = (2f * mouseX) / DisplayManager.getWidth() - 1;
        float y = (2f * mouseY) / DisplayManager.getHeight() - 1;
        return new Vector2f(x, y);
    }

    private Vector2f getNormalizedDeviceCoordinates(float mouseX, float mouseY, boolean flipped){
        float x = (2f * mouseX) / DisplayManager.getWidth() - 1;
        float y = (2f * mouseY) / DisplayManager.getHeight() - 1;
        return new Vector2f(x, (flipped ? -1 : 1) * y);
    }
}
