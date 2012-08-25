package it.kApps;

import it.kApps.GUI.GUI;
import it.kApps.core.Console;

public class Pagoda {
	/**
	 * The name of this program.
	 */
	private static final String TITLE = "Cassa Pagoda - vers. 3.0.0";

	/**
	 * The GUI
	 */
	private static GUI g;

	/**
	 * The main method
	 * 
	 * @param arg
	 *            standard.
	 */
	public static void main(String[] arg) {

		// USE MAC MENUBAR
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		new Console(null);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Pagoda.g = new GUI(TITLE);
				Console.setGUI(g);
			}
		});
	}
}
