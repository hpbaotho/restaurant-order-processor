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
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
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
import javax.swing.SwingWorker;
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

	private Hashtable<String, JButton>	buttons;
	private final JTextPane				textPane;
	private final JComboBox					whereTo;
	private final JCheckBox				free;
	private final StyledDocument doc;
	private final CashDesk				core;

	/**
	 * Main constructor, create the frame with each button for each product.
	 */
	public CashDeskFrame(CashDesk core) {
		super();
		this.core = core;
		core.setGui(this);
		this.setIconifiable(true);
		this.setClosable(true);
		this.setResizable(true);
		this.setTitle("Cash Desk v3.0");
		// this.putClientProperty("JInternalFrame.frameType", "normal");
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		new Database("xdb", "testdb");
		Database.start();
		Database.connect();

		ArrayList<String> cat = Database.listTableValues("categories");

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.LINE_AXIS));
		this.add(Box.createRigidArea(new Dimension(10, 0)));

		for (int i = 0; i < cat.size(); i++) {
			JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
			p.setVisible(true);
			this.add(p);
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
				Dimension d = new Dimension(160, 35);
				btt.setPreferredSize(d);
				btt.setMaximumSize(d);
				btt.setSize(d);
				p.add(Box.createRigidArea(new Dimension(0, 5)));
				p.add(btt);
				btt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						CashDeskFrame.this.convertButtonActionPerformed(evt);
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
						Dimension d = new Dimension(160, 35);
						btt.setPreferredSize(d);
						btt.setMaximumSize(d);
						btt.setSize(d);
						p.add(Box.createRigidArea(new Dimension(0, 5)));
						p.add(btt);
						btt.addActionListener(new ActionListener() {
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

		}
		this.add(Box.createRigidArea(new Dimension(10, 0)));

		JPanel other = new JPanel();
		other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));

		Dimension dim = new Dimension(300, 500);
		this.textPane = new JTextPane();
		this.textPane.setMargin(new Insets(10, 10, 10, 10));
		this.textPane.setSize(dim);
		this.doc = this.textPane.getStyledDocument();
		this.addStylesToDocument(this.doc);

		JScrollPane scroll = new JScrollPane(this.textPane);
		scroll.setMaximumSize(dim);
		scroll.setPreferredSize(dim);
		scroll.setBorder(BorderFactory.createLineBorder(Color.black));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);


		this.whereTo = new JComboBox();
		this.whereTo.addItem("SELEZIONA IL TAVOLO");
		for (int i = 1; i < 30; i++) {
			this.whereTo.addItem("Tavolo n." + i);
		}
		this.whereTo.addItem("AL BANCO");
		this.whereTo.setSelectedIndex(0);
		this.whereTo.setMaximumSize(new Dimension(300, 20));
		this.whereTo.setBackground(Color.WHITE);
		this.whereTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.this.whereTo(evt);
			}
		});


		other.add(Box.createRigidArea(new Dimension(0, 10)));
		other.add(this.whereTo);
		other.add(Box.createRigidArea(new Dimension(0, 10)));
		other.add(scroll);
		other.add(Box.createRigidArea(new Dimension(0, 10)));

		this.free = new JCheckBox("Gratis");
		JPanel freeAndOther = new JPanel();
		freeAndOther.setLayout(new BoxLayout(freeAndOther, BoxLayout.LINE_AXIS));

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
				CashDeskFrame.this.convertButtonActionPerformed(evt);
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
				CashDeskFrame.this.convertButtonActionPerformed(evt);
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
				CashDeskFrame.this.convertButtonActionPerformed(evt);
			}
		});
		JButton remove = new JButton("Togli");
		d = new Dimension(80, 40);
		remove.setMargin(new Insets(2, 2, 2, 2));
		remove.setBackground(Color.WHITE);
		remove.setOpaque(true);
		remove.setPreferredSize(d);
		remove.setMaximumSize(d);
		remove.setSize(d);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.this.convertButtonActionPerformed(evt);
			}
		});

		freeAndOther.add(this.free);
		freeAndOther.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndOther.add(ketchup);
		freeAndOther.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndOther.add(mayonnaise);
		freeAndOther.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndOther.add(hot);
		freeAndOther.add(Box.createRigidArea(new Dimension(10, 0)));
		freeAndOther.add(remove);

		other.add(freeAndOther);
		other.add(Box.createRigidArea(new Dimension(0, 10)));

		JButton confirm = new JButton("Conferma");
		d = new Dimension(200, 60);
		confirm.setBackground(new Color(66, 226, 92));
		confirm.setOpaque(true);
		confirm.setPreferredSize(d);
		confirm.setMaximumSize(d);
		confirm.setSize(d);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CashDeskFrame.this.convertButtonActionPerformed(evt);
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
				CashDeskFrame.this.convertButtonActionPerformed(evt);
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
				CashDeskFrame.this.convertButtonActionPerformed(evt);
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
		this.add(other);
		this.add(Box.createRigidArea(new Dimension(10, 0)));
		this.add(Box.createHorizontalGlue());
		this.pack();

		Database.disconnect();

	}

	private void paintHeader() {
		try {
			this.doc.setParagraphAttributes(0, this.doc.getLength(), this.doc.getStyle("blue"), false);
			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("center"));
			this.doc.insertString(this.doc.getLength(), "FESTA CON NOI 2012\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "Paninoteca Aladino\n", this.doc.getStyle("aladino"));
		} catch (BadLocationException e) {

		}
	}

	private void paintBody() {
		try {
			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("left"));

			String order = this.core.getActualOrder();
			Double total = this.core.getTotal() / 100.;

			ArrayList<String> val = this.core.getBarProd();
			if (val.size() > 0) {
				this.doc.insertString(this.doc.getLength(), "\nORDINE BIBITE - N. " + order, this.doc.getStyle("regular"));
				this.doc.insertString(this.doc.getLength(), "\n\t" + this.whereTo.getSelectedItem() + "\n\n", this.doc.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					this.doc.insertString(this.doc.getLength(), "- " + val.get(i) + "\n", this.doc.getStyle("regular"));
				}
			}
			val = this.core.getKitchenProd();
			if (val.size() > 0) {
				this.doc.insertString(this.doc.getLength(), "\n------------------------------", this.doc.getStyle("regular"));
				this.doc.insertString(this.doc.getLength(), "\nORDINE CUCINA - N. " + order, this.doc.getStyle("regular"));
				this.doc.insertString(this.doc.getLength(), "\n\t" + this.whereTo.getSelectedItem() + "\n\n", this.doc.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					this.doc.insertString(this.doc.getLength(), "- " + val.get(i) + "\n", this.doc.getStyle("regular"));
				}
			}
			val = this.core.getFriedProd();
			if (val.size() > 0) {
				this.doc.insertString(this.doc.getLength(), "\n------------------------------", this.doc.getStyle("regular"));
				this.doc.insertString(this.doc.getLength(), "\nORDINE FRITTO e DOLCI - N. " + order, this.doc.getStyle("regular"));
				this.doc.insertString(this.doc.getLength(), "\n\t" + this.whereTo.getSelectedItem() + "\n\n", this.doc.getStyle("regular"));
				for (int i = 0; i < val.size(); i++) {
					this.doc.insertString(this.doc.getLength(), "- " + val.get(i) + "\n", this.doc.getStyle("regular"));
				}
			}
			this.doc.insertString(this.doc.getLength(), "\n\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "Totale: " + total + "0 E\n\n", this.doc.getStyle("regular"));
		} catch (BadLocationException e) {

		}
	}

	public void paintFooter() {
		try {
			Date todaysDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			String formattedDate = formatter.format(todaysDate);
			String order = this.core.getActualOrder();

			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("center"));
			this.doc.insertString(this.doc.getLength(), "ARRIVEDERCI E GRAZIE\n", this.doc.getStyle("small"));
			this.doc.insertString(this.doc.getLength(), " \n", this.doc.getStyle("icon"));
			this.doc.insertString(this.doc.getLength(), formattedDate + " - N¡: " + order + "\n", this.doc.getStyle("small"));

			this.doc.insertString(this.doc.getLength(), "-NON FISCALE-\n", this.doc.getStyle("small"));
		} catch (BadLocationException e) {

		}
	}

	private void clearText() {
		try {
			this.doc.remove(0, this.doc.getLength());
		} catch (BadLocationException e) {
			// ###### DEBUG ######
			Console.println("[CashDeskFrame] Error during clearing the texts");
			// ###################
		}
	}

	public void print() {
		boolean background = false;// backgroundCheck.isSelected();

		// PrintingTask task = new PrintingTask(header, footer, interactive);
		PrintingTask task = new PrintingTask(null, null, true);
		if (background) {
			task.execute();
		} else {
			task.run();
		}
	}// GEN-LAST:event_print

	private class PrintingTask extends SwingWorker<Object, Object> {
		private final MessageFormat	headerFormat;
		private final MessageFormat	footerFormat;
		private final boolean		interactive;
		private volatile boolean	complete	= false;
		private volatile String		message;

		public PrintingTask(MessageFormat header, MessageFormat footer, boolean interactive) {
			this.headerFormat = header;
			this.footerFormat = footer;
			this.interactive = interactive;
		}

		@Override
		protected Object doInBackground() {
			try {
				PrinterJob pj = PrinterJob.getPrinterJob();
				PrintService[] ps = PrinterJob.lookupPrintServices();
				for (int i = 0; i < ps.length; i++) {
					if (ps[i].getName().equals(GUI.getDeskPrinter())) {
						PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
						attr_set.add(new MediaPrintableArea(5, 5, 90, 200, MediaPrintableArea.MM));
						CashDeskFrame.this.textPane.print(this.headerFormat, this.footerFormat, false, ps[i], attr_set, this.interactive);
					}
				}
				for (int i = 0; i < ps.length; i++) {
					if (ps[i].getName().equals(GUI.getKitchenPrinter())) {
						CashDeskFrame.this.onlyBody();
						PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
						attr_set.add(new MediaPrintableArea(5, 5, 90, 200, MediaPrintableArea.MM));
						CashDeskFrame.this.textPane.print(this.headerFormat, this.footerFormat, false, ps[i], attr_set, this.interactive);
					}
				}
			} catch (PrinterException ex) {
				this.message = "Sorry, a printer error occurred";
			} catch (SecurityException ex) {
				this.message = "Sorry, cannot access the printer due to security reasons";
			}
			return null;
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
		StyleConstants.setFontSize(s, 14);

		s = doc.addStyle("blue", regular);
		StyleConstants.setForeground(s, Color.BLUE);

		s = doc.addStyle("right", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_RIGHT);

		// s = doc.addStyle("left", regular);
		// StyleConstants.setAlignment(s, StyleConstants.ALIGN_LEFT);

		s = doc.addStyle("center", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);

		s = doc.addStyle("aladino", regular);
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

		s = doc.addStyle("icon", regular);
		StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
		ImageIcon logoIcon = new ImageIcon("images/logo2.gif", "Logo");
		if (logoIcon != null) {
			StyleConstants.setIcon(s, logoIcon);
		}
	}

	public void onlyBody() {
		this.clearText();
		if (this.core.getProds() != null && this.core.getProds().size() != 0) {
			this.paintBody();
		}

	}

	protected void convertButtonActionPerformed(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		this.core.buttonEvent(b.getText(), this.free.isSelected());
	}

	protected void whereTo(ActionEvent evt) {
		this.core.setWhereTo(this.whereTo.getSelectedIndex());
		this.repaintText();
	}

	public void resetWhereTo() {
		this.whereTo.setSelectedIndex(0);
	}

	public void repaintText() {
		this.clearText();
		if (this.core.getProds() != null && this.core.getProds().size() != 0) {
			this.paintHeader();
			this.paintBody();
			this.paintFooter();
		}
	}

}