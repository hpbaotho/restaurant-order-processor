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
	 * The main method
	 * 
	 * @param arg
	 *            standard.
	 */
	public static void main(String[] arg) {

		// USE MAC MENUBAR
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		// new Console();
		new Database("xdb", "testdb");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI(TITLE);
			}
		});
	}
}
