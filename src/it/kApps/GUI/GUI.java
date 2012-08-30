package it.kApps.GUI;

import it.kApps.core.CashDesk;
import it.kApps.core.Console;
import it.kApps.core.Database;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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
	private static JComboBox							cashDesk;
	private static JComboBox							kitchen;

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
		FlowLayout fl = new FlowLayout();
		this.desktop.setLayout(null);

		this.createCashDeskPane();
		this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// ########DEBUG
		Console.println("[GUI] Creating and setting the menu");
		// #############
		Menu menu = new Menu(this);
		mainFrame.setJMenuBar(menu.createMenuBar());

		mainFrame.setVisible(true);
	}

	// private void convertButtonActionPerformed(java.awt.event.ActionEvent evt)
	// {//GEN-FIRST:event_convertButtonActionPerformed
	// //Parse degrees Celsius as a double and convert to Fahrenheit
	// Console.println(Console.center("schiacciato"));
	// }

	public Container createConsolePane() {

		if (GUI.intFrames.containsKey("Console")) {
			intFrames.get("Console").setVisible(true);
			return null;
		}
		JInternalFrame intFrame = new JInternalFrame("Console", true, true, true, true);
		intFrame.putClientProperty("JInternalFrame.frameType", "normal"); // remove
		// shadows
		intFrame.setBounds(1050, 0, 150, 50); // min size
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

		return this.desktop;
	}

	public Container createIngredientPane() {

		if (GUI.intFrames.containsKey("Ingredients")) {
			if (intFrames.get("Ingredients").isVisible()) {
				return null;
			} else {
				intFrames.get("Ingredients").setVisible(false);
				this.desktop.remove(intFrames.get("Ingredients"));
				intFrames.remove("Ingredients");
				return null;
			}
		}
		JInternalFrame intFrame = new JInternalFrame("Ingredients", true, true, true, true);

		ArrayList<JTextField> tfAdd = new ArrayList<JTextField>();
		ArrayList<JTextField> tfDelete = new ArrayList<JTextField>();

		intFrame.setBounds(800, 300, 150, 50); // min size
		intFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.desktop.add(intFrame);

		// Create a scrolled text area.
		intFrame.setLayout(new BoxLayout(intFrame, BoxLayout.PAGE_AXIS));
		intFrame.add(Box.createRigidArea(new Dimension(0, 10)));
		intFrame.add(new JLabel("Aggiungi"));
		intFrame.add(Box.createRigidArea(new Dimension(0, 10)));
		for (int i = 0; i < 4; i++) {
			JTextField tef = new JTextField();
			tfAdd.add(tef);
			intFrame.add(tef);
		}
		intFrame.add(Box.createRigidArea(new Dimension(0, 10)));
		intFrame.add(new JLabel("Aggiungi"));
		intFrame.add(Box.createRigidArea(new Dimension(0, 10)));
		for (int i = 0; i < 4; i++) {
			JTextField tef = new JTextField();
			tfDelete.add(tef);
			intFrame.add(tef);
		}
		intFrame.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton btt = new JButton("Ket");
		Dimension d = new Dimension(60, 40);
		btt.setBackground(Color.WHITE);
		btt.setOpaque(true);
		btt.setPreferredSize(d);
		btt.setMaximumSize(d);
		btt.setSize(d);
		btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// GUI.this.convertButtonActionPerformed(evt);
			}
		});

		// Add the text area to the content pane.
		intFrame.add(this.scrollPane);
		intFrame.pack();
		intFrame.setVisible(true);
		intFrames.put("Console", intFrame);

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

		cashDesk = new JComboBox();
		PrinterJob pj = PrinterJob.getPrinterJob();
		PrintService[] ps = PrinterJob.lookupPrintServices();
		for (int i = 0; i < ps.length; i++) {
			cashDesk.addItem(ps[i].getName());
		}

		kitchen = new JComboBox();
		for (int i = 0; i < ps.length; i++) {
			kitchen.addItem(ps[i].getName());
		}

		JButton btt = new JButton("OK");
		Dimension d = new Dimension(150, 50);
		btt.setPreferredSize(d);
		btt.setMaximumSize(d);
		btt.setSize(d);
		btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
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

	public Container createCashDeskPane() {

		CashDesk cd = new CashDesk();
		CashDeskFrame intFrame = new CashDeskFrame(cd);
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
			JOptionPane.showMessageDialog(null, "Il totale di oggi �: " + (actual / 100.) + "0 Euro");
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
			JOptionPane.showMessageDialog(null, "Il totale dall'inizio della festa �: " + (actual / 100.) + "0 Euro");
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

	}

	public static String getDeskPrinter() {
		return (String) cashDesk.getSelectedItem();
	}

	public static String getKitchenPrinter() {
		return (String) kitchen.getSelectedItem();
	}
}
