package Entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f colour = new Vector3f(0, 0, 0);
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	public Light(Vector3f position, Vector3f color){
		this.position = position;
		this.colour = color;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation){
		this.position = position;
		this.colour = color;
		this.attenuation = attenuation;
	}
	
	public Light(Vector3f attenuation){
		this.attenuation = attenuation;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	
	public void setColour(Vector3f colour) {
		this.colour = colour;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
	}
	
	

}
