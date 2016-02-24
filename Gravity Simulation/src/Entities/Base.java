package Entities;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

public abstract class Base {
	private int unitID;
	protected Vector3f position;
	private int electronegativity = new Random().nextInt(100);
	protected Vector3f angle;
	protected Vector3f angularSpeed;
	protected Vector3f directionalSpeed;
	protected float mass = 100;
	
	
	public int getUnitID() {
		return unitID;
	}

	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getAngle() {
		return angle;
	}

	public void setAngle(Vector3f angle) {
		this.angle = angle;
	}

	public Vector3f getAngularSpeed() {
		return angularSpeed;
	}

	public void setAngularSpeed(Vector3f angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	public Vector3f getDirectionalSpeed() {
		return directionalSpeed;
	}

	public void setDirectionalSpeed(Vector3f directionalSpeed) {
		this.directionalSpeed = directionalSpeed;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.angle.x += dx;
		this.angle.y += dy;
		this.angle.z += dz;
	}
	
	public void increaseDirectionalSpeed(float dx, float dy, float dz){
		this.directionalSpeed.x += dx;
		this.directionalSpeed.y += dy;
		this.directionalSpeed.z += dz;
	}
	
	public void increaseAngularSpeed(float dx, float dy, float dz){
		this.angularSpeed.x += dx;
		this.angularSpeed.y += dy;
		this.angularSpeed.z += dz;
	}
	
	public void update(){
		increaseRotation(angularSpeed.x, angularSpeed.y, angularSpeed.z);
		increasePosition(directionalSpeed.x, directionalSpeed.y, directionalSpeed.z);
	}

	public int getElectronegativity() {
		return electronegativity;
	}

	public void setElectronegativity(int electronegativity) {
		this.electronegativity = electronegativity;
	}
}
