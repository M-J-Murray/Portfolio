package model;

import java.awt.Color;
import java.util.Random;

/**
 * A class to describe the properties of the Board object.
 * Provides functionality for validating moves made on the board in the game.
 *
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Board{
	/** setup location ID's */
	public static enum BoardLocation {
		FREE_GAP, USED_GAP, FREE_SQUARE, USED_SQUARE, FREE_WALLGAP, USED_WALLGAP
	}

	/** defines a 2-Dimensional array of board locations to describe the board*/
	private BoardLocation[][] theBoard;
	
	/**
	 * constructs the Board object
	 * @param PLAYER_COUNT - sets the amount of Player's playing on the Board
	 * @param MAX_WALL_LIMIT - sets the maximum amount of walls which each player may place
	 * @param playerColours - sets the colours of the Players Pawn objects
	 */
	public Board (){
		theBoard = new BoardLocation[17][17];
		initialiseBoard();
	}
	
	/**
	 * @param location - the Coordinate location to be requested from the 2-D BoardLocation array
	 * @return the board location assigned to the Coordinate location
	 */
	public BoardLocation getBoardLocation(Coordinate location){
		return theBoard[location.getX()][location.getY()];
	}
	
	/**
	 * @param location - the Coordinate location to be requested from the 2-D BoardLocation array
	 * @param locationType - the type of location to be set
	 */
	public void setBoardLocation(Coordinate location, BoardLocation locationType){
		theBoard[location.getX()][location.getY()] = locationType;
	}
	
	
	/**
	 * checks that the WallGap the wall is to be placed and the relevant Gap's are available
	 * @param BoardLocation - the Coordinate location the Player's Wall is to be placed on the 2-D BoardLocation array
	 * @param isHorizontal -  whether Wall to be placed is horizontal or vertical
	 */
	public boolean checkWallFits(Coordinate boardCoord, boolean isHorizontal){
		if (isHorizontal == true){
			//if the wall is horizontal checks that left and right gap are free
			if ((getBoardLocation(new Coordinate(boardCoord.getX()-1, boardCoord.getY())) == BoardLocation.FREE_GAP) && 
					(getBoardLocation(new Coordinate(boardCoord.getX()+1, boardCoord.getY())) == BoardLocation.FREE_GAP)){
				return true;
			} else {
				return false;
			}
		} else {
			//if the wall is vertical checks that top and bottom gap are free
			if ((getBoardLocation(new Coordinate(boardCoord.getX(), boardCoord.getY()-1)) == BoardLocation.FREE_GAP) && 
					(getBoardLocation(new Coordinate(boardCoord.getX(), boardCoord.getY()+1)) == BoardLocation.FREE_GAP)){
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * updates the specified BoardLocation's availability
	 * @param coords - the coordinates of the gaps which will be updated
	 * @param isHorizontal - states whether wall being placed is horizontal or vertical
	 */
	public void updateGapAvailabilty(Coordinate boardCoord, boolean isHorizontal){
		setBoardLocation(boardCoord, BoardLocation.USED_WALLGAP);
		if (isHorizontal == true){
			setBoardLocation(new Coordinate(boardCoord.getX()-1, boardCoord.getY()), BoardLocation.USED_GAP);
			setBoardLocation(new Coordinate(boardCoord.getX()+1, boardCoord.getY()), BoardLocation.USED_GAP);
		} else {
			setBoardLocation(new Coordinate(boardCoord.getX(), boardCoord.getY()-1), BoardLocation.USED_GAP);
			setBoardLocation(new Coordinate(boardCoord.getX(), boardCoord.getY()+1), BoardLocation.USED_GAP);
		}
	}
		
	/**
	 * creates the 2-D BoardLocation array filling the relevant BoardLocation with either a Square, Gap or WallGap
	 */
	public void initialiseBoard(){
		for (int x = 1; x < 18; x++) {
			for (int y = 1; y < 18; y++) {
				//checks if x is not even and y is not even, then sets it as Square Object
				if ((x % 2 != 0) && (y % 2 != 0)){
					theBoard[x-1][y-1] = BoardLocation.FREE_SQUARE;
				} 
				//checks if x is even and y is not even, then sets it as Gap Object
				if ((x % 2 == 0) && (y % 2 != 0)){
					theBoard[x-1][y-1] = BoardLocation.FREE_GAP;
				}
				//checks if x is even and y is even, then sets it as WallGap Object and sets WallGap's Gap's with relevant Coordinate's
				if ((x % 2 == 0) && (y % 2 == 0)){
					theBoard[x-1][y-1] = BoardLocation.FREE_WALLGAP;
				}
				//checks if x is not even and y is even, then sets it as Gap Object
				if ((x % 2 != 0) && (y % 2 == 0)){
					theBoard[x-1][y-1] = BoardLocation.FREE_GAP;
				}
			}
		}
	}
	
	/**
	 * loops through the coordinates of the board and returns the a string representation of it
	 * @return the string representation of the board
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 17; x++) {
			sb.append("[");
			for (int y = 0; y < 17; y++) {
				sb.append(theBoard[y][x].toString()+"	");
				if (y == 16){
					sb.append("] \n");
				}
			}
		}
		return sb.toString();
	}
}
