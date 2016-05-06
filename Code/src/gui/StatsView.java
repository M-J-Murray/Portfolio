package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gameplay.Statistics;

/**
 * This class creates a JPanel which displays the statistics screen at the end of each game.
 * The class retrieves all of the information from the Statistics class.
 * The class also loads the high scores text file, compares the winner's score and if appropriate append it to the file; keeping only top 10.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class StatsView implements ViewPanel {
	private JPanel panel;
	private Language currentLanguage = new Language();
	private ResourceBundle messages = currentLanguage.getMessages();
	private Statistics stats;

	/**
	 * Constructor method for the StatsView class
	 * It creates all of the components, sets their properties, layout managers and adds them to containers
	 * 
	 * @param Statistics stats
	 */
	public StatsView(Statistics stats) {
		this.stats = stats;
		// Create the components
		JButton mainMenuButton = new JButton();
		JLabel winnerLabel = new JLabel(messages.getString("winner") + stats.getWinnerId(),SwingConstants.CENTER);

		ImageIcon logoImage = new ImageIcon(this.getClass().getResource(messages.getString("stats_title")));
		JLabel logo = new JLabel(logoImage);

		// Set the properties of the components
		mainMenuButton.setText(messages.getString("back"));
		mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainMenuButton.setMinimumSize(new Dimension(75, 50));
		mainMenuButton.setPreferredSize(new Dimension(75, 50));
		mainMenuButton.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

		// Create containers to hold the components
		panel = new JPanel();
		JPanel statsPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		panel.setBackground(new Color(99, 187, 214));
		statsPanel.setOpaque(false);
		buttonPanel.setOpaque(false);

		JTable table = getTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new EmptyBorder(0,30,200,30));
		
		// Specify LayoutManagers
		panel.setLayout(new BorderLayout());
		statsPanel.setLayout(new BorderLayout());
		buttonPanel.setSize(100, 100);

		// Add components to containers
		buttonPanel.add(mainMenuButton);
		statsPanel.add(winnerLabel, BorderLayout.NORTH);
		statsPanel.add(scrollPane, BorderLayout.CENTER);
		panel.add(logo, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		panel.add(statsPanel);

		// Events
		mainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				MainMenuView mm = new MainMenuView();
				mm.addToJFrame();
				mm.setVisible();
			}
		});

		// Update the high scores
		updateHighScores();
	}

	/**
	 * updates the high scores if the winning players score is a new high score
	 */
	public void updateHighScores(){
		//gets the winners stats
		boolean[] isBot = stats.getIsBot();
		String winnerName = stats.getWinnerId();
		String[] playerNames = stats.getPlayerNames();

		int winnerID = 0;
		for (int i = 0; i < playerNames.length; i++) {
			if (winnerName.equals(playerNames)){
				winnerID = i;
				break;
			}
		}
		//wont update if the winner is a bot
		boolean skipUpdate = false;
		if (isBot[winnerID] == true){
			skipUpdate = true;
		}

		if (skipUpdate == false){
			//fetches the winners stats for the game
			int wallsPlaced = stats.getWallsPlaced()[winnerID];
			int stepsTaken = stats.getStepsTaken()[winnerID];
			int undosTaken = stats.getUndosTaken()[winnerID];
			int wallsRemoved = stats.getWallsRemoved()[winnerID];
			//generates a score based on the players results
			int score = (wallsPlaced+stepsTaken+wallsRemoved)-undosTaken;
			String[] playerScore = new String[2];
			playerScore[0] = playerNames[winnerID];
			playerScore[1] = score+"";
			//compares the winners scores to the previous scores
			String[][] allScores = compareHighScores(playerScore);
			if (allScores != null){
				PrintWriter writer;	
				try {
					//saves the high scores to the high scores files
					writer = new PrintWriter("./textFiles/highScores.txt");

					for (int i = 0; i < allScores.length; i++){
						writer.println(allScores[i][0]);
						writer.println(allScores[i][1]);
					}
					writer.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Loads the high scores to an array.
	 * Compares the winners' scores to the previous scores.
	 * Adds the players score to high scores if they beat it
	 * @param score - the array of the winners' name and score
	 * @return the array of names and high scores.
	 */
	public String[][] compareHighScores(String[] score){
		try {
			// Read the file
			FileReader fr = new FileReader("./textFiles/highScores.txt");

			// Wrap FileReader in BufferedReader
			BufferedReader br = new BufferedReader(fr);	

			String[][] scores = new String[10][2];

			String line;
			int x = 0;
			//loads the high scores to an array
			while ((line = br.readLine()) != null ){
				scores[x][0] = line;
				scores[x][1] = br.readLine();
				x++;
			}
			boolean update = false;
			String[][] tempScores = new String[10][2];
			//updates the scores if the new score is better than them
			for (int i = 0; i < scores.length; i++) {
				tempScores[i] = scores[i];
				if (scores[i][0] != null){
					if (Integer.parseInt(scores[i][1]) < Integer.parseInt(score[1])){
						for (int j = i; j < scores.length-1; j++) {
							tempScores[j+1] = scores[j];
						}
						tempScores[i] = score;
						scores = tempScores;
						update = true;
						break;
					}
				}
			}
			//returns the scores if they need to be updated
			if (update == true){
				return scores;
			} else {
				return null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method creates a table which displays the game's statistics
	 * 
	 * @return JTable table
	 */
	public JTable getTable(){
		// Create the headings
		String[] columnNames = { messages.getString("name"), messages.getString("walls_placed"),
				messages.getString("steps_taken"), messages.getString("undos_taken"), messages.getString("walls_removed")};

		String[] playerNames = stats.getPlayerNames();
		boolean[] isBot = stats.getIsBot();
		int playerCount = 0;
		
		// Check how many of the players were human players
		for (int i = 0; i < playerNames.length; i++) {
			if (isBot[i] == false){
				playerCount++;
			}
		}
		
		// Retrieve data from the Statistics class
		int[] wallsPlaced = stats.getWallsPlaced();
		int[] stepsTaken = stats.getStepsTaken();
		int[] undosTaken = stats.getUndosTaken();
		int[] wallsRemoved = stats.getWallsRemoved();
		
		// Create a 2D array for the data
		Object[][] data = new Object[playerCount][5];
		
		// Add human players' data only
		for (int i = 0; i < playerCount; i++) {
			if (isBot[i] == false){
				data[i][0] = playerNames[i];
				data[i][1] = wallsPlaced[i];
				data[i][2] = stepsTaken[i];
				data[i][3] = undosTaken[i];
				data[i][4] = wallsRemoved[i];
			}
		}

		// Create the table and make it non-editable
		JTable table = new JTable(data, columnNames);
		table.setEnabled(false);
		
		return table;
	}

	/**
	 * Return the JPanel
	 * 
	 * @return JPanel panel
	 */
	public JPanel getJPanel() {
		return panel;
	}

	/**
	 * Set the panel visible
	 */
	public void setVisible() {
		panel.setVisible(true);
	}

	/**
	 * Add the panel to the JFrame at its center
	 */
	public void addToJFrame() {
		GUI.getJFrame().add(panel, BorderLayout.CENTER);
	}

}
