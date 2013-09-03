package it.kApps.GUI;

import it.kApps.core.Database;
import it.kApps.core.Product;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 * Dialog to manage the services.
 * 
 * @author Gianmarco Laggia
 * 
 */
public class ManageProducts extends JDialog {
	
	private static final long			serialVersionUID	= 1L;
	private JList<Product>				list;
	private DefaultListModel<Product>	dlm;
	
	/**
	 * Shows a dialog to manage the Products.
	 * 
	 * @param window
	 */
	public ManageProducts(JFrame window) {
		super(window);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		add(Box.createHorizontalStrut(20));
		add(main);
		add(Box.createHorizontalStrut(20));
		
		main.add(Box.createVerticalStrut(20));
		
		JLabel title = new JLabel("Mod/Canc Servizi");
		title.setFont(new Font("SansSerif", Font.BOLD, 20));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		
		main.add(inPanel(title, 300));
		
		dlm = new DefaultListModel<Product>();
		Database.connect();
		ArrayList<Product> prod = Database.listProducts();
		Database.disconnect();
		
		for (int i = 0; i < prod.size(); i++) {
			dlm.addElement(prod.get(i));
		}
		list = new JList<Product>(dlm);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setFont(new Font("SansSerif", Font.PLAIN, 16));
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(400, 200));
		listScroller.setMinimumSize(new Dimension(400, 200));
		
		main.add(Box.createVerticalStrut(10));
		main.add(listScroller);
		main.add(Box.createVerticalStrut(5));
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		
		// JButton delete = new JButton("Elimina");
		// delete.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent evt) {
		// Database.connect();
		// if (!list.isSelectionEmpty()) {
		// if (JOptionPane.showConfirmDialog(null, "Sicuro che vuoi eliminare il servizio?",
		// "Eliminazione di un servizio", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		// Product removed = list.getSelectedValue();
		// if (Database.deleteProduct(removed.getId())) {
		// dlm.remove(list.getSelectedIndex());
		// }
		// list.revalidate();
		// list.repaint();
		// GUI.showMessage("Servizio eliminato correttamente");
		// GUI.repaintProducts();
		// }
		// }
		// Database.disconnect();
		// }
		// });
		JButton undo = new JButton("Esci");
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dispose();
				// GUI.repaintProducts();
			}
		});
		JButton edit = new JButton("Modifica");
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Database.connect();
				if (!list.isSelectionEmpty()) {
					Product selected = list.getSelectedValue();
					ArrayList<String> result = GUI.showProductDialog(selected);
					if (!"".equals(result.get(0)) && !"".equals(result.get(1)) && !"".equals(result.get(2)) && !"".equals(result.get(3))) {
						String name = result.get(1);
						int cat = -1;
						String ingr = result.get(3);
						int price = -1;
						// BigDecimal price = null;
						int id = 0;
						try {
							id = Integer.parseInt(result.get(0));
							cat = Integer.parseInt(result.get(2));
							price = Integer.parseInt(result.get(4));
							// price = BigDecimal.valueOf(Double.parseDouble(result.get(2).replace(',', '.')));
						} catch (Exception ex) {
							ex.printStackTrace();
							// GUI.showErrorMessage("Error while parsing numbers!\nI campi prezzo e categoria devono essere numeri\nNew service NOT added");
							return;
						}
						Database.connect();
						if (Database.editProduct(id, name, cat, ingr, price, 0, 0)) {
							int index = list.getSelectedIndex();
							Product r = dlm.remove(index);
							r.setName(name);
							r.setPrice(price);
							dlm.add(index, r);
							list.revalidate();
							list.repaint();
							GUI.showMessage("Servizio modificato correttamente");
							// GUI.repaintProducts();
						}
						Database.disconnect();
					} else {
						// GUI.showErrorMessage("All field necessary!\nNew Service NOT added");
						return;
					}
				}
				Database.disconnect();
			}
		});
		
		buttons.add(undo);
		buttons.add(Box.createHorizontalStrut(5));
		buttons.add(edit);
		buttons.add(Box.createHorizontalStrut(5));
		// buttons.add(delete);
		
		main.add(buttons);
		
		main.add(Box.createVerticalStrut(20));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private static JPanel inPanel(Component comp, int width) {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new BorderLayout(0, 0));
		p.setMinimumSize(new Dimension(width, 20));
		p.setPreferredSize(new Dimension(width, 30));
		p.setMaximumSize(new Dimension(width, 40));
		p.add(comp, BorderLayout.CENTER);
		return p;
	}
}
