package gui;

import gameplay.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

/**
 * This class creates the panel which displays the key bindings and allows the user to change them
 * The key bindings are saved in a text file which keeps the users' preferences between closing and reopening the game
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class KeyBindingsView implements ViewPanel {
	private JPanel panel;
	private String keyToChange;
	private Controls controls;
	
	// Keys
	private int keyCode;
	private char key;
	public String keyPressed;
	
	private Language currentLanguage;
	
	// Buttons
	private final JButton backButton = new JButton();
	private final JButton upButton = new JButton();
	private final JButton downButton = new JButton();
	private final JButton leftButton = new JButton();
	private final JButton rightButton = new JButton();
	private final JButton veritcalButton = new JButton();
	private final JButton horizontalButton = new JButton();
	private final JButton removeWallButton = new JButton();
	private final JButton undoButton = new JButton();
	private final JButton endTurnButton = new JButton();
	
	/**
	 * Constructor method for the KeyBindingsView class
	 * It creates all of the components, sets their properties, layout managers and adds them to containers
	 */
	public KeyBindingsView() {
		// Set the current language
		currentLanguage = new Language();
		ResourceBundle messages = currentLanguage.getMessages();
		
		// Set the controls
		controls = new Controls();
		keyToChange = null;

		final int blankSpace = 200; // Blank border at the edges of the panel

		// Create the components
		ImageIcon headerImage = new ImageIcon(this.getClass().getResource(messages.getString("key_bindings_title")));
		JLabel header = new JLabel(headerImage);

		JLabel upLabel = new JLabel(messages.getString("move_up"));
		JLabel downLabel = new JLabel(messages.getString("move_down"));
		JLabel leftabel = new JLabel(messages.getString("move_left"));
		JLabel rightLabel = new JLabel(messages.getString("move_right"));
		JLabel verticalLabel = new JLabel(messages.getString("place_ver_wall"));
		JLabel horizontalLabel = new JLabel(messages.getString("place_hor_wall"));
		JLabel removeWallLabel = new JLabel(messages.getString("remove_wall_kb"));
		JLabel undoLabel = new JLabel(messages.getString("undo_kb"));
		JLabel endTurnLabel = new JLabel(messages.getString("end_turn_kb"));

		// Set the properties of the components
		backButton.setText(messages.getString("back"));
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backButton.setMinimumSize(new Dimension(75, 50));
		backButton.setPreferredSize(new Dimension(75, 50));
		backButton.setMaximumSize(new Dimension(Short.MAX_VALUE,
				Short.MAX_VALUE));

		updateButtons();

		// Create containers to hold the components
		JPanel buttonPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		panel = new JPanel();

		panel.setBackground(new Color(99, 187, 214));
		buttonPanel.setOpaque(false);
		controlPanel.setOpaque(false);
		
		// Specify LayoutManagers
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		GridLayout controlLayout = new GridLayout(0, 2);
		controlPanel.setLayout(controlLayout);
		panel.setLayout(new BorderLayout());
		controlPanel.setBorder(new EmptyBorder(0, blankSpace, blankSpace / 4,
				blankSpace));
		buttonPanel.setBorder(new EmptyBorder(0, blankSpace, blankSpace / 2,
				blankSpace));

		// Add components to containers
		controlPanel.add(upLabel);
		controlPanel.add(upButton);
		controlPanel.add(downLabel);
		controlPanel.add(downButton);
		controlPanel.add(leftabel);
		controlPanel.add(leftButton);
		controlPanel.add(rightLabel);
		controlPanel.add(rightButton);
		controlPanel.add(verticalLabel);
		controlPanel.add(veritcalButton);
		controlPanel.add(horizontalLabel);
		controlPanel.add(horizontalButton);
		controlPanel.add(removeWallLabel);
		controlPanel.add(removeWallButton);
		controlPanel.add(undoLabel);
		controlPanel.add(undoButton);
		controlPanel.add(endTurnLabel);
		controlPanel.add(endTurnButton);
		buttonPanel.add(backButton);
		panel.add(header, BorderLayout.NORTH);
		panel.add(controlPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		// Events
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				// Send which key is meant to be changed
				keyToChange = "left";
				
		        // Change the key
				checkKeyStroke();
			}
			
		});
		
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "right";
				
				// Change the key
				checkKeyStroke();
			}
			
		});
		
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "up";
				
				// Change the key
				checkKeyStroke();
			}
			
		});
		
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "down";
					
				// Change the key
				checkKeyStroke();
			}
			
		});
		
		veritcalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "ver";
					
				// Change the key
				checkKeyStroke();
			}
			
		});
		
		horizontalButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "hor";

				// Change the key
				checkKeyStroke();
			}
			
		});
		
		removeWallButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "rem";

				// Change the key
				checkKeyStroke();
			}
			
		});
		
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "undo";

				// Change the key
				checkKeyStroke();
			}
			
		});
		
		endTurnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Send which key is meant to be changed
				keyToChange = "endTurn";

				// Change the key
				checkKeyStroke();
			}
			
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				OptionsView opt = new OptionsView();
				opt.addToJFrame();
				opt.setVisible();
			}
		});
	}

	/**
	 * This method returns the JPanel
	 * 
	 * @return JPanel panel
	 */
	public JPanel getJPanel() {
		return panel;
	}

	/**
	 * Makes the panel visible
	 */
	public void setVisible() {
		panel.setVisible(true);
	}

	/**
	 * Add the panel to the GUI's main JFrame, positioned at CENTER
	 */
	public void addToJFrame() {
		GUI.getJFrame().add(panel, BorderLayout.CENTER);
	}

	/**
	 * This method checks if the user has pressed they key, if so then record which key and convert it into a string so that it can be 
	 * saved in a text file
	 */
	public void checkKeyStroke(){
    	KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
					if (e.getID() == KeyEvent.KEY_PRESSED){
						keyCode = e.getKeyCode();
						key = (char)keyCode;
	                   	keyPressed = "" + key;   
	                   	switch(keyToChange){
	                   	case "left":
	                   		controls.setMoveLeft(keyPressed);
	                   		break;
	                   	case "right":
	                   		controls.setMoveRight(keyPressed);
	                   		break;
	                   	case "up":
	                   		controls.setMoveUp(keyPressed);
	                   		break;
	                   	case "down":
	                   		controls.setMoveDown(keyPressed);
	                   		break;
	                   	case "ver":
	                   		controls.setVerticalWall(keyPressed);
	                   		break;
	                   	case "hor":
	                   		controls.setHorizontalWall(keyPressed);
	                   		break;
	                   	case "rem":
	                   		controls.setRemoveWall(keyPressed);
	                   		break;
	                   	case "undo":
	                   		controls.setUndo(keyPressed);
	                   		break;
	                   	case "endTurn":
	                   		controls.setEndTurn(keyPressed);
	                   		break;
	                   	}
					}
					updateControlsFile();
					updateButtons();
				return false;
			}
    	 
    	});
    }	
	
	/**
	 * Save the new key to the text file
	 */
	public void updateControlsFile(){
		PrintWriter writer;	
		try {
			writer = new PrintWriter("./textFiles/controls.txt");
			writer.println(controls.getLeft());
			writer.println(controls.getRight());
			writer.println(controls.getUp());
			writer.println(controls.getDown());
			writer.println(controls.getVerticalWall());
			writer.println(controls.getHorizontalWall());
			writer.println(controls.getRemoveWall());
			writer.println(controls.getUndo());
			writer.println(controls.getEndTurn());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the display on the buttons to show the current key
	 */
	public void updateButtons(){
		upButton.setText(controls.getUp());
		downButton.setText(controls.getDown());
		leftButton.setText(controls.getLeft());
		rightButton.setText(controls.getRight());
		veritcalButton.setText(controls.getVerticalWall());
		horizontalButton.setText(controls.getHorizontalWall());
		removeWallButton.setText(controls.getRemoveWall());
		undoButton.setText(controls.getUndo());
		endTurnButton.setText(controls.getEndTurn());
	}
}