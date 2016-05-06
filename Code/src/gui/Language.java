package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class responsible for detecting the current language
 * 
 * ADDING A NEW LANGUAGE CHECKLIST:
 * 1. language_{}.{}.properties
 * 2. setCountry(); add to the if statement
 * 3. in OptionsView:
 *  a. language.addActionListener(); add to the if statement
 *  b. updateComboBoxView(); add to the if statement
 *  c. add to languages array
 * 4. Add images
 * 
 * @author COMP7
 * @version v1.0, 26/04/2016
 */
public class Language {
	protected String language;
	protected String country = "GB";
	protected Locale currentLocale;
	protected ResourceBundle messages;

	/**
	 * Constructor for the class Language
	 */
	public Language() {
		// Set the language and the country
		setLanguage();
		setCountry();
		
		// Set the locale and messages
		currentLocale = new Locale(language, country);
		messages = ResourceBundle.getBundle("language", currentLocale);
	}

	/**
	 * Return the current language
	 * 
	 * @return String language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Return the current resource bundle
	 * 
	 * @return ResourceBundle messages
	 */
	public ResourceBundle getMessages() {
		return messages;
	}

	/**
	 * This method reads the text file which holds the current language and based on that text file it sets the game's language
	 */
	public void setLanguage() {
		// This will reference one line at a time
		String line = null;

		// Read the language from the file
		try {
			// Read the file			
			FileReader in = new FileReader("./textFiles/currentLanguage.txt"); 
			
			// Wrap FileReader in BufferedReader
			BufferedReader br = new BufferedReader(in);

			// Set the language based on the file
			line = br.readLine();
			language = line;

			// Clear the line
			line = null;

			// Close file
			br.close();

		}

		catch (FileNotFoundException ex) {
			System.out.println("No file found.");
		}

		catch (IOException ex) {
			System.out.println("Error reading the file.");
		}
	}

	/**
	 * This method sets the current country based on the current language
	 */
	public void setCountry() {
		if (language == null) {
			language = "en";
			country = "GB";
		} else if (language.equals("en")) {
			country = "GB";
		} else if (language.equals("pl")) {
			country = "PL";
		} else if (language.equals("zu")) {
			country = "ZU";
		} else if (language.equals("af")) {
			country = "AF";
		} else if (language.equals("xh")) {
			country = "XH";
		}
	}

}