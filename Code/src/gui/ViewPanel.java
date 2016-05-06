package gui;

import javax.swing.*;

/**
 * This interface provides a template for the views/frames of each window in the GUI.
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public interface ViewPanel {

	// Abstract method declaration
	public JPanel getJPanel();
	public void setVisible();
	public void addToJFrame();	
}
