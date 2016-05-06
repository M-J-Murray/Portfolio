package model;

/**
 * A simple class to define the properties for a Coordinate object.
 * Provides functionality for comparing coordinates and getting neighbouring coordinates.
 *
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Coordinate {
	/** defines the x location of the coordinate */
	private int x;
	/** defines the y location of the coordinate */
	private int y;
	
	/**
	 * constructs Coordinate object
	 * @param x - the x variable of the coordinate
	 * @param y - the y variable of the coordinate
	 */
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x variable of the Coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * sets the x variable of the location
	 * @param x - the x variable of the coordinate
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y variable of the Coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * sets the y variable of the location
	 * @param y - the y variable of the coordinate
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * fetches the coordinate of the wall to be returned in the given direction from this coordinate
	 * this coordinate will most likely be a square coordinate
	 * @param direction - the string to determine the direction of the coordinate to be given  
	 * @return the coordinate of the wall in the given direction
	 */
	public Coordinate getWall(String direction){
		switch (direction){
		case "north":return new Coordinate(x,y-1);
		case "east":return new Coordinate(x+1,y);
		case "south":return new Coordinate(x,y+1);
		case "west":return new Coordinate(x-1,y);
		}
		return null;
	}
	
	/**
	 * fetches the coordinate of the square to be returned in the given direction from this coordinate
	 * this coordinate will most likely be a square coordinate
	 * @param direction - the string to determine the direction of the coordinate to be given  
	 * @return the coordinate of the square in the given direction from this square
	 */
	public Coordinate getSquare(String direction){
		switch (direction){
		case "north":return new Coordinate(x,y-2);
		case "east":return new Coordinate(x+2,y);
		case "south":return new Coordinate(x,y+2);
		case "west":return new Coordinate(x-2,y);
		}
		return null;
	}
	
	/**
	 * @return a string representation of this object
	 */
	public String toString(){
		return x+","+y+" ";
	}
	
	/**
	 * compares two coordinates and returns true if they are the same
	 * @param coord - the coordinate being compared to this coordinate
	 * @return the boolean stating if the coordinates match
	 */
	public boolean compare(Coordinate coord){
		boolean equal = false;
		if (coord.getX() == x && coord.getY() == y){
			equal = true;
		}
		return equal;
	}


}
