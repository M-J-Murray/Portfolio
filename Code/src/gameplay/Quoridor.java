package gameplay;

import gui.Language;
import gui.GameView.GameState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.ResourceBundle;

import model.*;
import model.Board.BoardLocation;

/**
 * This class processes all the gameplay elements of the Quoridor game.
 * It also handles the computer players turns.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Quoridor {
	/** the model facade used to pass model objects*/
	private ModelFacade models;
	/** the rules being used for this current game*/
	private Rules rules;
	/** the current players turn*/
	private int playersTurn;
	/** the grid used by the computer players to calculate there route */
	private Integer[][] grid = new Integer[17][17];
	/** the language and strings used for translations */
	private Language currentLanguage;
	private ResourceBundle messages;

	/**
	 * The constructor for the Quoridor class
	 * @param rules - the rules being used for this particular game
	 */
	public Quoridor(Rules rules){
		this.rules = rules;
		models = new ModelFacade();
		currentLanguage = new Language();
		messages = currentLanguage.getMessages();
	}

	/**
	 * Initialises all variables and methods need to start the game
	 */
	public void beginGame(){
		/** initialises the models in the models facade*/
		models.setBoard(new Board());
		models.getBoard().initialiseBoard();
		initialisePlayers();
		determineWhoStarts();
	}

	/**
	 * @return the Player models being used for the gameplay
	 */
	public Player[] getPlayers(){
		return models.getPlayers();
	}

	/**
	 * @return the Board model being used for the gameplay
	 */
	public Board getBoard(){
		return models.getBoard();
	}

	/**
	 * @return the Rules being used for the gameplay
	 */
	public Rules getRules(){
		return rules;
	}
	
	/**
	 * @return an integer stating how many walls the current player has left to place
	 */
	public int getPlayersWallsLeft(){
		return getPlayers()[playersTurn].getWallsLeft();
	}

	/**
	 * @return the integer corresponding the players turn
	 */
	public int getPlayersTurn(){
		return playersTurn;
	}

	/**
	 * changes the current players turn to the next players turn
	 */
	public void endPlayersTurn(){
		playersTurn++;
		if (playersTurn > rules.MAX_PLAYERS-1){
			playersTurn = 0;
		}

	}

	/**
	 * checks if the current players turn is actually the turn of the computer
	 * @return a boolean stating if the current player is a computer or not
	 */
	public boolean checkBotsGo(){
		for (int i = 0; i < rules.getBotPlayerNames().size(); i++) {
			if (models.getPlayers()[playersTurn].getPlayerName().equals(rules.getBotPlayerNames().get(i))){		
				return true;
			}
		}
		return false;
	}

	/**
	 * randomly decides which of the players will start the game
	 */
	public void determineWhoStarts(){
		Random random = new Random();
		playersTurn = random.nextInt(rules.MAX_PLAYERS);
	}

	/**
	 * creates the Player objects in the players array with their specified colours and names
	 */
	public void initialisePlayers(){
		/** gets the player's colours */
		String[] playerColours = rules.getPlayerColours();
		/** gets the player's names */
		String[] playerNames = rules.getPlayerNames();
		/** creates the players in the model facade */
		models.setPlayers(new Player[rules.MAX_PLAYERS]);
		
		/** sets up players max wall count and their starting position depending on the game rules*/
		if (rules.getGameRules() == messages.getString("normal_rules")){
			for (int i = 0; i < rules.MAX_PLAYERS; i++) {
				models.getPlayers()[i] = new Player(rules.MAX_WALLS, playerColours[i], playerNames[i]);
				switch (i){
				case 0: movePlayersPawn(i, new Coordinate(8,16));
				break;
				case 1: movePlayersPawn(i, new Coordinate(8,0));
				break;
				case 2: movePlayersPawn(i, new Coordinate(0,8));
				break;
				case 3: movePlayersPawn(i, new Coordinate(16,8));
				break;
				}
			}
		} else {
			for (int i = 0; i < rules.MAX_PLAYERS; i++) {
				models.getPlayers()[i] = new Player(rules.MAX_WALLS, playerColours[i], playerNames[i]);
				switch (i){
				case 0: movePlayersPawn(i, new Coordinate(16,16));
				break;
				case 1: movePlayersPawn(i, new Coordinate(0,0));
				break;
				case 2: movePlayersPawn(i, new Coordinate(0,16));
				break;
				case 3: movePlayersPawn(i, new Coordinate(16,0));
				break;
				}
			}
		}
	}

	/**
	 * This handles all the Artificial Intelligence the computer player has.
	 * Places walls or moves the pawn.
	 */
	public void takeBotsTurn(){
		/** initialises an array of coordinates for the route the player will take*/
		ArrayList<Coordinate> route = new ArrayList<Coordinate>();
		/** initialises an array of coordinates for the valid moves the computer can take from its current position*/
		ArrayList<Coordinate> validMoves = new ArrayList<Coordinate>();
		Random random = new Random();
		/** generates an integer between 0-1 to determine whether the computer will place a wall or move its pawn */
		int choice = random.nextInt(2);
		/** places a wall along a selected players best route*/
		if (choice == 1){
			/** string to check if wall is placed successfully */
			String wallAdded = "Invalid Move";
			/** an integer to specify how far along the selected players quickest route they should place a wall*/
			int distance = 0;
			/** selects which player it will block */
			int playerToBlock = random.nextInt(rules.MAX_PLAYERS);
			/** makes sure it cannot be itself */
			if (playerToBlock == playersTurn){
				playerToBlock++;
			}
			if (playerToBlock >= rules.MAX_PLAYERS){
				playerToBlock = 0;
			}
			/** generates the selected players quickest route */
			route = getFastestPlayerRoute(playerToBlock);
			/** tries placing a wall along each step of the selected players route until it places a wall or it cannot find room*/
			while (wallAdded != "Wall Placed" && distance != route.size()-1){
				distance++;
				Coordinate newPos = route.get(distance);
				/** checks for available gaps to place walls in every direction along the selected players route*/
				if (route.get(distance-1).getSquare("north").compare(newPos)){
					if (inBounds(route.get(distance-1).getWall("north").getWall("east"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("north").getWall("east"), true);
					} else if (inBounds(route.get(distance-1).getWall("north").getWall("west"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("north").getWall("west"), true);
					}
				} else if (route.get(distance-1).getSquare("south").compare(newPos)){
					if (inBounds(route.get(distance-1).getWall("south").getWall("east"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("south").getWall("east"), true);
					} else if (inBounds(route.get(distance-1).getWall("south").getWall("west"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("south").getWall("west"), true);
					}
				} else if (route.get(distance-1).getSquare("east").compare(newPos)){
					if (inBounds(route.get(distance-1).getWall("east").getWall("north"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("east").getWall("north"), false);
					} else if (inBounds(route.get(distance-1).getWall("east").getWall("south"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("east").getWall("south"), false);
					}
				} else if (route.get(distance-1).getSquare("west").compare(newPos)){
					if (inBounds(route.get(distance-1).getWall("west").getWall("north"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("west").getWall("north"), false);
					} else if (inBounds(route.get(distance-1).getWall("west").getWall("south"))){
						wallAdded = addPlayerWall(playersTurn, route.get(distance-1).getWall("west").getWall("south"), false);
					}
				}
			}
			/** if no wall was sucessfuly placed then it changes its choice to move the pawn instead*/
			if (wallAdded != "Wall Placed"){
				choice = 0;
			}
		}
		if (choice == 0){
			/** generates the quickest route to move its pawn to victory */
			route = getFastestPlayerRoute(playersTurn);
			/** generates an array of valid moves the computer may take */
			validMoves = getValidMoves();
			/** checks to see which valid move is along its optimum path */
			for (int i = 0; i < route.size(); i++) {
				Coordinate newPos = route.get(i);
				for (int j = 0; j < validMoves.size(); j++) {
					if (newPos.compare(validMoves.get(j))){
						movePlayersPawn(getPlayersTurn(), newPos);
					}
				}
			}
		} 
	}

	/**
	 * moves the specified Player's Pawn to the specified BoardLocation
	 * updates any board locations which may be relevant
	 * @param player - the integer which refers to which Player is being affected
	 * @param BoardLocation - the Coordinate location the Player's Pawn to be moved to on the 2-D BoardLocation array
	 */
	public boolean movePlayersPawn(int player, Coordinate boardCoord){
		//checks if players pawn had previous position to update board
		if (models.getPlayers()[player].getPawnLocation() != null){
			models.getBoard().setBoardLocation(models.getPlayers()[player].getPawnLocation(), BoardLocation.FREE_SQUARE);
		}
		//adds players pawn to new location
		models.getBoard().setBoardLocation(boardCoord, BoardLocation.USED_SQUARE);
		//moves player coordinates
		models.getPlayers()[player].movePawn(boardCoord);
		return true;
	}

	/**
	 * generates valid moves the player can take from their current position
	 * @return an array of valid moves available for the current player to take
	 */
	public ArrayList<Coordinate> getValidMoves(){
		ArrayList<Coordinate> validMoves = new ArrayList<Coordinate>();
		/** gets the current players pawn location */
		Coordinate playerPos = getPlayers()[getPlayersTurn()].getPawnLocation();
		/** checks which moves are available for every direction the player can move */
		validMoves.addAll(getDirectionalValidMoves(playerPos, "north"));
		validMoves.addAll(getDirectionalValidMoves(playerPos, "east"));
		validMoves.addAll(getDirectionalValidMoves(playerPos, "south"));
		validMoves.addAll(getDirectionalValidMoves(playerPos, "west"));
		return validMoves;
	}
	
	/**
	 * generates a list of valid coordinates the players pawn can move in the specified direction.
	 * @param playerPos - the current position of the players pawn 
	 * @param direction - the direction the valid moves need to be generated from (e.g north, east, south, west)
	 * @return an ArrayList of coordinates representing all valid moves the player can take in the selected direction
	 */
	public ArrayList<Coordinate> getDirectionalValidMoves(Coordinate playerPos, String direction){
		/** initialises the arrayList of valid moves */
		ArrayList<Coordinate> validMoves = new ArrayList<Coordinate>();
		/** checks the coordinate returned from next square in the direction is a location on the board*/
		if (inBounds(playerPos.getSquare(direction))){
			/** checks if the square in the direction is empty and there isnt a wall in the way*/
			if (checkFreeSquare(playerPos.getSquare(direction)) &&
					checkFreeGap(playerPos.getWall(direction))){

				validMoves.add(playerPos.getSquare(direction));
			/**  checks if the square beyond the first square is a location on the board*/
			} else if (inBounds(playerPos.getSquare(direction).getSquare(direction))){
				/** 
				 * checks if there is not wall in the way,
				 * if the square after the taken square is free
				 * and if there is not a wall in the way beyond the taken square
				 */
				if (checkFreeGap((playerPos.getWall(direction)))&&
						checkFreeSquare(playerPos.getSquare(direction).getSquare(direction)) && 
						checkFreeGap(playerPos.getSquare(direction).getWall(direction))){

					validMoves.add(playerPos.getSquare(direction).getSquare(direction));
				/**
				 * checks if there is not a wall in the way,
				 * if the square after the taken square is free
				 * and if there is a wall in the way of the free square
				 */
				} else if (checkFreeGap(playerPos.getWall(direction)) &&
						checkFreeSquare(playerPos.getSquare(direction).getSquare(direction)) && 
						!checkFreeGap(playerPos.getSquare(direction).getWall(direction))){
					/**
					 * checks depending on the direction whether the squares either side of the taken square are available
					 */
					if (direction == "north" || direction == "south"){
						if (inBounds(playerPos.getSquare(direction).getSquare("east"))){
							if (checkFreeSquare(playerPos.getSquare(direction).getSquare("east"))&&
									checkFreeGap(playerPos.getSquare(direction).getWall("east"))){
								validMoves.add(playerPos.getSquare(direction).getSquare("east"));
							}
						}
						if (inBounds(playerPos.getSquare(direction).getSquare("west"))){
							if (checkFreeSquare(playerPos.getSquare(direction).getSquare("west"))&&
									checkFreeGap(playerPos.getSquare(direction).getWall("west"))){
								validMoves.add(playerPos.getSquare(direction).getSquare("west"));
							}
						}
					} else {
						if (inBounds(playerPos.getSquare(direction).getSquare("north"))){
							if (checkFreeSquare(playerPos.getSquare(direction).getSquare("north")) &&
									checkFreeGap(playerPos.getSquare(direction).getWall("north"))){
								validMoves.add(playerPos.getSquare(direction).getSquare("north"));
							}
						}
						if (inBounds(playerPos.getSquare(direction).getSquare("south"))){
							if (checkFreeSquare(playerPos.getSquare(direction).getSquare("south")) &&
									checkFreeGap(playerPos.getSquare(direction).getWall("south"))){
								validMoves.add(playerPos.getSquare(direction).getSquare("south"));
							}
						}
					}
				}
			}

		}
		return validMoves;
	}

	/**
	 * checks to see if the new wall about to be placed will make it impossible for any of the players to make it to their end goal
	 * @param wallCoord - the coordinate of the wall about to be placed
	 * @param isHorizontal - boolean to state wheter the wall will be horizontal or vertical.
	 * @return a boolean stating whether all the players were able to reach their end goal
	 */
	public boolean checkClosedCircuit(Coordinate wallCoord, boolean isHorizontal){
		/** stack represents all possible moves which the player could take to get to their location */
		Deque<Coordinate> stack = new ArrayDeque<Coordinate>(); 
		/** successe represents how many players will be able to reach their end goal*/
		int sucesses = 0;
		/** checks for all players */
		for (int i = 0; i < rules.MAX_PLAYERS; i++) {
			/** updates the grid used by the computer find their route with the new wall*/
			updateGridWithWall(wallCoord, isHorizontal);
			/** checks if the players route is possible */
			stack = checkPlayerRoute(i);
			/** if the stack is empty then all possible routes were checked and no end goal was reached */
			if (stack.size() > 0){
				sucesses++;
			}
		}
		/** checks all players succeeded*/
		if (sucesses == rules.MAX_PLAYERS){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * a maze solving algorithm which checks using the grid whether the player would be able to reach their end goal
	 * @param player - the players route which is being checked
	 * @return a list of coordinates stating all the possible routes that were unchecked before suceess
	 */
	public Deque<Coordinate> checkPlayerRoute(int player){
		Deque<Coordinate> stack = new ArrayDeque<Coordinate>(); 
		/** holds the current location given from the stack */
		Coordinate pos = new Coordinate(0, 0);
		boolean done = false;
		boolean failed = false;
		stack = new ArrayDeque<Coordinate>(); 
		/** adds the players location to the stack as a starting point*/
		stack.push(getPlayers()[player].getPawnLocation());
		/** checks every available location around the pos and adds them to the stack, repeats until the process succeeds */
		while (done == false && failed == false){
			/** checks there are still locations in the stack, if not then the route has failed */
			if (stack.size() > 0){
				pos = stack.pop();
			} else if (stack.size() == 0) {
				failed = true;
			}
			if (failed == false){
				/** updates the grid location to say if it is a visited gap or visited square (1 = visited square, 2 = visited gap)*/
				if (getGridValue(pos) == 0){
					grid[pos.getX()][pos.getY()] = 1;
				} else {
					grid[pos.getX()][pos.getY()] = 2;
				}
				/** checks if the current pos is a finishing square of the player*/
				if (checkFinish(player, pos) == true){
					done = true;
				} else {
					/** adds all free locations in the given directions to the stack to be processed */
					String[] directionalStrings = getDirectionalStrings(player);
					if (validCoord(pos.getWall(directionalStrings[3]))) {
						stack.push(pos.getWall(directionalStrings[3]));
					}
					if (validCoord(pos.getWall(directionalStrings[2]))) {
						stack.push(pos.getWall(directionalStrings[2]));
					}
					if (validCoord(pos.getWall(directionalStrings[1]))) {
						stack.push(pos.getWall(directionalStrings[1]));
					}
					if (validCoord(pos.getWall(directionalStrings[0]))) {
						stack.push(pos.getWall(directionalStrings[0]));
					}
				}
			}
		}
		return stack;
	}

	/**
	 * Very similar to checkPlayerRoute(), however it generates the quickest route for a selected player based on 24 seperate directional patterns.
	 * With different permutations of the 4 directions being pushed into stack, different permutations generate different route lengths.
	 * selects the shortest route out of all the ones generated.
	 * @param player - the player who's route is being checked
	 * @return the shortest route generated
	 */
	public ArrayList<Coordinate> getFastestPlayerRoute(int player){
		/** the list of all routes generated */
		ArrayList<ArrayList<Coordinate>> allRoutes = new ArrayList<ArrayList<Coordinate>>();
		for (int i = 0; i < 24; i++) {
			/** refeshes grid for every route */
			updateGrid();
			ArrayList<Coordinate> testRoute = new ArrayList<Coordinate>();
			Deque<Coordinate> stack = new ArrayDeque<Coordinate>(); 
			Coordinate pos = new Coordinate(0, 0);
			boolean done = false;
			boolean failed = false;
			stack = new ArrayDeque<Coordinate>(); 
			/** adds the players location to the stack as a starting point*/
			stack.push(getPlayers()[player].getPawnLocation());
			/** checks every available location around the pos and adds them to the stack, repeats until the process succeeds */
			while (done == false && failed == false){
				/** checks there are still locations in the stack, if not then the route has failed */
				if (stack.size() > 0){
					pos = stack.pop();
					/** checks to see if the current location in the stack is a square, as only square coordinates are added to the route*/
					if (getGridValue(pos) == 0){
						/** adds the latest location to the route*/
						testRoute.add(pos);
						/** checks to see if the route is still possible using the currently added coordinates*/
						if (!checkRoutePossible(testRoute)){
							/**
							 * if the route failed, it means that the stacks current route failed and it has gone back to an earlier location
							 * the failed route coordinates must be removed from the current route
							 */
							RouteUpdater:
							/** 
							 * looks through the route reversely removing all failed coords until the latest coordinate added is a neighbour of an older Coordinate.
							 * This creates a possible route
							 */
							for (int x = testRoute.size()-2; x > 0; x--) {
								if (!checkIsValidNeighbour(testRoute.get(testRoute.size()-1),testRoute.get(x))){
									testRoute.remove(x);
								} else {
									break RouteUpdater;
								}
							}
						}
					}
				} else if (stack.size() == 0) {
					failed = true;
				}
				if (failed == false){
					/** updates the grid location to say if it is a visited gap or visited square (1 = visited square, 2 = visited gap)*/
					if (getGridValue(pos) == 0){
						grid[pos.getX()][pos.getY()] = 1;
					} else {
						grid[pos.getX()][pos.getY()] = 2;
					}
					/** checks if the current pos is a finishing square of the player*/
					if (checkFinish(player, pos) == true){
						done = true;
					} else {
					/** adds all free locations in the given directions to the stack to be processed */
						String[] directionalStrings = getDirectionalStrings(i);
						if (validCoord(pos.getWall(directionalStrings[3]))) {
							stack.push(pos.getWall(directionalStrings[3]));
						}
						if (validCoord(pos.getWall(directionalStrings[2]))) {
							stack.push(pos.getWall(directionalStrings[2]));
						}
						if (validCoord(pos.getWall(directionalStrings[1]))) {
							stack.push(pos.getWall(directionalStrings[1]));
						}
						if (validCoord(pos.getWall(directionalStrings[0]))) {
							stack.push(pos.getWall(directionalStrings[0]));
						}
					}
				}
			}
			/** removes all the excess loops made during the route to shorten it as much as possible */
			testRoute = shortenRoute(testRoute);
			allRoutes.add(testRoute);
		}
		/** searches through all of the routes to find the shortest one generated, then returns it*/
		ArrayList<Coordinate> fastestRoute = allRoutes.get(0);
		for (int i = 0; i < allRoutes.size(); i++) {
			if (allRoutes.get(i).size() < fastestRoute.size()){
				fastestRoute = allRoutes.get(i);
			}
		}
		return fastestRoute;
	}
	
	/**
	 * checks whether the coordinate given is a valid board location
	 * @param coord - the coordinate being checked
	 * @return a boolean stating whether it is a valid coord or not
	 */
	public boolean validCoord(Coordinate coord){
		if (inBounds(coord) == true){
			if (getGridValue(coord) == 0 ||
					getGridValue(coord) == 3){
				return true;
			} else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * checks if the two given coordinates are next to each other in any of the directions
	 * @param coord1 - the first coordinate being compared
	 * @param coord2 - the second coordinate being compared
	 * @return a boolean stating whether the coords are neighbours or not
	 */
	public boolean checkIsValidNeighbour(Coordinate coord1, Coordinate coord2){
		/** compares the coords in every direction*/
		if (coord1.compare(coord2.getSquare("north")) ||
			coord1.compare(coord2.getSquare("east"))  ||
			coord1.compare(coord2.getSquare("south")) ||
			coord1.compare(coord2.getSquare("west"))){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Cuts down the route by removing any loops in the route which would be unnecessary.
	 * @param route - the route being shortened
	 * @return the shortened route
	 */
	public ArrayList<Coordinate> shortenRoute(ArrayList<Coordinate> route){
		/** updates the grid for validation */
		updateGrid();
		ArrayList<Coordinate> newRoute = new ArrayList<Coordinate>();
		/** 
		 * searches from both sides of the route simultaneously to see if two coordinates are next to each other and do not have a wall between them.
		 * if they are next to each other, then all coordinates between the two points can be rendered useless and removed from the route.
		 */
		for (int x = 0; x < route.size(); x++) {
			newRoute.add(route.get(x));
			SearchLoop:
				for (int y = x+2; y < route.size(); y++) {
					if ((route.get(y).compare(route.get(x).getSquare("north")) && 
							getGridValue(route.get(x).getWall("north")) == 3) ||
							(route.get(y).compare(route.get(x).getSquare("east")) && 
									getGridValue(route.get(x).getWall("east")) == 3) ||
									(route.get(y).compare(route.get(x).getSquare("south")) && 
											getGridValue(route.get(x).getWall("south")) == 3) ||
											(route.get(y).compare(route.get(x).getSquare("west")) && 
													getGridValue(route.get(x).getWall("west")) == 3)){
						x = y-1;
						break SearchLoop;
					}
				}
		}		
		return newRoute;
	}

	/**
	 * searches along a route to make sure that every coordinate is a neighbour of the previous coorinate
	 * @param route - the route being traversed along.
	 * @return a boolean stating whether the route has been traversed sucessfuly or not.
	 */
	public boolean checkRoutePossible(ArrayList<Coordinate> route){
		for (int i = 0; i < route.size()-1; i++) {
			/** checks that the two coords are neighbours */
			if (!checkIsValidNeighbour(route.get(i), route.get(i+1))){
				return false;
			}
		}
		return true;
	}

	/**
	 * @param stringNumber - the number used to fetch the selected permutation of directions
	 * @return the string associated with it's permutation of the direcitons north, east, south, west.
	 */
	public String[] getDirectionalStrings(int stringNumber){
		switch (stringNumber){
		case 0: return new String[]{"north","west","east","south"};
		case 1: return new String[]{"north","west","south","east"};
		case 2: return new String[]{"north","east","south","west"};
		case 3: return new String[]{"north","east","west","south"};
		case 4: return new String[]{"north","south","east","west"};
		case 5: return new String[]{"north","south","west","east"};

		case 6: return new String[]{"east","west","north","south"};
		case 7: return new String[]{"east","west","south","north"};
		case 8: return new String[]{"east","north","south","west"};
		case 9: return new String[]{"east","north","west","south"};
		case 10: return new String[]{"east","south","north","west"};
		case 11: return new String[]{"east","south","west","north"};

		case 12: return new String[]{"south","west","north","east"};
		case 13: return new String[]{"south","west","east","north"};
		case 14: return new String[]{"south","north","east","west"};
		case 15: return new String[]{"south","north","west","east"};
		case 16: return new String[]{"south","east","north","west"};
		case 17: return new String[]{"south","east","west","north"};

		case 18: return new String[]{"west","east","north","south"};
		case 19: return new String[]{"west","east","south","north"};
		case 20: return new String[]{"west","north","south","east"};
		case 21: return new String[]{"west","north","east","south"};
		case 22: return new String[]{"west","south","north","east"};
		case 23: return new String[]{"west","south","east","north"};
		default: return null;
		}
	}

	/**
	 * @param coord - the coordinate being checked in the grid
	 * @return the value associated with coordinates location in the grid
	 */
	public int getGridValue(Coordinate coord){
		return grid[coord.getX()][coord.getY()];
	}

	/**
	 * updates all the grids values to match the board model
	 * 4 = FREE_WALLGAP, USED_WALLGAP, USED_GAP
	 * 3 = FREE_GAP
	 * 2 = checked FREE_GAP
	 * 1 = checked SQUARE
	 * 0 = checked SQUARE
	 */
	public void updateGrid(){
		for (int x = 0; x < 17; x++) {
			for (int y = 0; y < 17; y++) {
				BoardLocation boardLoc = getBoard().getBoardLocation(new Coordinate(x,y));
				if (boardLoc == BoardLocation.FREE_WALLGAP ||
						boardLoc == BoardLocation.USED_WALLGAP || 
						boardLoc == BoardLocation.USED_GAP){
					grid[x][y] = 4;
				} else if (boardLoc == BoardLocation.FREE_GAP){
					grid[x][y] = 3;
				} else {
					grid[x][y] = 0;
				}
			}
		}
	}

	/**
	 * updates all the grids values to match the board model, but with the extra wall being added
	 * 4 = FREE_WALLGAP, USED_WALLGAP, USED_GAP
	 * 3 = FREE_GAP
	 * 2 = checked FREE_GAP
	 * 1 = checked SQUARE
	 * 0 = checked SQUARE
	 * @param wallCoord - the coordinate the wall is being added to
	 * @param isHorizontal - the boolean to state if the wall is horizontal or vertical
	 */
	public void updateGridWithWall(Coordinate wallCoord, boolean isHorizontal){
		for (int x = 0; x < 17; x++) {
			for (int y = 0; y < 17; y++) {
				BoardLocation boardLoc = getBoard().getBoardLocation(new Coordinate(x,y));
				if (boardLoc == BoardLocation.FREE_WALLGAP ||
						boardLoc == BoardLocation.USED_WALLGAP || 
						boardLoc == BoardLocation.USED_GAP){
					grid[x][y] = 4;
				} else if (boardLoc == BoardLocation.FREE_GAP){
					grid[x][y] = 3;
				} else {
					grid[x][y] = 0;
				}
			}
		}
		/** adds the wall to the grid*/
		grid[wallCoord.getX()][wallCoord.getY()] = 4;
		if (isHorizontal == true){
			grid[wallCoord.getWall("east").getX()][wallCoord.getY()] = 4;
			grid[wallCoord.getWall("west").getX()][wallCoord.getY()] = 4;
		} else {
			grid[wallCoord.getX()][wallCoord.getWall("north").getY()] = 4;
			grid[wallCoord.getX()][wallCoord.getWall("south").getY()] = 4;
		}
	}
	
	/**
	 * Generates a string based on the grid object for testing purposes
	 * @return the string representing the grid
	 */
	public String gridToString(){
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 17; x++) {
			sb.append("[");
			for (int y = 0; y < 17; y++) {
				sb.append(grid[y][x].toString()+" ");
				if (y == 16){
					sb.append("] \n");
				}
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	
	/**
	 * checks if the given player is in a finishing square or not
	 * @param playersTurn - the player who is being checked
	 * @param coord - the current location being checked to see if it is a finishing location for the selected player
	 * @return a booolean stating wheter or not that is finishing location for the given location and player
	 */
	public boolean checkFinish(int playersTurn, Coordinate coord){
		boolean result = false;
		/** checks depending on the game type*/
		if (rules.getGameRules() == messages.getString("normal_rules")){
			switch (playersTurn){
			case 0:
				if (coord.getY() == 0){
					result = true;
				}
				break;
			case 1:
				if (coord.getY() == 16){
					result = true;
				}
				break;
			case 2:
				if (coord.getX() == 16){
					result = true;
				}
				break;
			case 3:
				if (coord.getX() == 0){
					result = true;
				}
				break;
			}
		} else {
			switch (playersTurn){
			case 0:
				if (coord.compare(new Coordinate(0,0))){
					result = true;
				}
				break;
			case 1:
				if (coord.compare(new Coordinate(16,16))){
					result = true;
				}
				break;
			case 2:
				if (coord.compare(new Coordinate(16,0))){
					result = true;
				}
				break;
			case 3:
				if (coord.compare(new Coordinate(0,16))){
					result = true;
				}
				break;
			}
		}
		return result;
	}

	/**
	 * @param coord - the coordinate on the board model being checked
	 * @return a boolean stating if the given coordinate represents a FREE_SQUARE on the board model
	 */
	public boolean checkFreeSquare(Coordinate coord){
		return models.getBoard().getBoardLocation(coord) == BoardLocation.FREE_SQUARE;
	}

	/**
	 * @param coord - the coordinate on the board model being checked
	 * @return a boolean stating if the given coordinate represents a FREE_GAP on the board model
	 */
	public boolean checkFreeGap(Coordinate coord){
		return models.getBoard().getBoardLocation(coord) == BoardLocation.FREE_GAP;
	}
	
	/**
	 * @param coord - the coordinate on the board model being checked
	 * @param isHorizontal - boolean stating if the wall is horizontal or vertical
	 * @return a boolean stating if the given coordinate represents a FREE_WALLGAP on the board model
	 */
	public boolean checkFreeWallGap(Coordinate coord, boolean isHorizontal){
		boolean isFree = false;
		if (isHorizontal == true){
			if (models.getBoard().getBoardLocation(coord) == BoardLocation.FREE_WALLGAP &&
					checkFreeGap(coord.getWall("east")) &&
					checkFreeGap(coord.getWall("west"))){
				isFree = true;
			}
		} else {
			if (models.getBoard().getBoardLocation(coord) == BoardLocation.FREE_WALLGAP &&
					checkFreeGap(coord.getWall("north")) &&
					checkFreeGap(coord.getWall("south"))){
				isFree = true;
			}
		}
		return isFree;
	}

	/**
	 * @param coord - the coordinate on the board model being checked
	 * @return a boolean stating whether the coordinate given is within the bounds of the size of the board
	 */
	public boolean inBounds(Coordinate coord){
		boolean inBounds = true;
		if (coord.getX() < 0 || coord.getX() > 16){
			inBounds = false;
		}
		if (coord.getY() < 0 || coord.getY() > 16){
			inBounds = false;
		}
		return inBounds;
	}	

	/**
	 * adds the specified Player's Wall to the specified BoardLocation
	 * updates any board locations which may be relevant
	 * @param player - the integer which refers to which Player is being affected
	 * @param boardCoord - the Coordinate location the Player's Wall is to be placed on the 2-D BoardLocation array
	 * @param isHorizontal - determines whether Wall to be placed is horizontal or vertical
	 */
	public String addPlayerWall(int playersTurn, Coordinate boardCoord, boolean isHorizontal){
		if (checkClosedCircuit(boardCoord, isHorizontal) == true){
			//checks if board location is a WallGap
			if (models.getBoard().getBoardLocation(boardCoord) == BoardLocation.FREE_WALLGAP){
				//checks if Wall fits in BoardLocations and if WallGap is available
				if (models.getBoard().checkWallFits(boardCoord, isHorizontal) == true){
					//updates players walls placed
					if (models.getPlayers()[playersTurn].placeWall(boardCoord, isHorizontal) == false){
						return "Player Has Used All Their Walls!";
					}
					//updates available gaps on board and adds wall to WallGap
					models.getBoard().updateGapAvailabilty(boardCoord, isHorizontal);
					return "Wall Placed";
				} else {
					return "No Room For Wall";
				}

			}
		}
		return "Invalid Move";
	}

	/**
	 * removes the specified Player's Wall from the specified BoardLocation
	 * updates any board locations which may be relevant
	 * @param boardCoord - the Coordinate location the Player's Wall is to be placed on the 2-D BoardLocation array
	 * @param isUndo - a boolean which states for what purpose the function is being used
	 * @return a string to state the outcome of the wall removal
	 */
	public String removePlayerWall(Coordinate boardCoord, boolean isUndo){
		boolean success = false;
		boolean isHorizontal = true;
		int i = 0;
		int playerNumber = 0;
		/** finds the wall from the players walls placed list and removes it */
		for (Player player : getPlayers()) {
			for (Wall wall : player.getWallsPlaced()) {
				if (wall.getPosition().compare(boardCoord)){
					/** finds the orientation of the wall*/
					isHorizontal = wall.isHorizontal();
					/** makes sure they cannot remove there own wall unless it is an undo */
					if (isUndo == false){
						if (playersTurn != i){
							player.getWallsPlaced().remove(wall);
							success = true;
							playerNumber = i;
						}
					} else {
						player.getWallsPlaced().remove(wall);
						success = true;
					}
					break;
				}
			}
			i++;
		}
		if (success == true){
			/** updates the board model to have the correct values for the removed walls locations*/
			getBoard().setBoardLocation(boardCoord, BoardLocation.FREE_WALLGAP);
			if (isHorizontal == true){
				getBoard().setBoardLocation(boardCoord.getWall("east"), BoardLocation.FREE_GAP);
				getBoard().setBoardLocation(boardCoord.getWall("west"), BoardLocation.FREE_GAP);
			} else {
				getBoard().setBoardLocation(boardCoord.getWall("north"), BoardLocation.FREE_GAP);
				getBoard().setBoardLocation(boardCoord.getWall("south"), BoardLocation.FREE_GAP);
			}
			/** returns string for undo purposes*/
			if (isHorizontal){
				return "p"+playerNumber+" t"+" Wall Removed";
			} else {
				return "p"+playerNumber+" f"+" Wall Removed";
			}
		} else {
			return "Failed";
		}
	}
}
