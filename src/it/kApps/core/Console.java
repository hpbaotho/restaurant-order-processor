package it.kApps.core;

import it.kApps.GUI.GUI;

public class Console {

	/**
	 * Progressive number of version
	 */
	protected static final String CONSOLE_VERSION = "0.0.1";

	private static final String NEW_LINE_FEED = "\n";

	private static GUI gui;

	public Console(GUI g) {
		gui = g;
	}

	public static void print(String msg) {
		if (gui != null) {
			gui.writeInConsole(msg);
		} else {
			System.out.print(msg);
		}
	}

	public static void println(String msg) {
		print(msg + NEW_LINE_FEED);
	}

	public static void setGUI(GUI g) {
		gui = g;
	}

}
