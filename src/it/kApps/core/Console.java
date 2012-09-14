package it.kApps.core;

import javax.swing.JOptionPane;

import it.kApps.GUI.GUI;

/**
 * Class that handle Console events.<br>
 * Is needed a <code>GUI</code> to have the console working. This class handle only the technical features, not the
 * graphics.
 * 
 * @author Gianmarco Laggia
 * 
 */
public class Console {
	
	/**
	 * Progressive number of version
	 */
	protected static final String	CONSOLE_VERSION	= "0.1.6";
	
	private static int				verbosity		= 0;
	
	public static void print(Object msg) {
		if (checkVerbosity(1)) {
			return;
		}
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		if (GUI.getIntFrames().containsKey("Console")) {
			GUI.writeInConsole(text);
		} else {
			System.out.print(text);
		}
	}
	
	public static void println(Object msg) {
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		print(text + PagodaConstants.NEW_LINE_FEED);
	}
	
	// FIXME deve essere testo grigio questo!! IL COLORE è importante
	public static void printDebug(Object msg) {
		if (checkVerbosity(0)) {
			return;
		}
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		if (GUI.getIntFrames().containsKey("Console")) {
			GUI.writeInConsole(text);
		} else {
			System.out.print(text);
		}
	}
	
	public static void printlnDebug(Object msg) {
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		printDebug(text + PagodaConstants.NEW_LINE_FEED);
	}
	
	// FIXME deve essere testo colorato!!
	public static void printWarning(Object msg) {
		String text = "WARNING! ";
		if (msg instanceof String) {
			text += (String) msg;
		} else {
			text += msg.toString();
		}
		JOptionPane.showMessageDialog(null, text, "WARNING", JOptionPane.WARNING_MESSAGE);
		print(text + PagodaConstants.NEW_LINE_FEED);
	}
	
	/**
	 * Used to set the verbosity of the <code>Console</code>.
	 * 
	 * @param n
	 *            The value of the verbosity to set.<br>
	 *            0: Debug<br>
	 *            1: Medium<br>
	 *            2: Quiet<br>
	 */
	public static void setVerbosity(int n) {
		verbosity = n;
	}
	
	/**
	 * Return the actual value of the verbosity.
	 * 
	 * @return the value, between 0 and 2
	 */
	public static int getVerbosity() {
		return verbosity;
	}
	
	/**
	 * Check if the verbosity is more than the value passed.
	 * 
	 * @param value
	 *            The value to confront
	 * @return if verbosity is more true, if less false.
	 */
	private static boolean checkVerbosity(int value) {
		return verbosity > value;
	}
	
	/**
	 * Used to center the text in the console.
	 * 
	 * @param msg
	 *            The text to center
	 * @return the text centered
	 */
	public static String center(Object msg) {
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		String result = text;
		println("" + GUI.getConsoleWidth());
		println("" + result.length());
		int spaces = (GUI.getConsoleWidth() - result.length() - 2) / 2;
		for (int s = 0; s < spaces; s++) {
			result = " " + result;
		}
		return result;
	}
	
	/**
	 * Create a new border for a table.
	 * 
	 * @return A string like "+---------+" as large as the <code>Console</code> is.
	 */
	public static String tableLine() {
		String line = PagodaConstants.PLUS;
		int spaces = GUI.getConsoleWidth() - 4;
		for (int s = 0; s < spaces; s++) {
			line += PagodaConstants.MINUS;
		}
		line += PagodaConstants.PLUS;
		return line;
	}
	
	/**
	 * Create a new border for a table with a title centered in it.
	 * 
	 * @param text
	 *            The text that has to be centered
	 * 
	 * @return A string like "+--- Text ---+" as large as the <code>Console</code> is.
	 */
	public static String tableLine(Object text) {
		String msg;
		if (text instanceof String) {
			msg = (String) text;
		} else {
			msg = text.toString();
		}
		String line = PagodaConstants.PLUS;
		int dots = (GUI.getConsoleWidth() - 4 - msg.length());
		double left = Math.ceil(dots / 2);
		for (int s = 0; s < left - 1; s++) {
			line += PagodaConstants.MINUS;
		}
		line += PagodaConstants.SPACE;
		line += msg;
		line += PagodaConstants.SPACE;
		
		while (line.length() <= GUI.getConsoleWidth() - 4) {
			line += PagodaConstants.MINUS;
		}
		line += PagodaConstants.PLUS;
		return line;
	}
	
	/**
	 * Create a new empty row in the table without upper and lower border.
	 * 
	 * @return A string like "|          |" as large as the <code>Console</code> is.
	 */
	public static String tableColumn() {
		String line = PagodaConstants.VERTICAL_BAR;
		int spaces = (GUI.getConsoleWidth() - line.length());
		for (int s = 0; s < spaces - 3; s++) {
			line += PagodaConstants.SPACE;
		}
		line += PagodaConstants.VERTICAL_BAR;
		return line;
	}
	
	/**
	 * Create a new empty row in the table without upper and lower border.
	 * 
	 * @param text
	 *            The text that has to be centered
	 * 
	 * @return A string like "|   Text   |" as large as the <code>Console</code> is.
	 */
	public static String tableColumn(Object text) {
		String msg;
		if (text instanceof String) {
			msg = (String) text;
		} else {
			msg = text.toString();
		}
		String line = PagodaConstants.VERTICAL_BAR;
		int dots = (GUI.getConsoleWidth() - 4 - msg.length());
		double left = Math.ceil(dots / 2);
		for (int s = 0; s < left; s++) {
			line += PagodaConstants.SPACE;
		}
		
		line += msg;
		
		while (line.length() <= GUI.getConsoleWidth() - 4) {
			line += PagodaConstants.SPACE;
		}
		line += PagodaConstants.VERTICAL_BAR;
		return line;
	}
}
