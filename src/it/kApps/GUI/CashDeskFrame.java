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
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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
		this.putClientProperty("JInternalFrame.frameType", "normal");// removes the shadows
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.d = new Database("xdb", "testdb");
		this.d.start();
		try {
			this.d.connect();

			ArrayList<String> cat = this.d.listTableValues("categories");

			this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.LINE_AXIS));
			this.add(Box.createRigidArea(new Dimension(5, 0)));

			for (int i = 0; i < cat.size(); i++) {
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
				p.setVisible(true);
				this.add(p);
				p.add(Box.createRigidArea(new Dimension(0, 5)));
				try {
					Console.println(cat.toString());
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
						Dimension d = new Dimension(200, 40);
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
					p.add(Box.createVerticalGlue());
					this.add(Box.createRigidArea(new Dimension(5, 0)));
				} catch (Exception e) {
				}
			}
			this.pack();


		} catch (SQLException e) {
			// DEBUG
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO DEBUG
			e.printStackTrace();
		}

	}

	protected void convertButtonActionPerformed(ActionEvent evt) {
		JButton b = (JButton) evt.getSource();
		Console.println(b.getText());
	}
}
