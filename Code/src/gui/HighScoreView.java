package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class creates a JPanel which displays the high scores
 * The high scores are read from a text file and added to a table
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class HighScoreView implements ViewPanel{
	private Language currentLanguage = new Language();
	private ResourceBundle messages = currentLanguage.getMessages();
	JPanel panel;
	
	/**
	 * Constructor method for the HighScoreView class
	 * It creates all of the components, sets their properties, layout managers and adds them to containers
	 */
	public HighScoreView() {// Create the components
		JButton mainMenuButton = new JButton();

		ImageIcon logoImage = new ImageIcon(this.getClass().getResource(messages.getString("high_score_title")));
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

		JTable highScores;
		JScrollPane scrollPaneHighScore = null;

		highScores = getHighScore();
		scrollPaneHighScore = new JScrollPane(highScores);
		scrollPaneHighScore.setBorder(new EmptyBorder(0,100,125,100));

		// Specify LayoutManagers
		panel.setLayout(new BorderLayout());
		statsPanel.setLayout(new BorderLayout());
		buttonPanel.setSize(100, 100);

		panel.setBackground(new Color(99, 187, 214));
		buttonPanel.setOpaque(false);
		statsPanel.setOpaque(false);
		
		// Add components to containers
		buttonPanel.add(mainMenuButton);
		statsPanel.add(scrollPaneHighScore, BorderLayout.CENTER);
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
		
	}

	/**
	 * This method reads the text file which stores the high scores and adds the to a table
	 * 
	 * @return JTable table
	 */
	public JTable getHighScore(){
		String[] columnNames = { messages.getString("name"), messages.getString("score")};
		String[][] scores = new String[10][2];
		
		// Read the file
		FileReader fr;
		try {
			FileReader in = new FileReader("./textFiles/highScores.txt"); 
			
			// Wrap FileReader in BufferedReader
			BufferedReader br = new BufferedReader(in);	
			
			String line;
			int x = 0;
			while ((line = br.readLine()) != null ){
				scores[x][0] = line;
				scores[x][1] = br.readLine();
				x++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JTable table = new JTable(scores, columnNames);
		table.setEnabled(false);
		
		return table;
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
