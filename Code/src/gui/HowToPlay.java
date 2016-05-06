package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class creates a JPanel which contains a tutorial for the user allowing them to learn how to play the game.
 * 
 * @author COMP7
 * @version v1.0, 28/04/2016
 */
public class HowToPlay implements ViewPanel {
	private JPanel panel;
	private Language currentLanguage;
	private ResourceBundle messages;
	private String currentTutorial = "movement";
	private JPanel currentTutPanel;
	private JButton previousButton;
	private JButton nextButton;
	
	public HowToPlay() {
		// Set the current language
		currentLanguage = new Language();
		messages = currentLanguage.getMessages();
		
		// Create the components
		ImageIcon logoImage = new ImageIcon(this.getClass().getResource(messages.getString("main_menu_title")));
		JLabel logo = new JLabel(logoImage);
		
		previousButton = new JButton(messages.getString("previous"));
		nextButton = new JButton(messages.getString("next"));
		JButton backButton = new JButton(messages.getString("back"));
		
		// Set the properties of the components
		updatePreviousButton();
		
		// Create containers to hold the components
		panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		
		panel.setBackground(new Color(99, 187, 214));
		buttonPanel.setOpaque(false);
		
		buttonPanel.setBorder(new EmptyBorder(0,0,50,0));		
		
		currentTutPanel = getTutorial("/tutorial/move.png", messages.getString("tut_move1"), messages.getString("tut_move2"), messages.getString("tut_move_heading"), "movement");
		
		// Specify LayoutManagers
		panel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new FlowLayout());
		
		// Add components to containers
		buttonPanel.add(previousButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		panel.add(logo, BorderLayout.NORTH);
		panel.add(currentTutPanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		// Events
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				MainMenuView mm = new MainMenuView();
				mm.addToJFrame();
				mm.setVisible();
			}
		});
		
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				panel.remove(currentTutPanel);
				panel.add(updateView(), BorderLayout.CENTER);
				updatePreviousButton();
				updateNextButton();
				panel.setVisible(true);
			}
		});
		
		previousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				panel.remove(currentTutPanel);
				panel.add(updateViewGoBack(), BorderLayout.CENTER);
				updatePreviousButton();
				updateNextButton();
				panel.setVisible(true);
			}
		});
		
	}
	
	/**
	 * Make the previous button non-clickable if on of the first page of the tutorial
	 */
	private void updatePreviousButton(){
		if(currentTutorial.equals("movement")){
			previousButton.setEnabled(false);
		} else {
			previousButton.setEnabled(true);
		}
	}
	
	/**
	 * Make the next button non-clickable if on of the last page of the tutorial
	 */
	private void updateNextButton(){
		if(currentTutorial.equals("victory")){
			nextButton.setEnabled(false);
		} else {
			nextButton.setEnabled(true);
		}
	}
	
	/**
	 * This method moves the player to the next tutorial screen
	 * 
	 * @return JPanel panel
	 */
	private JPanel updateView(){
		JPanel panel = new JPanel();
		switch(currentTutorial) {
		case "movement":
			panel = getTutorial("/tutorial/walls.png", messages.getString("tut_walls1"), messages.getString("tut_walls2"), messages.getString("tut_walls_heading"), "walls");
			currentTutPanel = panel;
			break;
		case "walls":
			panel = getTutorial("/tutorial/removeWall.png", messages.getString("tut_remove_walls1"), messages.getString("tut_remove_walls2"), messages.getString("tut_remove_walls_heading"), "remove_walls");
			currentTutPanel = panel;
			break;
		case "remove_walls":
			panel = getTutorial("/tutorial/undo.png", messages.getString("tut_undo1"), messages.getString("tut_undo2"), messages.getString("tut_undo_heading"), "undo");
			currentTutPanel = panel;
			break;
		case "undo":
			panel = getTutorial("/tutorial/victory.png", messages.getString("tut_victory1"), messages.getString("tut_victory2"), messages.getString("tut_victory_heading"), "victory");
			currentTutPanel = panel;
			break;
		}		
		return panel;
	}
	
	/**
	 * This method moves the player to the previous tutorial screen
	 * 
	 * @return JPanel panel
	 */
	private JPanel updateViewGoBack(){
		JPanel panel = new JPanel();
		switch(currentTutorial) {
		case "walls":
			panel = getTutorial("/tutorial/move.png", messages.getString("tut_move1"), messages.getString("tut_move2"), messages.getString("tut_move_heading"), "movement");
			currentTutPanel = panel;
			break;
		case "remove_walls":
			panel = getTutorial("/tutorial/walls.png", messages.getString("tut_walls1"), messages.getString("tut_walls2"), messages.getString("tut_walls_heading"), "walls");
			currentTutPanel = panel;
			break;
		case "undo":
			panel = getTutorial("/tutorial/removeWall.png", messages.getString("tut_remove_walls1"), messages.getString("tut_remove_walls2"), messages.getString("tut_remove_walls_heading"), "remove_walls");
			currentTutPanel = panel;
			break;
		case "victory":
			panel = getTutorial("/tutorial/undo.png", messages.getString("tut_undo1"), messages.getString("tut_undo2"), messages.getString("tut_undo_heading"), "undo");
			currentTutPanel = panel;
			break;
		}		
		return panel;
	}
	
	/**
	 * This method generates a JPanel which displays an appropriate tutorial based on passed varaibles
	 * 
	 * @param String imageLoc
	 * @param String tut1
	 * @param String tut2
	 * @param String heading
	 * @param String currentTut
	 * @return
	 */
	private JPanel getTutorial(String imageLoc, String tut1, String tut2, String heading, String currentTut){
		currentTutorial = currentTut;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		ImageIcon image = new ImageIcon(this.getClass().getResource(imageLoc));
		JLabel imageL = new JLabel(image);
		
		JLabel tutL1 = new JLabel(tut1);
		JLabel tutL2 = new JLabel(tut2);
		
		JLabel empty = new JLabel(" ");
		
		JLabel headingL = new JLabel(heading);
		
		tutL1.setAlignmentX(Component.CENTER_ALIGNMENT);
		tutL2.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageL.setAlignmentX(Component.CENTER_ALIGNMENT);
		headingL.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel.add(headingL, BorderLayout.NORTH);
		panel.add(imageL, BorderLayout.NORTH);
		panel.add(empty, BorderLayout.CENTER);
		panel.add(tutL1, BorderLayout.CENTER);
		panel.add(tutL2, BorderLayout.CENTER);
		panel.setOpaque(false);
		
		return panel;
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

}
