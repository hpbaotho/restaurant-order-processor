package it.kApps.GUI;

import it.kApps.core.CashDesk;
import it.kApps.core.Console;
import it.kApps.core.Database;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Main Class for the GUI of the <code>CashDesk</code> Class.
 * 
 * @author Gianmarco Laggia
 * 
 */
public class CashDeskFrame extends JInternalFrame {

	/**
	 * Generated serialVersionUID
	 */
	private static final long	serialVersionUID	= 1849321179964730683L;
	private static JTextPane				textPane;
	private static JComboBox					WHERE_TO;
	private static JLabel TOTAL;
	private static JCheckBox				free;
	private static StyledDocument DOC;

	/**
	 * Main constructor, create the frame with each button for each product.
	 */
	public CashDeskFrame() {
		super();
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		setTitle("Cash Desk v3.0");
		// putClientProperty("JInternalFrame.frameType", "normal");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		Database.start();
		Database.connect();

		ArrayList<String> cat = Database.listTableValues("categories");

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		add(Box.createRigidArea(new Dimension(10, 0)));

		for (int i = 0; i < cat.size(); i++) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
			p.setVisible(true);
			add(p);
			p.add(Box.createRigidArea(new Dimension(0, 5)));

			ArrayList<String> bts = Database.listTableValues("products WHERE CAT=" + i);
			JLabel lbl = new JLabel(cat.get(i));
			p.add(lbl);
			lbl.setFont(new Font("Broadway", Font.BOLD, 24));
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setMaximumSize(new Dimension(400, 30));
			lbl.setVisible(true);
			for (int j = 0; j < bts.size(); j++) {
				JButton btt = new JButton(bts.get(j));
				Dimension d = new Dimension(130, 35);
				btt.setPreferredSize(d);
				btt.setMaximumSize(d);
				btt.setSize(d);
				btt.setMargin(new Insets(2, 2, 2, 2));
				p.add(Box.createRigidArea(new Dimension(0, 5)));
				p.add(btt);
				btt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						CashDeskFrame.convertButtonActionPerformed(evt);
					}
				});
			}
			p.add(Box.createRigidArea(new Dimension(0, 5)));
			if (bts.size() < 12) {
				ArrayList<String> bts2 = Database.listTableValues("products WHERE CAT=" + (i + 1));
				if (bts2.size() + bts.size() < 12) {
					i++;
					p.add(Box.createRigidArea(new Dimension(0, 20)));
					JLabel lbl2 = new JLabel(cat.get(i));
					p.add(lbl2);
					lbl2.setFont(new Font("Broadway", Font.BOLD, 24));
					lbl2.setHorizontalAlignment(SwingConstants.CENTER);
					lbl2.setMaximumSize(new Dimension(400, 30));
					lbl2.setVisible(true);
					for (int j = 0; j < bts2.size(); j++) {
						JButton btt = new JButton(bts2.get(j));
						Dimension d = new Dimension(130, 35);
						btt.setPreferredSize(d);
						btt.setMaximumSize(d);
						btt.setSize(d);
						btt.setMargin(new Insets(2, 2, 2, 2));
						p.add(Box.createRigidArea(new Dimension(0, 5)));
						p.add(btt);
						btt.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								CashDeskFrame.convertButtonActionPerformed(evt);
							}
						});
					}
					p.add(Box.createRigidArea(new Dimension(0, 5)));
				}
			}
			p.add(Box.createVerticalGlue());
			add(Box.createRigidArea(new Dimension(10, 0)));

		}
		add(Box.createRigidArea(new Dimension(10, 0)));

		JPanel other = new JPanel();
		other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));

		Dimension dim = new Dimension(250, 350);
		textPane = new JTextPane();
		textPane.setMargin(new Insets(10, 10, 10, 10));
		textPane.setSize(dim);
		DOC = textPane.getStyledDocument();
		addStylesToDocument(DOC);

		JScrollPane scroll = new JScrollPane(textPane);
		scroll.setMaximumSize(dim);
		scroll.setPreferredSize(dim);
		scroll.setBorder(BorderFactory.createLineBorder(Color.black));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


		WHERE_TO = new JComboBox();
		WHERE_TO.addItem("SELEZIONA IL TAVOLO");
		for (int i = 1; i < 30; i++) {
			WHERE_TO.addItem("Tavolo n." + i);
		}
		WHERE_TO.addItem("AL BANCO");
		WHERE_TO.setSelectedIndex(0);
		WHERE_TO.setMaximumSize(new Dimension(250, 20));
		WHERE_TO.setBackground(Color.WHITE);
		WHERE_TO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.whereTo(evt);
			}
		});

		TOTAL = new JLabel("0,00");
		TOTAL.setFont(new Font("Sans serif",Font.PLAIN,80));
		dim = new Dimension(250,90);
		TOTAL.setMaximumSize(dim);
		TOTAL.setPreferredSize(dim);
		TOTAL.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel totalP = new JPanel();
		totalP.setLayout(new BoxLayout(totalP,BoxLayout.LINE_AXIS));
		totalP.add(TOTAL);
		
		other.add(Box.createRigidArea(new Dimension(0, 10)));
		other.add(WHERE_TO);
		other.add(Box.createRigidArea(new Dimension(0, 5)));
		other.add(scroll);
		other.add(Box.createRigidArea(new Dimension(0, 5)));
		other.add(totalP);
		other.add(Box.createRigidArea(new Dimension(0, 10)));

		free = new JCheckBox("Gratis");
		JPanel freeAndAddons = new JPanel();
		freeAndAddons.setLayout(new BoxLayout(freeAndAddons, BoxLayout.LINE_AXIS));
		JPanel salse = new JPanel();
		freeAndAddons.setLayout(new BoxLayout(freeAndAddons, BoxLayout.LINE_AXIS));

		JButton ketchup = new JButton("Ket");
		Dimension d = new Dimension(60, 40);
		ketchup.setBackground(Color.WHITE);
		ketchup.setOpaque(true);
		ketchup.setPreferredSize(d);
		ketchup.setMaximumSize(d);
		ketchup.setSize(d);
		ketchup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		JButton mayonnaise = new JButton("May");
		d = new Dimension(60, 40);
		mayonnaise.setMargin(new Insets(2, 2, 2, 2));
		mayonnaise.setBackground(Color.WHITE);
		mayonnaise.setOpaque(true);
		mayonnaise.setPreferredSize(d);
		mayonnaise.setMaximumSize(d);
		mayonnaise.setSize(d);
		mayonnaise.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		JButton hot = new JButton("Temp.Amb.");
		d = new Dimension(80, 40);
		hot.setMargin(new Insets(2, 2, 2, 2));
		hot.setBackground(Color.WHITE);
		hot.setOpaque(true);
		hot.setPreferredSize(d);
		hot.setMaximumSize(d);
		hot.setSize(d);
		hot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});
		JButton addRemove = new JButton("Variaz.");
		d = new Dimension(80, 40);
		addRemove.setMargin(new Insets(2, 2, 2, 2));
		addRemove.setBackground(Color.WHITE);
		addRemove.setOpaque(true);
		addRemove.setPreferredSize(d);
		addRemove.setMaximumSize(d);
		addRemove.setSize(d);
		addRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});
		JButton pink = new JButton("S.Rosa");
		d = new Dimension(80, 40);
		pink.setMargin(new Insets(2, 2, 2, 2));
		pink.setBackground(Color.WHITE);
		pink.setOpaque(true);
		pink.setPreferredSize(d);
		pink.setMaximumSize(d);
		pink.setSize(d);
		pink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		freeAndAddons.add(free);
		freeAndAddons.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndAddons.add(addRemove);
		freeAndAddons.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndAddons.add(hot);

		salse.add(ketchup);
		salse.add(Box.createRigidArea(new Dimension(10, 0)));
		salse.add(mayonnaise);
		salse.add(Box.createRigidArea(new Dimension(10, 0)));
		salse.add(pink);

		other.add(freeAndAddons);
		other.add(Box.createRigidArea(new Dimension(0, 5)));
		other.add(salse);
		other.add(Box.createRigidArea(new Dimension(0, 5)));
		

		JButton confirm = new JButton("Conferma");
		d = new Dimension(160, 60);
		confirm.setBackground(new Color(66, 226, 92));
		confirm.setOpaque(true);
		confirm.setPreferredSize(d);
		confirm.setMaximumSize(d);
		confirm.setSize(d);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		JButton undo = new JButton("<");
		d = new Dimension(40, 60);
		undo.setMargin(new Insets(2, 2, 2, 2));
		undo.setBackground(new Color(255, 207, 207));
		undo.setOpaque(true);
		undo.setPreferredSize(d);
		undo.setMaximumSize(d);
		undo.setSize(d);
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		JButton cancel = new JButton("X");
		d = new Dimension(40, 60);
		cancel.setMargin(new Insets(2, 2, 2, 2));
		cancel.setBackground(new Color(255, 85, 85));
		cancel.setOpaque(true);
		cancel.setPreferredSize(d);
		cancel.setMaximumSize(d);
		cancel.setSize(d);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.convertButtonActionPerformed(evt);
			}
		});

		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.add(confirm);
		buttons.add(Box.createRigidArea(new Dimension(5, 0)));
		buttons.add(undo);
		buttons.add(Box.createRigidArea(new Dimension(5, 0)));
		buttons.add(cancel);

		other.add(buttons);
		other.add(Box.createRigidArea(new Dimension(0, 10)));
		other.add(Box.createVerticalGlue());
		add(other);
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(Box.createHorizontalGlue());
		pack();

		Database.disconnect();

	}

	private static void paintHeader() {
		try {
			//DOC.setParagraphAttributes(0, DOC.getLength(), DOC.getStyle("blue"), false);
			DOC.setLogicalStyle(DOC.getLength(), DOC.getStyle("center"));
			DOC.insertString(DOC.getLength(), "FESTA CON NOI 2012\n", DOC.getStyle("regular"));
			DOC.insertString(DOC.getLength(), "Paninoteca Aladino\n", DOC.getStyle("aladino"));
		} catch (BadLocationException e) {

		}
	}

	private static void paintBody() {
		try {
			DOC.setLogicalStyle(DOC.getLength(), DOC.getStyle("left"));

			String order = CashDesk.getActualOrder();

			ArrayList<String> val = CashDesk.getBarProd();
			if (val.size() > 0) {
				DOC.insertString(DOC.getLength(), "\nORDINE BIBITE - N. " + order, DOC.getStyle("regular"));
				DOC.insertString(DOC.getLength(), "\n\t" + WHERE_TO.getSelectedItem() + "\n\n", DOC.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					DOC.insertString(DOC.getLength(), "- " + val.get(i) + "\n", DOC.getStyle("regular"));
				}
			}
			val = CashDesk.getKitchenProd();
			if (val.size() > 0) {
				DOC.insertString(DOC.getLength(), "\n------------------------------", DOC.getStyle("regular"));
				DOC.insertString(DOC.getLength(), "\nORDINE CUCINA - N. " + order, DOC.getStyle("regular"));
				DOC.insertString(DOC.getLength(), "\n\t" + WHERE_TO.getSelectedItem() + "\n\n", DOC.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					DOC.insertString(DOC.getLength(), "- " + val.get(i) + "\n", DOC.getStyle("regular"));
				}
			}
			val = CashDesk.getFriedProd();
			if (val.size() > 0) {
				DOC.insertString(DOC.getLength(), "\n------------------------------", DOC.getStyle("regular"));
				DOC.insertString(DOC.getLength(), "\nORDINE FRITTO e DOLCI - N. " + order, DOC.getStyle("regular"));
				DOC.insertString(DOC.getLength(), "\n\t" + WHERE_TO.getSelectedItem() + "\n\n", DOC.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					DOC.insertString(DOC.getLength(), "- " + val.get(i) + "\n", DOC.getStyle("regular"));
				}
			}
			DOC.insertString(DOC.getLength(), "\n\n", DOC.getStyle("regular"));
		} catch (BadLocationException e) {

		}
	}

	public static void paintFooter() {
		try {
			Double total = CashDesk.getTotal() / 100.;
			DOC.insertString(DOC.getLength(), "Totale: " + total + "0 E\n\n", DOC.getStyle("regular"));
			Date todaysDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			String formattedDate = formatter.format(todaysDate);
			String order = CashDesk.getActualOrder();

			DOC.setLogicalStyle(DOC.getLength(), DOC.getStyle("center"));
			DOC.insertString(DOC.getLength(), "ARRIVEDERCI E GRAZIE\n", DOC.getStyle("small"));
			DOC.insertString(DOC.getLength(), " \n", DOC.getStyle("icon"));
			DOC.insertString(DOC.getLength(), formattedDate + " - N: " + order + "\n", DOC.getStyle("small"));

			DOC.insertString(DOC.getLength(), "-NON FISCALE-\n", DOC.getStyle("small"));
		} catch (BadLocationException e) {

		}
	}

	private static void clearText() {
		try {
			DOC.remove(0, DOC.getLength());
		} catch (BadLocationException e) {
			// ###### DEBUG ######
			Console.println("[CashDeskFrame] Error during clearing the texts");
			// ###################
		}
	}

	public static void print() {
		boolean background = false;// backgroundCheck.isSelected();

		// PrintingTask task = new PrintingTask(header, footer, interactive);
		PrintingTask task = new PrintingTask(null, null, true);
		if (background) {
			task.execute();
		} else {
			task.run();
		}
	}// GEN-LAST:event_print

	public static JTextPane getTextPane(){
		return textPane;
	}

	protected static void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = DOC.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = DOC.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = DOC.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = DOC.addStyle("small", regular);
		StyleConstants.setFontSize(s, 10);

		s = DOC.addStyle("large", regular);
		StyleConstants.setFontSize(s, 14);

		s = DOC.addStyle("blue", regular);
		StyleConstants.setForeground(s, Color.BLUE);

		s = DOC.addStyle("right", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_RIGHT);

		// s = DOC.addStyle("left", regular);
		// StyleConstants.setAlignment(s, StyleConstants.ALIGN_LEFT);

		s = DOC.addStyle("center", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);

		s = DOC.addStyle("aladino", regular);
		try {
			Font.createFont(Font.TRUETYPE_FONT, new File("res/aladdin.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StyleConstants.setFontFamily(s, "Aladdin");
		StyleConstants.setFontSize(s, 25);

		s = DOC.addStyle("icon", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		ImageIcon logoIcon = new ImageIcon("images/logo2.gif", "Logo");
		if (logoIcon != null) {
			StyleConstants.setIcon(s, logoIcon);
		}
	}

	public static void onlyBody() {
		clearText();
		if (CashDesk.getProds() != null && CashDesk.getProds().size() != 0) {
			paintBody();
		}

	}

	protected static void convertButtonActionPerformed(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		CashDesk.buttonEvent(b.getText(), free.isSelected());
	}

	protected static void whereTo(ActionEvent evt) {
		CashDesk.setWhereTo(WHERE_TO.getSelectedIndex());
		repaintText();
	}

	public static void resetWhereTo() {
		WHERE_TO.setSelectedIndex(0);
	}
	
	public static void paintTotal(){
		TOTAL.setText((CashDesk.getTotal() / 100.+"0").replace('.', ','));
	}

	public static void repaintText() {
		clearText();
		if (CashDesk.getProds() != null && CashDesk.getProds().size() != 0) {
			paintTotal();
			paintHeader();
			paintBody();
			paintFooter();
		}else{
			TOTAL.setText("0,00");
		}
	}

}