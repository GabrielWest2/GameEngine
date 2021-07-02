package com.gabe.GEngine.rendering;

import com.gabe.GEngine.listener.Keyboard;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.event.KeyEvent;

public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	private final float MOVE_SPEED = 2f;
	public Camera(){}
	
	public void move(float deltaTime){
		if (Keyboard.isKeyPressed(KeyEvent.VK_W))
			position.z -= MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(KeyEvent.VK_S))
			position.z += MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(KeyEvent.VK_A))
			position.x -= MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(KeyEvent.VK_D))
			position.x += MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE))
			position.y += MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			position.y -= MOVE_SPEED * deltaTime;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
}
