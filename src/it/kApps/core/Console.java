package it.kApps.core;

import javax.swing.JOptionPane;

import it.kApps.GUI.GUI;

public class Console {

	/**
	 * Progressive number of version
	 */
	protected static final String	CONSOLE_VERSION	= "0.0.1";
	

	public static void print(Object msg) {
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
	
	public static void printWarning(Object msg){
		String text = "WARNING! ";
		if (msg instanceof String) {
			text += (String) msg;
		} else {
			text += msg.toString();
		}
		JOptionPane.showMessageDialog(null, text,"WARNING",JOptionPane.WARNING_MESSAGE);
		print(text + PagodaConstants.NEW_LINE_FEED);
	}

	public static String center(String msg){
		String result = msg;
		println(""+GUI.getConsoleWidth());
		println(""+msg.length());
		int spaces = (GUI.getConsoleWidth()-msg.length())/2;
		for(int s = 0; s<spaces; s++){
			result = " "+ result;
		}
		return result;
	}
}
