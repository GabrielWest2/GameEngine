package com.gabe.GEngine.rendering.shaders;

import com.gabe.GEngine.rendering.Camera;
import com.gabe.GEngine.utilities.MatrixMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;

public class StaticShader extends ShaderProgram {

	public StaticShader() {
		super("unlit");
	}

	int location_lightPosition;
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		super.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		super.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		super.location_viewMatrix = super.getUniformLocation("viewMatrix");
		super.location_color = super.getUniformLocation("color");
		location_lightPosition = super.getUniformLocation("lightPosition");
	}

	public void bindLightPosition(Vector3f lightPosition){
		loadVector(location_lightPosition, lightPosition);
	}

}
