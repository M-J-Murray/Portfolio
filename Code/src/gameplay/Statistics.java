package gameplay;

import java.util.ArrayList;

/**
 * This class processes all the statistics for the Quoridor game.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Statistics {
	private int[] wallsPlaced;
	private int[] stepsTaken;
	private int[] undosTaken;
	private int[] wallsRemoved;
	private boolean[] isBot;
	private String[] playerNames;
	private String winnerId;
	
	/**
	 * The constructor for the Statistics class
	 * @param rules - the rules being used for the current game
	 */
	public Statistics(Rules rules){
		int players = rules.MAX_PLAYERS;
		wallsPlaced = new int[players];
		stepsTaken = new int[players];
		undosTaken = new int[players];
		wallsRemoved = new int[players];
		isBot = new boolean[players];
		/** sets all the isBot booleans to false */
		for (boolean b : isBot) {
			b = false;
		}
		playerNames = rules.getPlayerNames();
		ArrayList<String> botNames = rules.getBotPlayerNames();
		int i = 0;
		/** searches the botNames against the player names to determine which ones are bots or not*/
		for (String player : playerNames) {
			for (String bot : botNames) {
				if (player.equals(bot)){
					isBot[i] = true;
				}
			}
			i++;
		}
	}
	
	/**
	 * increments the walls placed for a specific player
	 * @param player - the player whos walls placed will be incremented
	 */
	public void incrementWallsPlaced(int player){
		wallsPlaced[player]++;
	}
	
	/**
	 * decrements the walls placed for a specific player
	 * @param player - the player whos walls placed will be decremented
	 */
	public void decrementWallsPlaced(int player){
		wallsPlaced[player]--;
	}
	
	/**
	 * increments the steps taken for a specific player
	 * @param player - the player whos steps taken will be incremented
	 */
	public void incrementStepsTaken(int player){
		stepsTaken[player]++;
	}
	
	/**
	 * decremented the steps taken for a specific player
	 * @param player - the player whos steps taken will be decremented
	 */
	public void decrementStepsTaken(int player){
		stepsTaken[player]--;
	}
	
	/**
	 * increments the undos taken for a specific player
	 * @param player - the player whos undos taken will be incremented
	 */
	public void incrementUndosTaken(int player){
		undosTaken[player]++;
	}
	
	/**
	 * increments the walls removed for a specific player
	 * @param player - the player whos walls removed will be incremented
	 */
	public void incrementWallsRemoved(int player){
		wallsRemoved[player]++;
	}
	
	/**
	 * decremented the walls removed for a specific player
	 * @param player - the player whos walls removed will be decremented
	 */
	public void decrementWallsRemoved(int player){
		wallsRemoved[player]--;
	}
	
	/**
	 * sets the winnerID for the specified players name
	 * @param playerID - the players name whos won the game
	 */
	public void setWinner(String playerID){
		winnerId = playerID;
	}

	/**
	 * @return the walls placed for all players
	 */
	public int[] getWallsPlaced() {
		return wallsPlaced;
	}

	/**
	 * @return the steps taken for all players
	 */
	public int[] getStepsTaken() {
		return stepsTaken;
	}

	/**
	 * @return the undos taken for all players
	 */
	public int[] getUndosTaken() {
		return undosTaken;
	}

	/**
	 * @return the walls removed for all players
	 */
	public int[] getWallsRemoved() {
		return wallsRemoved;
	}

	/**
	 * @return the winners name
	 */
	public String getWinnerId() {
		return winnerId;
	}
	
	/**
	 * @return the names of all the players
	 */
	public String[] getPlayerNames() {
		return playerNames;
	}

	/**
	 * @return the booleans for if players are bots
	 */
	public boolean[] getIsBot() {
		return isBot;
	}	
}
