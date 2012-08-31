package it.kApps.GUI;

import it.kApps.core.CashDesk;
import it.kApps.core.Console;
import it.kApps.core.Database;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.print.PrintService;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * The main gui
 * 
 * @author Gianmarco Laggia
 * 
 */
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

	/**
	 * The console
	 */
	private JTextArea									output;
	/**
	 * the scroll pane in the console frame.
	 */
	private JScrollPane									scrollPane;

	/**
	 * Selection of the printer
	 */
	private static JComboBox							cashDesk;
	/**
	 * Selection of the printer
	 */
	private static JComboBox							kitchen;

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the window
	 */
	public GUI(String title) {

		// ########DEBUG
		Console.println("[GUI] Creating a new Frame");
		// #############

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			// ###### DEBUG ######
			Console.println("[GUI] Impossible to set the crossPlatform Look and Feel");
			// ###################
		}

		mainFrame = new JFrame(title);
		mainFrame.setUndecorated(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// mainFrame.setIconImage();
		mainFrame.setSize(300, 150);
		// mainFrame.setLocationRelativeTo(null); //USED TO CENTER
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

		// ########DEBUG
		Console.println("[GUI] Creating the desktop content pane");
		// #############

		this.desktop = new JDesktopPane();
		mainFrame.setContentPane(this.desktop);
		this.desktop.setLayout(null);

		// Default starts the CashDesk unit
		this.createCashDeskPane();
		this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// ########DEBUG
		Console.println("[GUI] Creating and setting the menu");
		// #############
		Menu menu = new Menu(this);
		mainFrame.setJMenuBar(menu.createMenuBar());

		mainFrame.setVisible(true);
	}

	/**
	 * Used to create the <code>InternalFrame</code> of the <code>Console</code> class.
	 * 
	 * @return the container
	 */
	public Container createConsolePane() {

		if (GUI.intFrames.containsKey("Console")) {
			intFrames.get("Console").setVisible(true);
			return null;
		}
		JInternalFrame intFrame = new JInternalFrame("Console", true, true, true, true);
		intFrame.putClientProperty("JInternalFrame.frameType", "normal"); // remove
		// shadows
		intFrame.setBounds(1050, 0, 1024, 768); // min size
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
		// intFrame.pack();
		intFrame.setVisible(true);
		intFrames.put("Console", intFrame);

		return this.desktop;
	}

	/**
	 * Used to append a string in the console panel.
	 * 
	 * @param msg
	 */
	public void writeInConsole(String msg) {
		if (this.output == null) {
			// ###### DEBUG ######
			Console.println("[GUI] Error, tried to write in the console, but it is not ready yet.");
			// ###################
			return;
		}
		this.output.append(msg);
		this.output.setCaretPosition(this.output.getDocument().getLength());
	}

	/**
	 * Get the width of the console panel. TODO not used well
	 * 
	 * @return the width
	 */
	public int getConsoleWidth(){
		this.output.repaint();
		return this.output.getColumns();
	}

	/**
	 * Create the <code>InternalFrame</code>, used to set printers property.
	 * 
	 * @return container
	 */
	public Container createPrinterPane() {

		if(GUI.intFrames.containsKey("Printers")){
			intFrames.get("Printers").setVisible(true);
			return null;
		}
		JInternalFrame intFrame = new JInternalFrame("Printers", true, true, true, true);
		intFrame.setSize(100, 50); // min size
		intFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.desktop.add(intFrame);

		JLabel lbl = new JLabel("Select the two default printers");
		lbl.setFont(new Font("Sans serif", Font.PLAIN, 18));
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		lbl.setMaximumSize(new Dimension(400, 30));
		lbl.setVisible(true);

		JLabel lbl2 = new JLabel("CashDesk PRINTER");
		lbl2.setFont(new Font("Sans serif", Font.PLAIN, 12));
		lbl2.setHorizontalAlignment(SwingConstants.CENTER);
		lbl2.setMaximumSize(new Dimension(400, 30));
		lbl2.setVisible(true);

		JLabel lbl3 = new JLabel("Kitchen PRINTER");
		lbl3.setFont(new Font("Sans serif", Font.PLAIN, 12));
		lbl3.setHorizontalAlignment(SwingConstants.CENTER);
		lbl3.setMaximumSize(new Dimension(400, 30));
		lbl3.setVisible(true);

		Database.connect();
		ResultSet rs = Database.listValuesByName("printerDesk", "settings");
		String actual = "";
		try {
			while (rs.next()) {
				actual = rs.getString(3);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[CashDesk] Error in handling database values");
			// ###################
		}
		cashDesk = new JComboBox();
		int selected = 0;
		PrintService[] ps = PrinterJob.lookupPrintServices();
		for (int i = 0; i < ps.length; i++) {
			if (actual.equals(ps[i].getName())) {
				selected = i;
			}
			cashDesk.addItem(ps[i].getName());
		}
		cashDesk.setSelectedIndex(selected);

		selected = 0;
		kitchen = new JComboBox();
		for (int i = 0; i < ps.length; i++) {
			if (actual.equals(ps[i].getName())) {
				selected = i;
			}
			kitchen.addItem(ps[i].getName());
		}
		kitchen.setSelectedIndex(selected);

		JButton btt = new JButton("OK");
		Dimension d = new Dimension(150, 50);
		btt.setPreferredSize(d);
		btt.setMaximumSize(d);
		btt.setSize(d);
		btt.addActionListener(new ActionListener() {
			@SuppressWarnings("synthetic-access")
			// TODO ocio
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				GUI.setPrinters();
				GUI.intFrames.get("Printers").setVisible(false);
			}
		});

		intFrame.getContentPane().setLayout(new BoxLayout(intFrame.getContentPane(), BoxLayout.LINE_AXIS));
		JPanel p = new JPanel();
		intFrame.add(Box.createRigidArea(new Dimension(10, 0)));
		intFrame.add(p);
		intFrame.add(Box.createRigidArea(new Dimension(10, 0)));
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(lbl);
		p.add(Box.createRigidArea(new Dimension(0, 30)));
		p.add(lbl2);
		p.add(Box.createRigidArea(new Dimension(0, 5)));
		p.add(cashDesk);
		p.add(Box.createRigidArea(new Dimension(0, 20)));
		p.add(lbl3);
		p.add(Box.createRigidArea(new Dimension(0, 5)));
		p.add(kitchen);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(btt);
		p.add(Box.createRigidArea(new Dimension(0, 10)));
		p.add(Box.createVerticalGlue());

		intFrame.pack();
		intFrame.setVisible(true);
		intFrames.put("Printers", intFrame);

		mainFrame.repaint();

		return this.desktop;
	}

	protected static void setPrinters() {
		Database.connect();
		String actual = (String) cashDesk.getSelectedItem();
		Boolean updated = Database.updateTable("settings", "printerDesk", actual);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE DESK PRINTER");
		}
		actual = (String) kitchen.getSelectedItem();
		updated = Database.updateTable("settings", "printerKitchen", actual);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE KITCHEN PRINTER");
		}
		Database.disconnect();

	}

	/**
	 * Create the <code>InternalFrame</code> for the CashDesk utility.
	 * 
	 * @return container
	 */
	public Container createCashDeskPane() {

		CashDesk cd = new CashDesk();
		CashDeskFrame intFrame = new CashDeskFrame(cd);
		this.desktop.add(intFrame);

		intFrame.setVisible(true);
		intFrames.put("CashDesk", intFrame);

		mainFrame.repaint();

		return this.desktop;
	}

	/**
	 * Return the hashtable that contains a link to each <code>InternalFrame</code> in the window.
	 * 
	 * @return the hashtable.
	 */
	public Hashtable<String, JInternalFrame> getIntFrames() {
		return intFrames;
	}

	/**
	 * Return the frame, if is contained.
	 * 
	 * @param name
	 *            the name of the frame
	 * @return the frame
	 */
	public JInternalFrame getFrame(String name) {
		if (intFrames.containsKey(name)) {
			return intFrames.get(name);
		}else{
			return null;
		}
	}

	/**
	 * Handles the event from the menu
	 * 
	 * @param e
	 *            the event
	 */
	public void menuAction(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		String action = source.getText();
		if ("Set Printers".equals(action)) {
			this.createPrinterPane();
		}
		if ("Total Today".equals(action)) {
			Database.connect();
			ResultSet rs = Database.listValuesByName("totalToday", "settings");
			int actual = 0;
			try {
				while (rs.next()) {
					actual = rs.getInt(3);
				}
			} catch (SQLException ex) {
				// ###### DEBUG ######
				Console.println("[CashDesk] Error in handling database values");
				// ###################
			}
			JOptionPane.showMessageDialog(null, "Il totale di oggi è: " + (actual / 100.) + "0 Euro");
			Database.disconnect();
		}
		if ("Total Ever".equals(action)) {
			Database.connect();
			ResultSet rs = Database.listValuesByName("totalEver", "settings");
			int actual = 0;
			try {
				while (rs.next()) {
					actual = rs.getInt(3);
				}
			} catch (SQLException ex) {
				// ###### DEBUG ######
				Console.println("[CashDesk] Error in handling database values");
				// ###################
			}
			JOptionPane.showMessageDialog(null, "Il totale dall'inizio della festa è: " + (actual / 100.) + "0 Euro");
			Database.disconnect();
		}
		if ("Start new day".equals(action)) {
			int result = JOptionPane.showConfirmDialog((Component) null, "Sicuro? il totale di ieri e il numero comande viene resettato!", "alert",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == 0) {
				Database.connect();
				int actual = 0;
				Boolean updated = Database.updateTable("settings", "totalToday", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT RESET THE TOTAL. ");
				}
				actual = 1;
				updated = Database.updateTable("settings", "ordersToday", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT RESET THE TOTAL. ");
				}
				Database.disconnect();
			}
		}
		if ("Reset all".equals(action)) {
			int result = JOptionPane.showConfirmDialog((Component) null, "Sicuro? Tutto viene resettato!", "alert",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == 0) {
				Database.connect();
				int actual = 0;
				Boolean updated = Database.updateTable("settings", "totalToday", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT RESET THE TOTAL. ");
				}
				actual = 1;
				updated = Database.updateTable("settings", "ordersToday", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT RESET THE TOTAL. ");
				}
				actual = 0;
				updated = Database.updateTable("settings", "totalEver", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT RESET THE TOTAL EVER. ");
				}
				Database.disconnect();
			}
		}
		if ("An item in the submenu".equals(action)) {
			String result = JOptionPane.showInputDialog((Component) null, "Sicuro? Tutto viene resettato!", "alert", JOptionPane.OK_CANCEL_OPTION);
			if (result != "") {
				Database.connect();
				ResultSet rs = Database.listValuesByName("totalToday", "settings");
				int actual = 0;
				try {
					while (rs.next()) {
						actual = rs.getInt(3);
					}
				} catch (SQLException ex) {
					// ###### DEBUG ######
					Console.println("[CashDesk] Error in handling database values");
					// ###################
				}
				actual -= Integer.parseInt(result) * 100;
				Boolean updated = Database.updateTable("settings", "totalToday", "" + actual);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE TOTAL. " + Integer.parseInt(result) * 100 + " euro");
				}

				rs = Database.listValuesByName("totalEver", "settings");
				int ever = 0;
				try {
					while (rs.next()) {
						ever = rs.getInt(3);
					}
				} catch (SQLException ex) {
					// ###### DEBUG ######
					Console.println("[CashDesk] Error in handling database values");
					// ###################
				}
				ever -= Integer.parseInt(result) * 100;
				updated = Database.updateTable("settings", "totalEver", "" + ever);
				if (!updated) {
					Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE TOTAL OF THE DAY. " + Integer.parseInt(result) * 100 + " euro");
				}
				Database.disconnect();
			}
		}

	}

	/**
	 * The desk printer
	 * 
	 * @return a string with the name.
	 */
	public static String getDeskPrinter() {
		return (String) cashDesk.getSelectedItem();
	}

	/**
	 * The kitchen printer
	 * 
	 * @return a string with the name.
	 */
	public static String getKitchenPrinter() {
		return (String) kitchen.getSelectedItem();
	}
}
