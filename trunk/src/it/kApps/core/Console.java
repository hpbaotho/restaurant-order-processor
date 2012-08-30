package it.kApps.core;

import it.kApps.GUI.GUI;

public class Console {

	/**
	 * Progressive number of version
	 */
	protected static final String	CONSOLE_VERSION	= "0.0.1";

	private static final String		NEW_LINE_FEED	= "\n";

	private static GUI				gui;

	public Console(GUI g) {
		gui = g;
	}

	public static void print(Object msg) {
		String text;
		if (msg instanceof String) {
			text = (String) msg;
		} else {
			text = msg.toString();
		}
		if (gui != null && gui.getIntFrames().containsKey("Console")) {
			gui.writeInConsole(text);
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
		print(text + NEW_LINE_FEED);
	}

	public static String center(String msg){
		String result = msg;
		println(""+gui.getConsoleWidth());
		println(""+msg.length());
		int spaces = (gui.getConsoleWidth()-msg.length())/2;
		for(int s = 0; s<spaces; s++){
			result = " "+ result;
		}
		return result;
	}

	public static void setGUI(GUI g) {
		gui = g;
	}
	public static GUI getGUI(){
		return gui;
	}

}
