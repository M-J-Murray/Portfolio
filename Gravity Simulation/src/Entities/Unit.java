package Entities;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.TexturedModel;
import toolbox.Maths;

public class Unit extends Base {
	private TexturedModel model;
	private float scale;
	private float energy = 100;
	
	private Vector4f colour = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
	private float dampner;
	private float reflectivity;
	private boolean isLight;
	private boolean useFakeLighting;
	
	public Unit(TexturedModel model, Vector3f position, Vector3f angle, Vector3f directionalSpeed, Vector3f angularSpeed, float scale){
		this.model = model;
		this.position = position;
		this.angle = angle;
		this.directionalSpeed = directionalSpeed;
		this.angularSpeed = angularSpeed;
		this.scale = scale;
	}
	
	public Unit(Unit unit1){
		this.model = unit1.getModel();
		this.position = unit1.getPosition();
		this.angle = unit1.getAngle();
		this.directionalSpeed = unit1.getDirectionalSpeed();
		this.angularSpeed = unit1.getAngularSpeed();
		this.scale = unit1.getScale();
	}
	
	public void checkCollision(Unit unit, float GRAVITATIONAL_CONSTANT){
		Vector3f offset = Maths.findDifference(getPosition(), unit.getPosition());
		float distance = (float) Math.sqrt(
				Math.pow(offset.x, 2)+
				Math.pow(offset.y, 2)+
				Math.pow(offset.z, 2));
		if (distance > (getScale()+unit.getScale())){
			float accelleration = (float) (((GRAVITATIONAL_CONSTANT*((unit.getMass()*getMass())/Math.pow(distance, 2)))/unit.getMass()));
			Vector3f changeVector = Maths.findPointOnLine(getPosition(),unit.getPosition(), accelleration);
			increaseDirectionalSpeed(changeVector.x, changeVector.y, changeVector.z);
		} else {
			int error = Maths.collision3D(1f, this, unit, new Vector3f(unit.getPosition().x+(offset.x/2),unit.getPosition().y+(offset.y/2),unit.getPosition().z+(offset.z/2)));
		}
	}
	
	public Vector3f rotateUnit(Vector3f point){
		Vector3f result = new Vector3f(0,0,0);
		//TRANSLATE TO ORIGIN
		float x1 = (point.x - position.x);
		float y1 = (point.y - position.y);

		//APPLY ROTATION
		float temp_x1 = (float) (x1 * Math.cos(Math.toRadians(angle.z)) - y1 * Math.sin(Math.toRadians(angle.z)));
		float temp_y1 = (float) (x1 * Math.sin(Math.toRadians(angle.z)) + y1 * Math.cos(Math.toRadians(angle.z)));

		//TRANSLATE BACK
		point.x = (float) (temp_x1 + position.x);
		point.y = (float) (temp_y1 + position.y);

		//TRANSLATE TO ORIGIN
		x1 = (point.x - position.x);
		float z1 = (point.z - position.z);

		//APPLY ROTATION
		temp_x1 = (float) (x1 * Math.cos(Math.toRadians(angle.y)) - z1 * Math.sin(Math.toRadians(angle.y)));
		float temp_z1 = (float) (x1 * Math.sin(Math.toRadians(angle.y)) + z1 * Math.cos(Math.toRadians(angle.y)));

		//TRANSLATE BACK
		point.x = temp_x1 + position.x;
		point.z = temp_z1 + position.z;
		
		//TRANSLATE TO ORIGIN
		y1 = (point.y - position.y);
		z1 = (point.z - position.z);

		//APPLY ROTATION
		temp_y1 = (float) (y1 * Math.cos(Math.toRadians(angle.x)) - z1 * Math.sin(Math.toRadians(angle.x)));
		temp_z1 = (float) (y1 * Math.sin(Math.toRadians(angle.x)) + z1 * Math.cos(Math.toRadians(angle.x)));

		//TRANSLATE BACK
		point.y = temp_y1 + position.y;
		point.z = temp_z1 + position.z;
		
		result = new Vector3f(point.x,point.y,point.z);
		return result;
	}
	
	public Vector3f[] getUnitPoints(){
		Vector3f FrontPoint = new Vector3f(
				(float) (scale * Math.cos(Math.toRadians(90))) + position.x, 
				(float) (scale * Math.cos(Math.toRadians(135)))+ position.y, 
				(float) (scale * Math.cos(Math.toRadians(180)))+ position.z);
		FrontPoint = rotateUnit(FrontPoint);
		Vector3f RightPoint = new Vector3f(
				(float) (scale * Math.cos(Math.toRadians(30)))+ position.x, 
				(float) (scale * Math.cos(Math.toRadians(135)))+ position.y, 
				(float) (scale * Math.cos(Math.toRadians(60)))+ position.z);
		RightPoint = rotateUnit(RightPoint);
		Vector3f TopPoint = new Vector3f(
				(float) (scale * Math.cos(Math.toRadians(90)))+ position.x, 
				(float) (scale * Math.cos(Math.toRadians(45)))+ position.y, 
				(float) (scale * Math.cos(Math.toRadians(90)))+ position.z);
		TopPoint = rotateUnit(TopPoint);
		Vector3f LeftPoint = new Vector3f(
				(float) (scale * Math.cos(Math.toRadians(150)))+ position.x, 
				(float) (scale * Math.cos(Math.toRadians(135)))+ position.y, 
				(float) (scale * Math.cos(Math.toRadians(60)))+ position.z);
		LeftPoint = rotateUnit(LeftPoint);
		Vector3f[] results = new Vector3f[4];
		results[0] = FrontPoint;
		results[1] = RightPoint;
		results[2] = TopPoint;
		results[3] = LeftPoint;
		return results;
	}
	
	
		
	public void increaseEnergy(float dt){
		energy += dt;
	}
	
	public boolean isLight() {
		return isLight;
	}

	public void setLight(boolean isLight) {
		this.isLight = isLight;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
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


	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getDampner() {
		return dampner;
	}

	public void setDampner(float dampner) {
		this.dampner = dampner;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * @return the colour
	 */
	public Vector4f getColour() {
		return colour;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(Vector4f colour) {
		this.colour = colour;
	}
	
	

}
