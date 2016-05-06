package gameplay;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class processes all the rules for the Quoridor game.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Rules {
	public final int MAX_WALLS;
	public final int MAX_PLAYERS;
	private ArrayList<String> botPlayerNames;
	private String[] playerColours; 
	private String[] playerNames; 
	private String gameRules;
	
	/**
	 * The constructor for Rules
	 * @param MAX_WALLS - the maximum amount of walls each player gets
	 * @param MAX_PLAYERS - the maximum amount of players for the game
	 * @param playerColours - the colours each of the players will be
	 * @param playerNames - the names of each of the player
	 * @param botPlayerNames - the names of the computer which are playing 
	 * @param gameRules - the rule set the game will follow
	 */
	public Rules(int MAX_WALLS, int MAX_PLAYERS, String[] playerColours, String[] playerNames, ArrayList<String> botPlayerNames, String gameRules){
		this.MAX_WALLS = MAX_WALLS;
		this.MAX_PLAYERS = MAX_PLAYERS;
		this.playerColours = playerColours;
		this.playerNames = playerNames;
		this.botPlayerNames = botPlayerNames;
		this.gameRules = gameRules;
	}

	/**
	 * @return the names of the computer players
	 */
	public ArrayList<String> getBotPlayerNames() {
		return botPlayerNames;
	}

	/**
	 * @return the colour strings of the computer players
	 */
	public String[] getPlayerColours() {
		return playerColours;
	}
	
	/**
	 * @return the names of the players
	 */
	public String[] getPlayerNames() {
		return playerNames;
	}

	/**
	 * @return the rules the game will follow
	 */
	public String getGameRules() {
		return gameRules;
	}
}
