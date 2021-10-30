package com.gabe.GEngine.rendering;

import com.gabe.GEngine.utilities.listener.Keyboard;
import com.gabe.GEngine.utilities.listener.Mouse;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.event.KeyEvent;

public class Camera {
	
	private static Vector3f position = new Vector3f(5,2,-5);
	private static float pitch;
	private static float yaw=180;
	private static float roll;
	private static final float MOVE_SPEED = 2f;
	private static float mouseSensitivity = 0.25f;
	private Camera(){}
	
	public static void update(float deltaTime){
		//if(ImGui.isAnyItemFocused())
		//	return;
		yaw += Mouse.getMouseDX() * mouseSensitivity;
		pitch += Mouse.getMouseDY() * mouseSensitivity;
		pitch = Math.clamp(-90, 90, pitch);
		if (Keyboard.isKeyPressed(KeyEvent.VK_W)){
			position.x += MOVE_SPEED * Math.sin(Math.toRadians(yaw)) * deltaTime;
			position.z -= MOVE_SPEED * Math.cos(Math.toRadians(yaw)) * deltaTime;
		}

		if (Keyboard.isKeyPressed(KeyEvent.VK_S)){
			position.x -= MOVE_SPEED * Math.sin(Math.toRadians(yaw)) * deltaTime;
			position.z += MOVE_SPEED * Math.cos(Math.toRadians(yaw)) * deltaTime;
		}
		if (Keyboard.isKeyPressed(KeyEvent.VK_A)){
			position.x -= MOVE_SPEED * Math.sin(Math.toRadians(yaw+90)) * deltaTime;
			position.z += MOVE_SPEED * Math.cos(Math.toRadians(yaw+90)) * deltaTime;
		}
		if (Keyboard.isKeyPressed(KeyEvent.VK_D)){
			position.x -= MOVE_SPEED * Math.sin(Math.toRadians(yaw-90)) * deltaTime;
			position.z += MOVE_SPEED * Math.cos(Math.toRadians(yaw-90)) * deltaTime;
		}


		if (Keyboard.isKeyPressed(KeyEvent.VK_SPACE))
			position.y += MOVE_SPEED * deltaTime;
		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
			position.y -= MOVE_SPEED * deltaTime;

		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT))
			yaw -= 40 * deltaTime;
		if (Keyboard.isKeyPressed(GLFW.GLFW_KEY_RIGHT))
			yaw += 40 * deltaTime;
	}

	public static Vector3f getPosition() {
		return position;
	}

	public static float getPitch() {
		return pitch;
	}

	public static float getYaw() {
		return yaw;
	}

	public static float getRoll() {
		return roll;
	}

	public static void setPitch(float pitch) {
		pitch = pitch;
	}

	public static void setRoll(float roll) {
		roll = roll;
	}

	public static void setYaw(float yaw) {
		yaw = yaw;
	}
}
