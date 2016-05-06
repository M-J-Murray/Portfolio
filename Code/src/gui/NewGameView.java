package gui;

import gameplay.Quoridor;
import gameplay.Rules;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class creates a JPanel which allows the user to set conditions to create a new game, that information is passed in a constructor
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class NewGameView implements ViewPanel {
	private JPanel panel;
	private JPanel playerDetailsPanel;
	private JPanel newGamePanel;
	private final JComboBox gameMode;
	private final JComboBox gameRules;
	private final JComboBox wallRules;
	private Language currentLanguage;
	private ResourceBundle messages;
	//Options for the JComboBoxes
	private String[] gamePlayersStrings;
	private String[] gameRulesStrings;
	private String[] playerTypeStrings;
	private String[] playerColourStrings;
	private String[] gameWallsStrings;

	private JLabel[] playerLabels; 
	private JComboBox[] playerTypes;
	private JComboBox[] playerColours;
	private JTextField[] playerNames;

	private int playerColourChange;
	private int playerNumberChange;
	private int active = 0;

	/**
	 * Constructor method for the NewGameView class
	 * It creates all of the components, sets their properties, layout managers and adds them to containers
	 */
	public NewGameView() {	
		// Set the current language
		currentLanguage = new Language();
		messages = currentLanguage.getMessages();

		final int blankSpace = 100;  // Blank border at the edges of the panel

		//Options for the JComboBoxes
		gamePlayersStrings = new String[]{messages.getString("2_player_game"), messages.getString("4_player_game")};
		gameWallsStrings = new String[]{messages.getString("walls_5"), messages.getString("walls_10"), messages.getString("walls_15"), messages.getString("walls_20")};
		gameRulesStrings = new String[]{messages.getString("normal_rules"), messages.getString("challenge_rules")};
		playerTypeStrings = new String[]{messages.getString("computer_player"), messages.getString("human_player")};
		playerColourStrings = new String[]{messages.getString("colour_red"), messages.getString("colour_blue"), messages.getString("colour_green"), messages.getString("colour_yellow")};

		// Create the components
		gameMode = new JComboBox(gamePlayersStrings);
		gameRules = new JComboBox(gameRulesStrings);
		wallRules = new JComboBox(gameWallsStrings);
		JLabel gameModeLabel = new JLabel(messages.getString("game_mode"));
		JLabel gameRulesLabel = new JLabel(messages.getString("game_rules"));
		JLabel wallRulesLabel = new JLabel(messages.getString("players_walls"));

		JButton startGameButton = new JButton();
		JButton backButton = new JButton();

		ImageIcon headerImage = new ImageIcon(this.getClass().getResource(messages.getString("new_game_title")));
		JLabel header = new JLabel(headerImage);

		JLabel blankLabel = new JLabel(" ");

		// Set the properties of the components	
		gameMode.setSelectedIndex(0);
		gameRules.setSelectedIndex(0);
		wallRules.setSelectedIndex(0);

		header.setLocation(200, 0);
		header.setSize(400, 200);

		startGameButton.setText(messages.getString("start"));
		startGameButton.setSize(100, 50);
		startGameButton.setLocation(450, 460);

		backButton.setText(messages.getString("back"));
		backButton.setSize(100, 50);
		backButton.setLocation(250, 460);

		// Create containers to hold the components
		playerDetailsPanel = new JPanel();
		newGamePanel = new JPanel();
		panel = new JPanel();

		//Specify LayoutManagers
		panel.setLayout(null);
		newGamePanel.setLayout(new GridLayout(0, 2, 0, 10));
		updatePlayerInputs();

		newGamePanel.setSize(300, 100);
		newGamePanel.setLocation(250, 120);

		playerDetailsPanel.setSize(400, 100);
		playerDetailsPanel.setLocation(200, 240);		

		panel.setBackground(new Color(99, 187, 214));
		newGamePanel.setOpaque(false);
		playerDetailsPanel.setOpaque(false);
		
		// Add components to containers
		newGamePanel.add(gameModeLabel);
		newGamePanel.add(gameMode);
		newGamePanel.add(gameRulesLabel);
		newGamePanel.add(gameRules);
		newGamePanel.add(wallRulesLabel);
		newGamePanel.add(wallRules);
		panel.add(header);
		panel.add(newGamePanel);
		panel.add(playerDetailsPanel);
		panel.add(startGameButton);
		panel.add(backButton);

		// Events
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				Rules rules = setupRules();
				GameView gv = new GameView(rules);
				gv.addToJFrame();
				gv.setVisible();
				gv.getJPanel().setFocusable(true);
				gv.getJPanel().requestFocusInWindow();
			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				MainMenuView mm = new MainMenuView();
				mm.addToJFrame();
				mm.setVisible();
			}
		});
		
		gameMode.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				updatePlayerInputs();
			}
		});
	}

	/**
	 * generates the player settings panel depending on the player amound selected
	 */
	public void updatePlayerInputs(){
		//clears the panel
		playerDetailsPanel.removeAll();
		playerDetailsPanel.revalidate();
		//creates the components
		playerLabels = new JLabel[]{new JLabel(messages.getString("player_1")), new JLabel(messages.getString("player_2")), new JLabel(messages.getString("player_3")), new JLabel(messages.getString("player_4"))};
		playerTypes = new JComboBox[]{new JComboBox(playerTypeStrings), new JComboBox(playerTypeStrings), new JComboBox(playerTypeStrings), new JComboBox(playerTypeStrings)};
		playerColours = new JComboBox[]{new JComboBox(playerColourStrings), new JComboBox(playerColourStrings), new JComboBox(playerColourStrings), new JComboBox(playerColourStrings)};
		playerColours[0].setSelectedIndex(0);
		playerColours[1].setSelectedIndex(1);
		playerColours[2].setSelectedIndex(2);
		playerColours[3].setSelectedIndex(3);
		playerNames = new JTextField[]{new JTextField(20), new JTextField(20), new JTextField(20), new JTextField(20)};
		
		//sets the sizes of the components
		for (int i = 0; i < 4; i++) {
			playerLabels[i].setSize(40, 20);
			playerTypes[i].setSize(40, 20);
			playerColours[i].setSize(40, 20);
			playerNames[i].setSize(40, 20);
		}
		
		//attaches the headings
		playerDetailsPanel.add(new JLabel(""));
		playerDetailsPanel.add(new JLabel(messages.getString("player")));
		playerDetailsPanel.add(new JLabel(messages.getString("colour")));
		playerDetailsPanel.add(new JLabel(messages.getString("name")));

		//creates the inputs depending on the player count
		switch(gameMode.getSelectedItem().toString().substring(0, 1)){
		case "2":
			wallRules.setSelectedIndex(1);
			playerDetailsPanel.setLayout(new GridLayout(3, 4, 20, 10));
			playerDetailsPanel.setSize(400, 100);
			for (int i = 0; i < 2; i++) {
				playerDetailsPanel.add(playerLabels[i]);
				playerTypes[i].addItemListener (new ItemListener () {
					@Override
					public void itemStateChanged(ItemEvent e) {
						generateBotName(e);					
					}  
				});
				playerDetailsPanel.add(playerTypes[i]);
				playerColours[i].addItemListener (new ItemListener () {
					@Override
					public void itemStateChanged(ItemEvent e) {
						updatePlayerColourInputs(e);					
					}  
				});
				playerDetailsPanel.add(playerColours[i]);
				playerDetailsPanel.add(playerNames[i]);
			}
			break;
		case "4":
			wallRules.setSelectedIndex(0);
			playerDetailsPanel.setLayout(new GridLayout(5, 4, 20, 10));
			playerDetailsPanel.setSize(400, 172);
			for (int i = 0; i < 4; i++) {
				playerDetailsPanel.add(playerLabels[i]);
				playerTypes[i].addItemListener (new ItemListener () {
					@Override
					public void itemStateChanged(ItemEvent e) {
						generateBotName(e);					
					}  
				});
				playerDetailsPanel.add(playerTypes[i]);
				playerColours[i].addItemListener (new ItemListener () {
					@Override
					public void itemStateChanged(ItemEvent e) {
						updatePlayerColourInputs(e);						
					}  
				});
				playerDetailsPanel.add(playerColours[i]);
				playerDetailsPanel.add(playerNames[i]);
			}
			break;
		}
		playerTypes[0].setSelectedIndex(1);
		playerTypes[1].setSelectedIndex(1);
		playerTypes[2].setSelectedIndex(1);
		playerTypes[3].setSelectedIndex(1);
		getJPanel().updateUI();
	}

	/**
	 * Makes sure that only one player may select one colour 
	 * @param e - the combo box selection which triggered this method
	 */
	public void updatePlayerColourInputs(ItemEvent e){
		//method runs twice for an item select, then twice more when the player colours are updated
		//this makes sure it only picks up on the first two method runs
		active++;
		if (active < 3){
			//gets the players number whos changing colour and the colour they had it as before
			if (e.getStateChange() == ItemEvent.DESELECTED){
				for (int i = 0; i < 4; i++) {
					if (playerColours[i] == (JComboBox) e.getSource()){
						playerNumberChange = i;
						switch(e.getItem().toString()){
						case "Red": playerColourChange = 0; break;
						case "Blue": playerColourChange = 1; break;
						case "Green": playerColourChange = 2; break;
						case "Yellow": playerColourChange = 3; break;
						}
					}
				}
			//updates the player with the matching colour to have the other players colour
			} else if (e.getStateChange() == ItemEvent.SELECTED){
				for (int i = 0; i < 4; i++) {
					if (e.getItem().toString().equals(playerColours[i].getSelectedItem().toString()) &&
							i != playerNumberChange){
						playerColours[i].setSelectedIndex(playerColourChange);
						break;
					}
				}
			}
		}
		//allows method to be triggered correcly again
		if (active == 4){
			active = 0;
		}
	}

	/**
	 * generates the players name depending on if they are a human or computer player
	 * @param e - the combo box event which triggered this method
	 */
	public void generateBotName(ItemEvent e){
		if (e.getStateChange() == ItemEvent.SELECTED){
			int playerID = -1;
			for (int i = 0; i < 4; i++) {
				if (playerTypes[i] == (JComboBox) e.getSource()){
					playerID = i;
					break;
				}
			}
			if (playerTypes[playerID].getSelectedItem().toString().equals(messages.getString("computer_player"))){	
				playerNames[playerID].setText(messages.getString("computer")+(playerID+1));
				playerNames[playerID].setEnabled(false);
			} else {
				playerNames[playerID].setText(messages.getString("player")+(playerID+1));
				playerNames[playerID].setEnabled(true);
			}
		}
	}

	/**
	 * generates a set of rules based on the selected inputs for the new game
	 * @return the rules generated
	 */
	public Rules setupRules(){
		Rules theRules = null;
		int playerCount = 0;
		int wallCount = 0;
		String[] colours = null;
		String[] names = null;
		ArrayList<String> botIDs = new ArrayList<String>();
		switch(gameMode.getSelectedItem().toString().substring(0, 1)){
		//for a 2 player game
		case "2":
			playerCount = 2;
			colours = new String[]{playerColours[0].getSelectedItem().toString(), playerColours[1].getSelectedItem().toString()};
			names = new String[]{playerNames[0].getText(), playerNames[1].getText()};
			for (int i = 0; i < 2; i++) {
				if (playerTypes[i].getSelectedItem().toString().equals(messages.getString("computer_player"))){
					botIDs.add(playerNames[i].getText());
				}
			}
			break;
		//for a 4 player game
		case "4":
			playerCount = 4;
			colours = new String[]{playerColours[0].getSelectedItem().toString(), playerColours[1].getSelectedItem().toString(), playerColours[2].getSelectedItem().toString(), playerColours[3].getSelectedItem().toString()};
			names = new String[]{playerNames[0].getText(), playerNames[1].getText(), playerNames[2].getText(), playerNames[3].getText()};
			for (int i = 0; i < 4; i++) {
				if (playerTypes[i].getSelectedItem().toString().equals(messages.getString("computer_player"))){
					botIDs.add(playerNames[i].getText());
				}
			}
			break;
		}
		// determines the wall counts for the game
		switch(wallRules.getSelectedItem().toString().substring(0, 2)){
		case "5 ": wallCount = 5;break;
		case "10": wallCount = 10;break;
		case "15": wallCount = 15;break;
		case "20": wallCount = 20;break;
		}
		theRules = new Rules(wallCount, playerCount, colours, names, botIDs, gameRules.getSelectedItem().toString());
		return theRules;
	}

	/**
	 * Make the panel visible
	 */
	public void setVisible(){
		panel.setVisible(true);
	}

	/**
	 * Add the panel to the GUI's main JFrame, positioned at CENTER
	 */
	public void addToJFrame() {
		GUI.getJFrame().add(panel, BorderLayout.CENTER);
	}

	/**
	 * This method returns the main view panel
	 * @return JPanel panel
	 */
	public JPanel getJPanel() {
		return panel;
	}

}
