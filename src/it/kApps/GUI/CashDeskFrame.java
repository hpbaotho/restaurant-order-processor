package it.kApps.GUI;

import it.kApps.core.Console;
import it.kApps.core.Database;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class CashDeskFrame extends JInternalFrame {

	private Hashtable<String, JButton>	buttons;
	private final Database					d;

	public CashDeskFrame() {
		super();
		this.setIconifiable(true);
		this.setClosable(true);
		this.setResizable(true);
		this.setTitle("Cash Desk v3.0");
		// this.setSize(100, 50);
		this.putClientProperty("JInternalFrame.frameType", "normal");// removes the shadows
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.d = new Database("xdb", "testdb");
		this.d.start();
		try {
			this.d.connect();

			ArrayList<String> cat = this.d.listTableValues("categories");

			FlowLayout pan = new FlowLayout();
			pan.setAlignment(FlowLayout.TRAILING);
			this.setLayout(null);// ########################

			for (int i = 0; i < cat.size(); i++) {
				// GridLayout grid = new GridLayout(1, 1, 10, 20);
				// JPanel p = new JPanel();
				// p.setBorder(BorderFactory.createLineBorder(Color.black));
				// p.setLayout(grid);
				// this.add(p);
				try {
					Console.println(cat.toString());
					ArrayList<String> bts = this.d.listTableValues("products WHERE CAT=" + i);
					// if (grid.getRows() < bts.size()) {
					// grid.setRows(bts.size());
					// }
					JLabel lbl = new JLabel(cat.get(i));
					this.add(lbl);
					lbl.setFont(new Font("Broadway", Font.BOLD, 24));
					Dimension dl = lbl.getPreferredSize();
					lbl.setBounds(10 + 200 * i + 5 * i, 5, 200, dl.height);
					lbl.setHorizontalAlignment(SwingConstants.CENTER);
					lbl.setVisible(true);
					for (int j = 0; j < bts.size(); j++) {
						JButton btt = new JButton(bts.get(j));
						btt.setBounds(10 + 200 * i + 5 * i, 5 + dl.height + 5 + 40 * j, 200, 40);
						btt.setVisible(true);
						this.add(btt); // was p.add....
						btt.addActionListener(new java.awt.event.ActionListener() {
							@Override
							public void actionPerformed(java.awt.event.ActionEvent evt) {
								System.out.println(evt.getSource());
								// this.convertButtonActionPerformed(evt);
							}
						});
						if (this.getHeight() < (btt.getY() + btt.getHeight() + 5)) {
							this.setSize(this.getWidth(), btt.getY() + btt.getHeight() + 27);
						}
						if (this.getWidth() < (btt.getX() + btt.getWidth())) {
							this.setSize(btt.getX() + btt.getWidth() + 5, this.getHeight());
						}

					}
				} catch (Exception e) {
				}
			}

		} catch (SQLException e) {
			// DEBUG
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO DEBUG
			e.printStackTrace();
		}

	}
}
