package com.gabe.GEngine.rendering.shaders;

import com.gabe.GEngine.rendering.Camera;
import com.gabe.GEngine.utilities.MatrixMath;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;

public class WireframeShader extends ShaderProgram {

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_color;

	public WireframeShader() {
		super("wireframe");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_color = getUniformLocation("color");
	}

	@Override
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

	@Override
	public void loadColor(Color color){
		super.loadVector4(location_color, new Vector4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f));
	}

	@Override
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
}
