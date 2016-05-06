package model;

import java.awt.Color;

/**
 * A simple class to describe the properties of a Pawn object
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Pawn {
	/** defines Coordinate location of the Pawn object on the game board */
	private Coordinate position;
	/** defines the colour of the Pawn object to be displayed */
	private String pawnColour;
	
	/**
	 * constructs Pawn object
	 * @param colour - the colour of the Pawn object to be displayed
	 */
	public Pawn(String colour){
		this.position = null;
		this.pawnColour = colour; 
	}
	
	
	/**
	 * @return the colour of the Pawn object to be displayed
	 */
	public String getColour(){
		return pawnColour;
	}

	/**
	 * @return the Coordinate location of the Wall
	 */
	public Coordinate getPosition() {
		return position;
	}

	/**
	 * sets the position of the Pawn object
	 * @param position - the position of the Pawn object to be set
	 */
	public void setPosition(Coordinate position) {
		this.position = position;
	}
}
