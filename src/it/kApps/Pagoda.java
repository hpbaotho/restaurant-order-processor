package it.kApps;

import it.kApps.GUI.GUI;
import it.kApps.core.Console;
import it.kApps.core.Database;

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
	@SuppressWarnings("unused")
	public static void main(String[] arg) {

		// USE MAC MENUBAR
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		new Console(null);
		new Database("xdb", "testdb");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Pagoda.g = new GUI(TITLE);
				Console.setGUI(g);
			}
		});
	}
}
