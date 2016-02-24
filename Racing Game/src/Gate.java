import org.lwjgl.util.vector.Vector3f;

/**
 * This class defines the relevant information for the gate entity
 * 
 * @author Michael Murray
 */
public class Gate{
	private int gateNumber;
	private Vector3f position;
	private float angle;
	private int flagType;
	
	/**
     * Constructs a gate entity
     * @param gateNumber - the number assigned to the gate
     * @param position - the current position of the gate
     * @param angle - the angle the gate is facing
     * @param flagType - the type of flag the gate has (0 - checkered, 1 - green, 2 - red)
     */
	public Gate(int gateNumber, Vector3f position, float angle, int flagType) {
		this.gateNumber = gateNumber;
		this.position = position;
		this.angle = angle;
		this.flagType = flagType;
	}

	/**
	 * uses the formula :
	 * distance = sqrt(dx^2 + dz^2)
	 * to calculate whether the car has passed through the gate
     * @return whether the car is within range to cross the gate or not
     */
	public boolean checkGateCrossed(Car car){
		if (Math.sqrt(Math.pow(car.getPosition().x-position.x, 2)+Math.pow(car.getPosition().z-position.z, 2)) < 3.5f){
			car.setCurrentGate(car.getCurrentGate()+1);
			if (car.getCurrentGate() == 11){
				car.setCurrentGate(0);
			}
			if (flagType != 0){
				flagType = 2;
			}
			System.out.println("Gate " + gateNumber + " Passed");
			return true;
		} else {
			if (flagType != 0){
				flagType = 1;
			}
			return false;
		}
	}
	
	/**
     * @return the position of the gate
     */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
     * @return the angle of the gate
     */
	public float getAngle() {
		return angle;
	}
	
	/**
     * @return the flag type of the gate
     */
	public int getflagType() {
		return flagType;
	}



}
