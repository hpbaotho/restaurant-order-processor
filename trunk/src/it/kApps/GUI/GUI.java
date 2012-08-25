package it.kApps.GUI;

import it.kApps.core.Console;

public class GUI {

	/**
	 * Main Frame
	 */
	private static JFrame mainFrame;

	/**
	 * Frames' container
	 */
	private final JDesktopPane desktop;

	/**
	 * A reference to each frame in the GUI
	 */
	private static Hashtable<String, JInternalFrame> intFrames = new Hashtable<String, JInternalFrame>();

	JTextArea output;
	JScrollPane scrollPane;

	private static int minX = 300;
	private static int minY = 150;

	public GUI(String title) {

		// ########DEBUG
		Console.println("[GUI] Creating a new Frame");
		// #############

		mainFrame = new JFrame(title);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setIconImage();
		mainFrame.setSize(minX, minY);
		// mainFrame.setLocationRelativeTo(null); //USED TO CENTER
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// ########DEBUG
		Console.println("[GUI] Creating the desktop content pane");
		// #############
		this.desktop = new JDesktopPane();
		mainFrame.setContentPane(this.desktop);
		this.createConsolePane();
		this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// ########DEBUG
		Console.println("[GUI] Creating and setting the menu");
		// #############
		Menu menu = new Menu();
		mainFrame.setJMenuBar(menu.createMenuBar());

		mainFrame.setVisible(true);
	}

	private Container createConsolePane() {

		JInternalFrame intFrame = new JInternalFrame("Console", true, true,
				true, true);
		intFrame.putClientProperty("JInternalFrame.frameType", "normal"); // remove
																			// shadows
		intFrame.setSize(100, 50); // min size
		intFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.desktop.add(intFrame);

		// Create a scrolled text area.
		this.output = new JTextArea(8, 70);
		this.output.setFont(new Font("monospaced", Font.PLAIN, 10));
		this.output.setEditable(false);
		this.output.setLineWrap(true);
		this.output.setWrapStyleWord(false);
		this.scrollPane = new JScrollPane(this.output);

		// Add the text area to the content pane.
		intFrame.add(this.scrollPane);
		intFrame.pack();
		intFrame.setVisible(true);
		intFrames.put("Console", intFrame);

		return this.desktop;
	}

	public static Hashtable<String, JInternalFrame> getIntFrameList() {
		return intFrames;
	}

	public void writeInConsole(String msg) {
		this.output.append(msg);
		this.output.setCaretPosition(this.output.getDocument().getLength());
	}
}
