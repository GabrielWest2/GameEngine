package com.gabe.GEngine;

import com.gabe.GEngine.rendering.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixMath {

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1,0,0));
		matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0,1,0));
		matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0,0,1));
		matrix.scale(new Vector3f(scale.x, scale.y, scale.z));
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0));
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		viewMatrix.translate(negativeCameraPos);
		return viewMatrix;
	}

}
