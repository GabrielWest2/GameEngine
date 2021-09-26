package com.gabe.GEngine.rendering.shaders;

import com.gabe.GEngine.rendering.Camera;
import com.gabe.GEngine.utilities.MatrixMath;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;


public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_color;

	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String shaderDir){
		vertexShaderID = loadShader("src/main/java/com/gabe/GEngine/rendering/shaders/"+shaderDir+"/vertexShader.txt", GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader("src/main/java/com/gabe/GEngine/rendering/shaders/"+shaderDir+"/fragmentShader.txt", GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}

	protected void getAllUniformLocations(){
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_color = getUniformLocation("color");
	}

	public void loadTransformationMatrix(Matrix4f matrix){
		loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadColor(Color color){
		loadVector4(location_color, new Vector4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
	}

	public void loadProjectionMatrix(Matrix4f projection){
		loadMatrix(location_projectionMatrix, projection);
	}

	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID,uniformName);
	}

	public void start(){
		GL20.glUseProgram(programID);
	}

	public void stop(){
		GL20.glUseProgram(0);
	}

	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	protected abstract void bindAttributes();

	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	protected void loadVector4(int location, Vector4f vector){
		GL20.glUniform4f(location,vector.x,vector.y,vector.z,vector.w);
	}

	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}

	protected void loadMatrix(int location, Matrix4f matrix){
		GL20.glUniformMatrix4fv(location, false, matrix.get(matrixBuffer));
	}

	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		//if(GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
		//	System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
		//	System.err.println("Could not compile shader!");
		//	System.exit(-1);
		//}
		return shaderID;
	}

}
