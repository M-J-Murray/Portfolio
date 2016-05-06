package model;

import java.awt.Color;
import java.util.ArrayList;

/**
 * A class to describe the properties and functionality of a Player object
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Player {
	/** defines the players name*/	
	private String playerName;
	/** defines the maximum amount of walls the Player is allowed to place */
	private final int MAX_WALL_LIMIT;
	/** defines the current amount of walls the Player has placed */
	private ArrayList<Wall> wallsPlaced;

	/** defines the Pawn object which represents the Player's piece */
	private Pawn pawn;
	
	/**
	 * constructs the player object
	 * @param MAX_WALL_LIMIT - sets the MAX_WALL_LIMIT variable
	 * @param colour - sets the Player's Pawn objects colour
	 */
	public Player(int MAX_WALL_LIMIT, String colour, String playerName){
		this.MAX_WALL_LIMIT = MAX_WALL_LIMIT;
		this.playerName = playerName;
		wallsPlaced = new ArrayList<Wall>();
		pawn = new Pawn(colour);
	}
	
	/**
	 * @return the coordinate of the Player's Pawn object
	 */
	public Coordinate getPawnLocation(){
		return pawn.getPosition();
	}
	
	/**
	 * updates the Player's current walls placed
	 * @return boolean to state if Player has placed all their walls or not
	 */
	public Boolean placeWall(Coordinate coord, boolean isHorizontal){
		//checks to see if a Player has placed as many walls as they have available
		if (wallsPlaced.size() < MAX_WALL_LIMIT){
			wallsPlaced.add(new Wall(coord, isHorizontal));
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * updates the current position of the Player's Pawn object
	 * @param position - the coordinate the pawn will be moved to
	 */
	public void movePawn(Coordinate position){
		pawn.setPosition(position);
	}
	
	
	/**
	 * @return the Player's Name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * sets the Player's name
	 * @param playerName - the string the Player's name will be set as
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the Player's Pawn object
	 */
	public Pawn getPawn(){
		return pawn;
	}
	
	/**
	 * @return the number of walls the player has left to place
	 */
	public int getWallsLeft(){
		return MAX_WALL_LIMIT-wallsPlaced.size();
	}
	/**
	 * @return the array of walls the player has placed
	 */ 
	public ArrayList<Wall> getWallsPlaced() {
		return wallsPlaced;
	}

}
