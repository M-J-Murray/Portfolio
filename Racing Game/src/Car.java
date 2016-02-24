import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * This class defines the relevant information for the car entity and processes all physics relating to the car.
 * 
 * @author Michael Murray
 */
public class Car {
	private Vector3f position;
	private float speed = 0.0f;
	private float acceleration = 0.0f;
	private float wheelRotationAngle = 0.0f;
	private float carAngle;
	private float da;
	private float carWheelAngle = 0.0f;
	private float topSpeed;
	private final float enginePower = 0.05f;
	private final float brakingForce = 0.05f;
	private float drag;
	private float dt;
	private int currentGate;
	private float viewerAngle = 0.0f;
	
	 /**
     * Constructs a car object
     * @param position - the current position of the car
     * @param topSpeed - the maximum speed the car may reach
     * @param carAngle - the angle the car is facing
     */
	public Car(Vector3f position, float topSpeed, float carAngle){
		this.position = position;
		this.topSpeed = topSpeed;
		this.carAngle = carAngle;
		setCurrentGate(0);
	}
	
	/**
     * updates relevant variables top simulate the car physics
     */
	public void update(){			
		//Update the car angle based on carWheelAngle and grip relative to animation
		//formula changes for slower speeds to stop car rotating without moving 
		if ((speed > 5f) || (speed < -5f)){
			da = (carWheelAngle/2 * (1-(speed/topSpeed)))/100;
			carAngleIncrement(da);
		} else {
			da = carWheelAngle * dt;
			carAngleIncrement(da);
		}
		
		//update the drag value
		drag = (speed/topSpeed)/50;
		
		//update cars speed
		speedIncrement(acceleration-drag);
		
		//generate new position based on speed and car angle
		position = calacuateCarPosition();
		
		//update car wheel rotation animation value
		wheelRotationAngle += speed / 4;		
	}
	
	/**
     * checks that the car position is within the relevant boundaries of the track
     */
	public void checkBoundaries(float mapX, float mapZ, float mapWidth, float mapHeight){
		float carDiameter = 0.3f;
		if (position.x > (mapX+mapWidth)-carDiameter){
			position.x = (mapX+mapWidth)-carDiameter;
		    speed = 0.0f;
		}
		if (position.x < mapX+carDiameter){
			position.x = mapX+carDiameter;
			speed = 0.0f;
		}
		if (position.z > (mapZ+mapHeight)-carDiameter){
			position.z = (mapZ+mapHeight)-carDiameter;
			speed = 0.0f;
		}
		if (position.z < mapZ+carDiameter){
			position.z = mapZ+carDiameter;
			speed = 0.0f;
		}
	}

	/**
     * binds relevant keys to different actions for the car
     */
	public void bindKeys(int accelerate, int decelerate, int turnLeft, int turnRight, int brakes, int rotateAroundCar){
		if(Keyboard.isKeyDown(accelerate)){
			acceleration = enginePower;
			if (speed < 0) {
				speedIncrement(brakingForce);
			}
		} else if(Keyboard.isKeyDown(decelerate)) {
			if (speed > 0) {
				speedDecrement(brakingForce);
			}
			acceleration = -enginePower;
		} else {
			acceleration = 0.0f;
		}
		
		if(Keyboard.isKeyDown(brakes)){
			if (speed < 0) {
				speedIncrement(brakingForce);
			} else {
				speedDecrement(brakingForce);
			}
		}

		if(Keyboard.isKeyDown(turnLeft)){
			carWheelAngleIncrement(0.1f);	
		} else {
			if (carWheelAngle > 0.0f) {
				carWheelAngleDecrement(0.08f);
			}
		}

		if(Keyboard.isKeyDown(turnRight)){
			carWheelAngleDecrement(0.1f);
		} else {
			if (carWheelAngle < 0.0f) {
				carWheelAngleIncrement(0.08f);
			}
		}
		
		if(Keyboard.isKeyDown(rotateAroundCar)){
			if(Keyboard.isKeyDown(brakes)){
				viewerAngleDecrement(0.05f);
			} else {
				viewerAngleIncrement(0.05f);
			}
		}
		
	}
	
	/**
     * @return the position of the car
     */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
     * increments the speed of the car by a relevant amount without surpassing topSpeed
     */
	public void speedIncrement(float incrementation) {
		this.speed += incrementation;
		if (speed >= topSpeed){
			speed = topSpeed;
		}
	}
	
	/**
     * decrements the speed of the car by a relevant amount without surpassing -topSpeed/2
     */
	public void speedDecrement(float decrementation) {
		this.speed -= decrementation;
		if (speed <= -topSpeed/2){
			speed = -topSpeed/2;
		}
	}
	
	/**
     * increments the viewing angle on the car
     */
	public void viewerAngleIncrement(float incrementation){
		this.viewerAngle += incrementation; 
		viewerAngle = formatAngle(viewerAngle);
	}
	
	/**
     * decrements the viewing angle on the car
     */
	public void viewerAngleDecrement(float decrementation){
		this.viewerAngle -= decrementation; 
		viewerAngle = formatAngle(viewerAngle);
	}
	
	/**
     * @return the wheel rotation
     */
	public float getWheelRotationAngle() {
		return wheelRotationAngle;
	}
	
	/**
     * increments the wheel rotation by the relevant amount while keeping it within 360 degrees
     */
	public void WheelRotationIncrement(float incrementation) {
		this.wheelRotationAngle += incrementation;
		formatAngle(wheelRotationAngle);
	}
	
	/**
     * decrements the wheel rotation by the relevant amount while keeping it within 360 degrees
     */
	public void WheelRotationDecrement(float decrementation) {
		this.wheelRotationAngle -= decrementation;
		formatAngle(wheelRotationAngle);
	}
	
	/**
     * @return the car angle
     */
	public float getCarAngle() {
		return carAngle;
	}

	/**
     * increments the car angle  by the relevant amount while keeping it within 360 degrees
     */
	public void carAngleIncrement(float incrementation) {
		this.carAngle += incrementation;
		carAngle = formatAngle(carAngle);
	}
	
	/**
     * increments the car angle by the relevant amount while keeping it within 360 degrees
     */
	public void carAngleDecrement(float decrementation) {
		this.carAngle -= decrementation;
		carAngle = formatAngle(carAngle);
	}
	
	/**
     * @return the front car wheels angle
     */
	public float getCarWheelAngle() {
		return carWheelAngle;
	}

	/**
     * increments the front car wheels angle by the relevant amount while keeping it below 45 degrees
     */
	public void carWheelAngleIncrement(float incrementation) {
		this.carWheelAngle += incrementation;

		if (carWheelAngle > 45.0f){
			carWheelAngle = 45.0f;		
		}
	}
	
	/**
     * decrements the front car wheels angle by the relevant amount while keeping it above -45 degrees
     */
	public void carWheelAngleDecrement(float decrementation) {
		this.carWheelAngle -= decrementation;

		if (carWheelAngle < -45.0f){
			carWheelAngle = -45.0f;		
		}
	}
	
	/**
     * @return the current gate the car needs to pass through
     */
	public int getCurrentGate() {
		return currentGate;
	}
	
	/**
     * sets the current gate the car needs to go through
     */
	public void setCurrentGate(int currentGate) {
		this.currentGate = currentGate;
	}

	/**
	 * uses the formula :
	 * x = x0 + r * cos(theta)
	 * z = z0 + r * sin(theta)
	 * to calculate the cars next postion based on its angle and speed
     * @return the positions the car will next be in
     */
	protected Vector3f calacuateCarPosition(){
		float tempX = position.x;
		float tempZ = position.z;
		float x = (float) (position.x - (convertToScale(speed) * Math.cos(Math.toRadians(-carAngle+(da*(speed/topSpeed))))));
		float z = (float) (position.z - (convertToScale(speed) * Math.sin(Math.toRadians(-carAngle+(da*(speed/topSpeed))))));
		dt = (float) Math.sqrt(Math.pow(tempX - x, 2) + Math.pow(tempZ - z, 2));
		return new Vector3f(x, position.y, z);
	}
	
	/**
	 * uses the formula :
	 * x = x0 + r * cos(theta)
	 * z = z0 + r * sin(theta)
	 * to calculate the camera position for the car at any one time
     * @return the positions the camera will next be in
     */
	public Vector3f getCameraPosition(){
		float x = (float) (position.x - 1.5f * Math.cos(Math.toRadians((-carAngle+(-(carWheelAngle)/2)+180.0f)+viewerAngle)));
		float z = (float) (position.z - 1.5f * Math.sin(Math.toRadians((-carAngle+(-(carWheelAngle)/2)+180.0f)+viewerAngle)));
		return new Vector3f(x, position.y+0.3f, z);
	}

	/**
	 * takes an angle and formats it within the ranges of 0 - 360 degrees
     * @return the formatted angle
     */
	public float formatAngle(float angle){
		if (angle < 0.0f){
			angle = angle + 360.0f;
		}
		if (angle > 360.0f){
			angle = angle - 360.0f;
		}
		return angle;
	}
	
	/**
	 * takes the speed of the car and converts it into a scale relevant to the simulation
     * @return the corrected speed
     */
	private float convertToScale(float inputInMPH){
		return (float) ((((inputInMPH * 1.60934)*1000)/3600)/1000);
	}
	}
