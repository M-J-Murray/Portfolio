package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class creates the panel which displays the options menu, it allows the users to access key bindings and change the current langauge.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class OptionsView implements ViewPanel {
	private JPanel panel; 
	private Language currentLanguage;
	
	private JComboBox language;
	
	/**
	 * Constructor method for the OptionsView class
	 * It creates all of the components, sets their properties, layout managers and adds them to containers
	 */
	public OptionsView() {
		// Set the current language
		currentLanguage = new Language();
		ResourceBundle messages = currentLanguage.getMessages();
		
		final int blankSpace = 200;  // Blank border at the edges of the panel
		
		//Options for the JComboBoxes
		String[] languages = {"English", "Polish", "Zulu", "Afrikaans", "Xhosa"};
		
		// Create the components
		ImageIcon headerImage = new ImageIcon(this.getClass().getResource(messages.getString("options_title")));
		JLabel header = new JLabel(headerImage);
		
		JButton backButton = new JButton();
		JButton keyBindingsButton = new JButton();
		
		language = new JComboBox(languages);
		updateComboBoxView();
		JLabel languageLabel = new JLabel(messages.getString("language"));
		
		// Set the properties of the components	
		backButton.setText(messages.getString("back"));
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		backButton.setMinimumSize(new Dimension(75, 50));
		backButton.setPreferredSize(new Dimension(75, 50));
		backButton.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		
		keyBindingsButton.setText(messages.getString("key_bindings"));
		keyBindingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		keyBindingsButton.setMinimumSize(new Dimension(75, 50));
		keyBindingsButton.setPreferredSize(new Dimension(75, 50));
		keyBindingsButton.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		
		// Create containers to hold the components
		JPanel optionsPanel = new JPanel();
		panel = new JPanel();
		JPanel twoWidePanel = new JPanel();
		
		panel.setBackground(new Color(99, 187, 214));
		optionsPanel.setOpaque(false);
		twoWidePanel.setOpaque(false);

		// Specify LayoutManagers
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		panel.setLayout(new BorderLayout());
		
		GridLayout newGameLayout = new GridLayout(0, 2);
		twoWidePanel.setLayout(newGameLayout);
		
		optionsPanel.setBorder(new EmptyBorder(0, blankSpace, blankSpace/2, blankSpace));
		
		// Add components to containers
		twoWidePanel.add(languageLabel);
		twoWidePanel.add(language);
		optionsPanel.add(twoWidePanel);
		optionsPanel.add(keyBindingsButton);
		optionsPanel.add(backButton);
		panel.add(header, BorderLayout.NORTH);
		panel.add(optionsPanel, BorderLayout.SOUTH);
		
		// Events
		keyBindingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				KeyBindingsView kb = new KeyBindingsView();
				kb.addToJFrame();
				kb.setVisible();
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
		
		// When the user changes a language using the drop down menu save the language to a text file
		// This keeps the language change even after closing and reopening the game
		language.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintWriter writer;
				try {
					writer = new PrintWriter("./textFiles/currentLanguage.txt");
					// Shorten each language to it's corresponding language "code"
					if(language.getSelectedItem() == "English"){
						writer.print("en");
					} else if(language.getSelectedItem() == "Polish"){
						writer.print("pl");
					} else if(language.getSelectedItem() == "Zulu"){
						writer.print("zu");
					} else if(language.getSelectedItem() == "Afrikaans"){
						writer.print("af");
					} else if(language.getSelectedItem() == "Xhosa"){
						writer.print("xh");
					}
					writer.close();
				} catch (FileNotFoundException e1) {
					System.out.println("No file found.");
				}
				
				// Update the combo box to show the current language
				updateComboBoxView();
				
				// Reload the screen so it's displayed in the chosen language
				panel.setVisible(false);
				OptionsView op = new OptionsView();
				op.addToJFrame();
				op.setVisible();
				
			}
		});
	}
	
	public void updateComboBoxView(){
		// Set the combo box to display current language
		String tmpLng = currentLanguage.getLanguage();
		if(tmpLng.equals("en")){
			language.setSelectedItem("English");
		} else if(tmpLng.equals("pl")) {
			language.setSelectedItem("Polish");
		} else if(tmpLng.equals("zu")) {
			language.setSelectedItem("Zulu");
		} else if(tmpLng.equals("af")) {
			language.setSelectedItem("Afrikaans");
		} else if(tmpLng.equals("xh")) {
			language.setSelectedItem("Xhosa");
		}
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
