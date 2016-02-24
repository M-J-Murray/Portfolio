package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.TexturedModel;

public class Player {

	private static final float X_SPEED = 0.01f;
	private static final float Y_SPEED = 0.01f;
	private static final float Z_SPEED = 0.01f;

	private float currentXSpeed = 0;
	private float currentYSpeed = 0;
	private float currentZSpeed = 0;
	private float currentRotation = 0;
	
	private Base character;


	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		character = new Unit(model, position, new Vector3f(rotX, rotY, rotZ),  new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), scale);
	}

	public void move(){
		checkInputs();
		float speed;
		float tempAngle = (float) (Math.atan2(currentXSpeed, currentZSpeed) * 180 / Math.PI);
		if (currentXSpeed !=0 || currentZSpeed !=0){
			speed = (X_SPEED+Z_SPEED)*DisplayManager.getFrameTimeSeconds();
		} else {
			speed = 0;
		}
		float dx = (float) (speed * Math.sin(Math.toRadians(character.getAngle().y+tempAngle)));
		float dz = (float) (speed * Math.cos(Math.toRadians(character.getAngle().y+tempAngle)));
		character.increaseDirectionalSpeed(dx, currentYSpeed*DisplayManager.getFrameTimeSeconds(), dz);
		character.increaseRotation(0, currentRotation*DisplayManager.getFrameTimeSeconds(), 0);
		character.update();
	}



	private void checkInputs(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentZSpeed = -Z_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentZSpeed = Z_SPEED;
		} else {
			this.currentZSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.currentXSpeed = X_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.currentXSpeed = -X_SPEED;
		} else {
			this.currentXSpeed = 0;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			this.currentYSpeed = Y_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
			this.currentYSpeed = -Y_SPEED;
		} else {
			this.currentYSpeed = 0;
		}

		if(Mouse.isButtonDown(0)) {
			float pitchChange = Mouse.getDX() * 1f;
			currentRotation -= pitchChange;
		} else {
			currentRotation = 0;
		}
	}

	/**
	 * @return the character
	 */
	public Base getCharacter() {
		return character;
	}

	/**
	 * @param character the character to set
	 */
	public void setCharacter(Base character) {
		this.character = character;
	}
}
