package gameplay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class keeps track of the current key bindings by reading them from a file.
 * It also contains setter methods to allow the user to change them.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Controls {
	/** The string variables used to represent the shortcuts*/
	public String moveLeft = "A";
	public String moveRight = "D";
	public String moveUp = "W";
	public String moveDown = "S";
	public String verticalWall = "V";
	public String horizontalWall = "H";
	public String removeWall = "Q";
	public String undo = "Z";
	public String endTurn = "M";
	
	/**
	 * Constructor for the class Controls
	 * It reads a text files to set the current key bindings
	 */
	public Controls(){
		getKeysFromFile("./textFiles/controls.txt");
	}
	
	/**
	 * This method reads the text file line by line and sets the current key bindings based on the line number
	 * 
	 * @param String file
	 */
	public void getKeysFromFile(String file){
		// This will reference one line at a time
		String line = null;
		
		try {
			// Read the file
			FileReader fr = new FileReader(file);
			
			// Wrap FileReader in BufferedReader
			BufferedReader br = new BufferedReader(fr);	
			
			int x = 0;
			
			// Read the text file
			while ((line = br.readLine()) != null ){
				x++;
				switch (x){
				case 1:
					moveLeft = line;
					break;
				case 2:
					moveRight = line;
					break;
				case 3:
					moveUp = line;
					break;
				case 4:
					moveDown = line;
					break;
				case 5:
					verticalWall = line;
					break;
				case 6:
					horizontalWall = line;
					break;
				case 7:
					removeWall = line;
					break;
				case 8:
					undo = line;
					break;
				case 9:
					endTurn = line;
					break;
				}
			}
			
			// Clear the line
			line = null;
			
			// Close file
			br.close();
		}
		
		catch(FileNotFoundException ex) {
			System.out.println("No file found.");
		}
			
		catch(IOException ex){
			System.out.println("Error reading the file.");
		}
		
	}
	
	/**
	 * Return the moveLeft key
	 * 
	 * @return String moveLeft
	 */
	public String getLeft(){
		return moveLeft;
	}
	
	/**
	 * Return the moveRight key
	 * 
	 * @return String moveRight
	 */
	public String getRight(){
		return moveRight;
	}
	
	/**
	 * Return the moveUp key
	 * 
	 * @return String moveUp
	 */
	public String getUp(){
		return moveUp;
	}
	
	/**
	 * Return the moveDown key
	 * 
	 * @return String moveDown
	 */
	public String getDown(){
		return moveDown;
	}
	
	/**
	 * Return the horizontalWall key
	 * 
	 * @return String horizontalWall
	 */
	public String getHorizontalWall(){
		return horizontalWall;
	}
	
	/**
	 * Return the verticalWall key
	 * 
	 * @return String verticalWall
	 */
	public String getVerticalWall(){
		return verticalWall;
	}
	
	/**
	 * Return the undo key
	 * 
	 * @return String undo
	 */
	public String getUndo(){
		return undo;
	}
	
	/**
	 * Return the endTurn key
	 * 
	 * @return String endTurn
	 */
	public String getEndTurn(){
		return endTurn;
	}

	/**
	 * Return the removeWall key
	 * 
	 * @return String removeWall
	 */
	public String getRemoveWall() {
		return removeWall;
	}

	/**
	 * Set the removeWall key
	 * 
	 * @param String removeWall
	 */
	public void setRemoveWall(String removeWall) {
		this.removeWall = removeWall;
	}

	/**
	 * Set the moveLeft key
	 * 
	 * @param String moveLeft
	 */
	public void setMoveLeft(String moveLeft) {
		this.moveLeft = moveLeft;
	}

	/**
	 * Set the moveRight key
	 * 
	 * @param String moveRight
	 */
	public void setMoveRight(String moveRight) {
		this.moveRight = moveRight;
	}

	/**
	 * Set the moveUp key
	 * 
	 * @param String moveUp
	 */
	public void setMoveUp(String moveUp) {
		this.moveUp = moveUp;
	}

	/**
	 * Set the moveDown key
	 * 
	 * @param String moveDown
	 */
	public void setMoveDown(String moveDown) {
		this.moveDown = moveDown;
	}

	/**
	 * Set the horizontalWall key
	 * 
	 * @param String horizontalWall
	 */
	public void setHorizontalWall(String horizontalWall) {
		this.horizontalWall = horizontalWall;
	}

	/**
	 * Set the verticalWall key
	 * 
	 * @param String verticalWall
	 */
	public void setVerticalWall(String verticalWall) {
		this.verticalWall = verticalWall;
	}

	/**
	 * Set the undo key
	 * 
	 * @param String undo
	 */
	public void setUndo(String undo) {
		this.undo = undo;
	}

	/**
	 * Set the endTurn key
	 * 
	 * @param String endTurn
	 */
	public void setEndTurn(String endTurn) {
		this.endTurn = endTurn;
	}
}