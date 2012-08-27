package it.kApps.GUI;

import it.kApps.core.Console;

import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class GUI {

	/**
	 * Main Frame
	 */
	private static JFrame								mainFrame;

	/**
	 * Frames' container
	 */
	private final JDesktopPane							desktop;

	/**
	 * A reference to each frame in the GUI
	 */
	private static Hashtable<String, JInternalFrame>	intFrames	= new Hashtable<String, JInternalFrame>();

	JTextArea											output;
	JScrollPane											scrollPane;

	private static int									minX		= 300;
	private static int									minY		= 150;

	public GUI(String title) {

		// ########DEBUG
		Console.println("[GUI] Creating a new Frame");
		// #############

		mainFrame = new JFrame(title);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setIconImage();
		mainFrame.setSize(minX, minY);
		// mainFrame.setLocationRelativeTo(null); //USED TO CENTER
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

		// ########DEBUG
		Console.println("[GUI] Creating the desktop content pane");
		// #############

		this.desktop = new JDesktopPane();
		mainFrame.setContentPane(this.desktop);

		JButton btt = new JButton("button");
		btt.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GUI.this.convertButtonActionPerformed(evt);
			}
		});
		mainFrame.add(btt);
		btt.setSize(100,100);
		btt.setVisible(true);

		this.createConsolePane();
		this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// ########DEBUG
		Console.println("[GUI] Creating and setting the menu");
		// #############
		Menu menu = new Menu(this);
		mainFrame.setJMenuBar(menu.createMenuBar());

		mainFrame.setVisible(true);
	}

	private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
		//Parse degrees Celsius as a double and convert to Fahrenheit
		Console.println(Console.center("schiacciato"));
	}

	public Container createConsolePane() {

		JInternalFrame intFrame = new JInternalFrame("Console", true, true, true, true);
		intFrame.putClientProperty("JInternalFrame.frameType", "normal"); // remove
		// shadows
		intFrame.setSize(100, 50); // min size
		intFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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

		//		mainFrame.repaint();

		return this.desktop;
	}

	public void writeInConsole(String msg) {
		this.output.append(msg);
		this.output.setCaretPosition(this.output.getDocument().getLength());
	}

	public int getConsoleWidth(){
		this.output.repaint();
		return this.output.getColumns();
	}

	public Container createCochiPane() {

		System.out.println("called2");
		JInternalFrame intFrame = new JInternalFrame("Cochi", true, true, true, true);
		intFrame.putClientProperty("JInternalFrame.frameType", "normal"); // remove
		// shadows
		intFrame.setSize(100, 50); // min size
		intFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
		intFrames.put("Cochi", intFrame);

		mainFrame.repaint();

		return this.desktop;
	}

	public Container createCashDeskPane() {

		System.out.println("called");
		CashDeskFrame intFrame = new CashDeskFrame();
		this.desktop.add(intFrame);

		// intFrame.pack();
		intFrame.setVisible(true);
		intFrames.put("CashDesk", intFrame);

		mainFrame.repaint();

		return this.desktop;
	}
	public Hashtable<String, JInternalFrame> getIntFrames() {
		return intFrames;
	}

	public JInternalFrame getFrame(String name) {
		if (intFrames.containsKey(name)) {
			return intFrames.get(name);
		}else{
			return null;
		}
	}
}
