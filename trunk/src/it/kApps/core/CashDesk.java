package it.kApps.core;

import it.kApps.GUI.CashDeskFrame;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CashDesk {
	private int							total;
	private final ArrayList<Product>	prods;
	private CashDeskFrame				gui;
	private int							whereTo			= -1;

	public CashDesk() {
		this.total = 0;
		this.prods = new ArrayList<Product>();
	}

	public void setGui(CashDeskFrame g) {
		this.gui = g;
	}

	public void setWhereTo(int table) {
		this.whereTo = table;
	}

	public int getWhereTo(){
		return this.whereTo;
	}

	public ArrayList<String> getBarProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("1") || p.getCat().equals("2")) {
				list.add(p.getCompleteName() + p.getAdds());
			}
		}
		return list;
	}

	public ArrayList<String> getKitchenProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("0") || p.getCat().equals("3") || p.getCat().equals("5")) {
				list.add(p.getCompleteName() + p.getAdds() + p.getRemoves());
			}
		}
		return list;
	}

	public ArrayList<String> getFriedProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("4")) {
				list.add(p.getCompleteName() + p.getAdds() + p.getRemoves());
			}
		}
		return list;
	}

	public int getTotal() {
		return this.total;
	}

	public ArrayList<Product> getProds() {
		return this.prods;
	}

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

	private void newClient() {
		this.incrementTotalDB(this.total);
		this.total = 0;
		this.prods.clear();
		this.whereTo = -1;
		this.gui.resetWhereTo();
		this.gui.repaintText();
	}

	public void buttonEvent(String text, boolean free) {
		if ("<".equals(text)){
			Product p = this.prods.remove(this.prods.size() - 1);
			this.updateTotal(p.getPrice(), "-");
		}else if("X".equals(text)){
			this.total = 0;
			this.prods.clear();
			this.whereTo = -1;
			this.gui.resetWhereTo();
		}else if("Conferma".equals(text)){
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
			this.newClient();
			// if (this.gui.print()) {
			//
			// }

		}else if("Ket".equals(text)){
			this.prods.add(new Product("Ketchup"));
		}else if("May".equals(text)){
			this.prods.add(new Product("Maionese"));
		}else if("Temp.Amb.".equals(text)) {
			this.prods.get(this.prods.size() - 1).addToName("Temp.Ambiente");
		} else if ("Sfogliata Fantasia".equals(text) || "Toast Farcito".equals(text)) {
			Product p = new Product(text, free);
			String ing = JOptionPane.showInputDialog("Inserisci gli ingredienti");
			p.setAdds(ing);
			this.prods.add(p);
			if (!free) {
				this.updateTotal(p.getPrice(), "+");
			}
		} else if ("Togli".equals(text)) {
			if (this.prods.size() > 0) {
				String ing = JOptionPane.showInputDialog("Cosa vuoi togliere?");
				this.prods.get(this.prods.size() - 1).setRemoves(ing);
			}
		}else{
			Product p = new Product(text, free);
			this.prods.add(p);
			if (!free) {
				this.updateTotal(p.getPrice(), "+");
			}
		}
		this.gui.repaintText();
	}

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

		rs = Database.listValuesByName("tatalEver", "settings");
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
