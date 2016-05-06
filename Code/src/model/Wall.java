package model;

 /**
 * This class describes the properties a wall object
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Wall {
	/** defines the Coordinate location of the Wall on the game board */
	private Coordinate position;
	/** defines whether the rotation of the wall is horizontal or vertical */
	private boolean isHorizontal;
	
	/**
	 * constructs the Wall object
	 * @param position - defines the Coordinate position of the Wall
	 * @param isHorizontal - defines the rotation of the Wall
	 */
	public Wall(Coordinate position, boolean isHorizontal){
		this.position = position;
		this.isHorizontal = isHorizontal;
	}
	
	/**
	 * @return the Coordinate location of the Wall
	 */
	public Coordinate getPosition() {
		return position;
	}
	
	/**
	 * @return boolean to determine whether wall is horizontal or vertical
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}		
}
