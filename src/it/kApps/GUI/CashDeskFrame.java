package it.kApps.GUI;

import it.kApps.core.Console;
import it.kApps.core.Database;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class CashDeskFrame extends JInternalFrame {

	private Hashtable<String, JButton>	buttons;
	private final Database					d;

	/**
	 * Main constructor, create the frame with each button for each product.
	 */
	public CashDeskFrame() {
		super();
		this.setIconifiable(true);
		this.setClosable(true);
		this.setResizable(true);
		this.setTitle("Cash Desk v3.0");
		this.putClientProperty("JInternalFrame.frameType", "normal");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.d = new Database("xdb", "testdb");
		this.d.start();
		try {
			this.d.connect();

			ArrayList<String> cat = this.d.listTableValues("categories");

			this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
			this.add(Box.createRigidArea(new Dimension(10, 0)));

			for (int i = 0; i < cat.size(); i++) {
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
				p.setVisible(true);
				this.add(p);
				p.add(Box.createRigidArea(new Dimension(0, 5)));
				try {

					ArrayList<String> bts = this.d.listTableValues("products WHERE CAT=" + i);
					JLabel lbl = new JLabel(cat.get(i));
					p.add(lbl);
					lbl.setFont(new Font("Broadway", Font.BOLD, 24));
					// lbl.setBounds(10 + 200 * i + 5 * i, 5, 200, dl.height);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setMaximumSize(new Dimension(400, 30));
					lbl.setVisible(true);
					for (int j = 0; j < bts.size(); j++) {
						JButton btt = new JButton(bts.get(j));
						Dimension d = new Dimension(150, 40);
						btt.setPreferredSize(d);
						btt.setMaximumSize(d);
						btt.setSize(d);
						// btt.setBounds(10 + 200 * i + 5 * i, 5 + dl.height + 5 + 40 * j, 200, 40);
						p.add(Box.createRigidArea(new Dimension(0, 5)));
						p.add(btt); // was p.add....
						btt.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								CashDeskFrame.this.convertButtonActionPerformed(evt);
							}
						});
						// if (this.getHeight() < (btt.getY() + btt.getHeight() + 5)) {
						// this.setSize(this.getWidth(), btt.getY() + btt.getHeight() + 27);
						// }
						// if (this.getWidth() < (btt.getX() + btt.getWidth())) {
						// this.setSize(btt.getX() + btt.getWidth() + 5, this.getHeight());
						// }
					}
					p.add(Box.createRigidArea(new Dimension(0, 5)));
					if (bts.size() < 12) {
						ArrayList<String> bts2 = this.d.listTableValues("products WHERE CAT=" + (i + 1));
						if (bts2.size() + bts.size() < 12) {
							i++;
							p.add(Box.createRigidArea(new Dimension(0, 20)));
							JLabel lbl2 = new JLabel(cat.get(i));
							p.add(lbl2);
							lbl2.setFont(new Font("Broadway", Font.BOLD, 24));
							lbl2.setHorizontalAlignment(SwingConstants.CENTER);
							lbl2.setMaximumSize(new Dimension(400, 30));
							lbl2.setVisible(true);
							for (int j = 0; j < bts.size(); j++) {
								JButton btt = new JButton(bts.get(j));
								Dimension d = new Dimension(150, 40);
								btt.setPreferredSize(d);
								btt.setMaximumSize(d);
								btt.setSize(d);
								p.add(Box.createRigidArea(new Dimension(0, 5)));
								p.add(btt);
								btt.addActionListener(new java.awt.event.ActionListener() {
									@Override
									public void actionPerformed(java.awt.event.ActionEvent evt) {
										CashDeskFrame.this.convertButtonActionPerformed(evt);
									}
								});
							}
							p.add(Box.createRigidArea(new Dimension(0, 5)));
						}
					}
					p.add(Box.createVerticalGlue());
					this.add(Box.createRigidArea(new Dimension(10, 0)));

				} catch (Exception e) {
				}
			}
			this.add(Box.createRigidArea(new Dimension(10, 0)));
			// JTextPane list = new JTextPane();
			// StyledDocument doc = list.getStyledDocument();
			// this.addStylesToDocument(doc);
			String newline = "\n";
			String[] initString = {
					"This is an editable JTextPane, ", // regular
					"another ", // italic
					"styled ", // bold
					"text ", // small
					"component, ", // large
					"which supports embedded components..." + newline,// regular
					" " + newline, // button
					"...and embedded icons..." + newline, // regular
					" ", // icon
					newline + "JTextPane is a subclass of JEditorPane that " + "uses a StyledEditorKit and StyledDocument, and provides "
					+ "cover methods for interacting with those objects." };

			String[] initStyles = { "regular", "italic", "bold", "small", "large", "regular", "button", "regular", "icon", "regular" };

			JTextPane textPane = new JTextPane();
			StyledDocument doc = textPane.getStyledDocument();
			this.addStylesToDocument(doc);

			try {
				for (int e = 0; e < initString.length; e++) {
					doc.insertString(doc.getLength(), initString[e], doc.getStyle(initStyles[e]));
				}
			} catch (BadLocationException ble) {
				System.err.println("Couldn't insert initial text into text pane.");
			}
			this.add(textPane);
			this.pack();


		} catch (SQLException e) {
			// DEBUG
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO DEBUG
			e.printStackTrace();
		}

	}

	protected void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

		s = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		ImageIcon pigIcon = createImageIcon("images/spiox.gif", "a cute pig");
		if (pigIcon != null) {
			StyleConstants.setIcon(s, pigIcon);
		}

		s = doc.addStyle("button", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = CashDeskFrame.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(path, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	protected void convertButtonActionPerformed(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		Console.println(b.getText());
	}
}
