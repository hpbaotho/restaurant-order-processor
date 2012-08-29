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
		this.putClientProperty("JInternalFrame.frameType", "normal");
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

		}
		this.add(Box.createRigidArea(new Dimension(10, 0)));
		// JTextPane list = new JTextPane();
		// StyledDocument doc = list.getStyledDocument();
		// this.addStylesToDocument(doc);

		JPanel other = new JPanel();
		other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));

		Dimension dim = new Dimension(300, 600);
		this.textPane = new JTextPane();
		this.textPane.setMargin(new Insets(10, 10, 10, 10));
		this.textPane.setSize(dim);
		this.doc = this.textPane.getStyledDocument();

		JScrollPane scroll = new JScrollPane(this.textPane);
		scroll.setMaximumSize(dim);
		scroll.setPreferredSize(dim);
		scroll.setBorder(BorderFactory.createLineBorder(Color.black));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.addStylesToDocument(this.doc);

		try {
			this.doc.setParagraphAttributes(0, this.doc.getLength(), this.doc.getStyle("blue"), false);
			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("center"));
			this.doc.insertString(this.doc.getLength(), "FESTA CON NOI 2012\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "Paninoteca Aladino\n", this.doc.getStyle("aladino"));
			this.doc.insertString(this.doc.getLength(), "Ordine n.", this.doc.getStyle("small"));
			this.doc.insertString(this.doc.getLength(), "99", this.doc.getStyle("regular"));

			Date todaysDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yy HH:mm:ss");
			String formattedDate = formatter.format(todaysDate);

			this.doc.insertString(this.doc.getLength(), "                          " + formattedDate + "\n", this.doc.getStyle("small"));

			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("left"));
			this.doc.insertString(this.doc.getLength(), "\nORDINE BAR   ------   n¡ 99\n\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "- Acqua 1/2 litro\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "- Acqua 1/2 litro\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "- Birra bionda media\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "- Coca cola\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "\nORDINE CUCINA   ------   n¡ 99\n\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "- Panino Amore\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "- Panino Proximus\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "\nORDINE FRITTO   ------   n¡ 99\n\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "- Patatine fritte\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "- Patatine fritte\n", this.doc.getStyle("regular"));

			this.doc.insertString(this.doc.getLength(), "\n--------------------------\n", this.doc.getStyle("regular"));
			this.doc.insertString(this.doc.getLength(), "Totale: 26,50 E\n\n", this.doc.getStyle("regular"));

			this.doc.setLogicalStyle(this.doc.getLength(), this.doc.getStyle("center"));
			this.doc.insertString(this.doc.getLength(), "NON FISCALE\n", this.doc.getStyle("small"));
			this.doc.insertString(this.doc.getLength(), " \n", this.doc.getStyle("icon"));
			this.doc.insertString(this.doc.getLength(), "GRAZIE E ARRIVEDERCI\n", this.doc.getStyle("small"));

		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
		other.add(Box.createRigidArea(new Dimension(0, 10)));
		other.add(scroll);
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
			this.doc.insertString(this.doc.getLength(), "Ordine n.", this.doc.getStyle("small"));
			String order = this.core.getActualOrder();
			this.doc.insertString(this.doc.getLength(), order, this.doc.getStyle("regular"));

			Date todaysDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yy HH:mm:ss");
			String formattedDate = formatter.format(todaysDate);

			this.doc.insertString(this.doc.getLength(), "                          " + formattedDate + "\n", this.doc.getStyle("small"));
		} catch (BadLocationException e) {

		}
	}

	private void clearText() {
		try {
			this.doc.remove(0, this.doc.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void appendToText(String s) {
		try {
			this.doc.insertString(this.doc.getLength(), "FESTA CON NOI 2012\n", this.doc.getStyle("regular"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void print(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_print
		// MessageFormat header = createFormat(headerField);
		// MessageFormat footer = createFormat(footerField);
		// boolean interactive = interactiveCheck.isSelected();
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
					if (!ps[i].getName().equals("Officejet 4500 G510g-m [EFF970]")) {
						PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
						attr_set.add(new MediaPrintableArea(5, 5, 90, 200, MediaPrintableArea.MM));
						this.complete = CashDeskFrame.this.textPane.print(this.headerFormat, this.footerFormat, false, ps[i], attr_set, this.interactive);
						this.message = "Printing " + (this.complete ? "complete" : "canceled");
					}
				}
			} catch (PrinterException ex) {
				this.message = "Sorry, a printer error occurred";
			} catch (SecurityException ex) {
				this.message = "Sorry, cannot access the printer due to security reasons";
			}
			return null;
		}

		@Override
		protected void done() {
			Console.println("Stampa completata");
			// message(!this.complete, this.message);
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

	protected void convertButtonActionPerformed(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		this.core.buttonEvent(b.getText());
		Console.println(b.getText());
		//		this.print(evt);
	}

	public void repaintText() {
		this.clearText();
		this.paintHeader();
		Console.println(this.core.getKitchenProd());
		Console.println(this.core.getBarProd());
		Console.println(this.core.getFriedProd());
		Console.println(this.core.getTotal());
	}
}
