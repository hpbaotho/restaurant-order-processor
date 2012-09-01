package it.kApps.core;

import it.kApps.GUI.CashDeskFrame;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Main class for the CashDesk, contains all the action called in the GUI
 * 
 * @author Gianmarco Laggia
 * 
 */
public class CashDesk {

	/**
	 * For each client, the total of the order
	 */
	private int							total;
	/**
	 * Contains the products of the order, reset every client.
	 */
	private final ArrayList<Product>	prods;
	/**
	 * A link to the gui TODO MVC!
	 */
	private CashDeskFrame				gui;
	/**
	 * The place where the order should be delivered
	 */
	private int							whereTo	= -1;

	/**
	 * Constructor, set to zero the total and initialize a new <code>ArrayList&lt;String&gt;</code> of the products.
	 */
	public CashDesk() {
		this.total = 0;
		this.prods = new ArrayList<Product>();
	}

	/**
	 * Set the GUI with the class have to interact
	 * 
	 * @param g
	 *            The GUI
	 */
	public void setGui(CashDeskFrame g) {
		this.gui = g;
	}

	/**
	 * Set the place where the order should be delivered.<br>
	 * 
	 * @param table
	 */
	public void setWhereTo(int table) {
		this.whereTo = table;
	}

	/**
	 * Get Where to
	 * 
	 * @return the location integer
	 */
	public int getWhereTo() {
		return this.whereTo;
	}

	/**
	 * Returns an <code>ArrayList&lt;String&gt;</code> containing the names of the products' names that refers to the
	 * bar.
	 * 
	 * @return The products
	 */
	public ArrayList<String> getBarProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("1") || p.getCat().equals("2")) {
				list.add(p.getCompleteName() + p.getAdds()+ p.getVariations());
			}
		}
		return list;
	}

	/**
	 * Returns an <code>ArrayList&lt;String&gt;</code> containing the names of the products' names that refers to the
	 * kitchen.
	 * 
	 * @return The products
	 */
	public ArrayList<String> getKitchenProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("0") || p.getCat().equals("3")) {
				list.add(p.getCompleteName() + p.getAdds() + p.getRemoves()+ p.getVariations());
			}
		}
		return list;
	}

	/**
	 * Returns an <code>ArrayList&lt;String&gt;</code> containing the names of the products' names that refers to the
	 * back kitchen.
	 * 
	 * @return The products
	 */
	public ArrayList<String> getFriedProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("4") || p.getCat().equals("5")) {
				list.add(p.getCompleteName() + p.getAdds() + p.getRemoves()+ p.getVariations());
			}
		}
		return list;
	}

	/**
	 * The total of the current order
	 * 
	 * @return int the value <u>(multiplied for 100)</u> of the total
	 */
	public int getTotal() {
		return this.total;
	}

	/**
	 * Return the products selected in this order
	 * 
	 * @return <code>ArrayList&lt;String&gt;</code> containings products' names.
	 */
	public ArrayList<Product> getProds() {
		return this.prods;
	}

	/**
	 * Update the local total (the total of this order)
	 * 
	 * @param sum
	 *            The value to operate
	 * @param operator
	 *            The operator (plus or minus)
	 * @return The result of the operation
	 */
	private int updateTotal(int sum, String operator) {
		if ("-".equals(operator)) {
			this.total -= sum;
			if (sum <= 0) {
				sum = 0;
			}
		} else if ("+".equals(operator)) {
			this.total += sum;
		}
		return this.total;
	}

	/**
	 * Prepare the class for a new order.<br>
	 * Ask the update of the <code>totalEver</code> and the <code>totalToday</code> in the database, reset the GUI to
	 * zero.
	 */
	private void newClient() {
		this.incrementTotalDB(this.total);
		this.total = 0;
		this.prods.clear();
		this.whereTo = -1;
		this.gui.resetWhereTo();
		this.gui.repaintText();
	}

	/**
	 * Handles the action of each button in the gui, referring to its text
	 * 
	 * @param text
	 *            The text of the pressed button
	 * @param free
	 *            True if the product should be free TODO not corret to be here this. MVC!
	 */
	public void buttonEvent(String text, boolean free) {
		if ("<".equals(text)) {
			Product p = this.prods.remove(this.prods.size() - 1);
			this.updateTotal(p.getPrice(), "-");
		} else if ("X".equals(text)) {
			this.total = 0;
			this.prods.clear();
			this.whereTo = -1;
			this.gui.resetWhereTo();
		} else if ("Conferma".equals(text)) {
			if (this.whereTo == -1) {
				JOptionPane.showMessageDialog((Component) null, "DEVI SELEZIONARE UN TAVOLO");
				return;
			}
			if (this.prods.size() == 0) {
				JOptionPane.showMessageDialog((Component) null, "SCUSA SA', MA NOL COMPRA NIENTE?");
				return;
			}
			this.gui.print();
			this.incrementOrderNum();
			JOptionPane.showMessageDialog(null, "Ordine andato a buon fine");
			this.newClient();

		} else if ("Ket".equals(text)) {
			this.prods.add(new Product("Ketchup"));
		} else if ("May".equals(text)) {
			this.prods.add(new Product("Maionese"));
		} else if ("S.Rosa".equals(text)) {
			this.prods.add(new Product("Salsa Rosa"));
		} else if ("Temp.Amb.".equals(text)) {
			this.prods.get(this.prods.size() - 1).setVariations("Temp.Ambiente");
		} else if ("Sfogliata Fantasia".equals(text) || "Toast Farcito".equals(text)) {
			Product p = new Product(text, free);
			String ing = JOptionPane.showInputDialog("Inserisci gli ingredienti");
			if(ing!=null && !"".equals(ing)){
				p.setAdds(ing,Integer.parseInt(ing));
				this.prods.add(p);
				if (!free) {
					this.updateTotal(p.getPrice(), "+");
				}
			}
		} else if ("Variaz.".equals(text)) {

			JTextField add = new JTextField();
			JTextField remove = new JTextField();
			JTextField variation = new JTextField();
			JCheckBox cb = new JCheckBox("Aggiungi prezzo");
			JTextField quanti = new JTextField();
			final JComponent[] inputs = new JComponent[] { new JLabel("Aggiungi"), add, new JLabel("Togli"), remove, new JLabel("Annotazioni"), variation,cb,new JLabel("N. ingredienti"),quanti };
			JOptionPane.showMessageDialog(null, inputs, "Aggiungi o togli ingrediente", JOptionPane.PLAIN_MESSAGE);
			if (this.prods.size() > 0) {
				if (!"".equals(add.getText())) {
					Product p = this.prods.get(this.prods.size()-1);
					if(cb.isSelected() && !"".equals(quanti.getText())){
						int i = 0;
						if("".equals(quanti.getText())){
							i = 1;
						}else{
							i = Integer.parseInt(quanti.getText());
						}
						p.setAdds(add.getText(),i);
						this.updateTotal(50*i, "+");
					}else{
						p.setAdds(add.getText(),0);
					}
				}
				if (!"".equals(remove.getText())) {
					this.prods.get(this.prods.size() - 1).setRemoves(remove.getText());
				}
				if (!"".equals(variation.getText())) {
					this.prods.get(this.prods.size() - 1).setVariations(remove.getText());
				}
			}
		} else {
			Product p = new Product(text, free);
			this.prods.add(p);
			if (!free) {
				this.updateTotal(p.getPrice(), "+");
			}
		}
		this.gui.repaintText();
	}

	/**
	 * Increments the order num in the database
	 */
	private void incrementOrderNum() {
		Database.connect();
		ResultSet rs = Database.listValuesByName("ordersToday", "settings");
		int actual = 0;
		try {
			while (rs.next()) {
				actual = rs.getInt(3);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[CashDesk] Error in handling database values");
			// ###################
		}
		actual += 1;
		Boolean updated = Database.updateTable("settings", "ordersToday", "" + actual);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE ORDER NUM");
		}
		Database.disconnect();
	}

	/**
	 * Increments the total by the value in the database.
	 * 
	 * @param value
	 *            The value to sum
	 */
	private void incrementTotalDB(int value) {
		Database.connect();
		ResultSet rs = Database.listValuesByName("totalToday", "settings");
		int actual = 0;
		try {
			while (rs.next()) {
				actual = rs.getInt(3);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[CashDesk] Error in handling database values");
			// ###################
		}
		actual += value;
		Boolean updated = Database.updateTable("settings", "totalToday", "" + actual);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE TOTAL. " + value + " euro");
		}

		rs = Database.listValuesByName("totalEver", "settings");
		int ever = 0;
		try {
			while (rs.next()) {
				ever = rs.getInt(3);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[CashDesk] Error in handling database values");
			// ###################
		}
		ever += value;
		updated = Database.updateTable("settings", "totalEver", "" + ever);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE TOTAL OF THE DAY. " + value + " euro");
		}
		Database.disconnect();
	}

	/**
	 * Read from the database the actual number of the order
	 * 
	 * @return An integer, number of the next order
	 */
	public String getActualOrder() {
		Database.connect();
		ResultSet rs = Database.listValuesByName("ordersToday", "settings");
		int actual = 0;
		try {
			while (rs.next()) {
				actual = rs.getInt(3);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[CashDesk] Error in handling database values");
			// ###################
		}
		Database.disconnect();
		return "" + actual;
	}
}
