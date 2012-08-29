package it.kApps.core;

import it.kApps.GUI.CashDeskFrame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CashDesk implements Runnable {
	private int			total;
	private final ArrayList<Product>	prods;
	private final boolean				stopThread	= false;
	private final boolean				isThereAChange	= true;
	private CashDeskFrame				gui;

	public CashDesk() {
		this.total = 0;
		this.prods = new ArrayList<Product>();
	}

	public void setGui(CashDeskFrame g) {
		this.gui = g;
	}

	public ArrayList<String> getBarProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("1") || p.getCat().equals("2")) {
				list.add(p.getName());
			}
		}
		return list;
	}

	public ArrayList<String> getKitchenProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("0") || p.getCat().equals("3") || p.getCat().equals("5")) {
				list.add(p.getName());
			}
		}
		return list;
	}

	public ArrayList<String> getFriedProd() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < this.prods.size(); i++) {
			Product p = this.prods.get(i);
			if (p.getCat().equals("4")) {
				list.add(p.getName());
			}
		}
		return list;
	}

	public int getTotal() {
		return this.total;
	}
	private int updateTotal(int sum, String operator) {
		if ("-".equals(operator)) {
			this.total -= sum;
		} else if ("+".equals(operator)) {
			this.total += sum;
		}
		return this.total;
	}

	private void newClient() {
		this.incrementTotalDB(this.total);
		// TODO UPDATE THE GUI
		this.total = 0;
		this.prods.clear();
	}

	@Override
	public void run() {

		// if a name has not be defined the client listener don't send any message to the server
		while (!this.stopThread && this.isThereAChange) {
			if (this.gui != null) {
				this.gui.repaintText();
			} else {
				Console.println("[CashDesk] No GUI avaible");
			}
			// XXX we should add a counter here, after few time of wrong answer the thread must die
		}


	}

	public void buttonEvent(String text) {
		if (!"Annulla".equals(text)) {
			Product p = new Product(text);
			if (p.getPrice() != 0) {
				this.prods.add(p);
				this.updateTotal(p.getPrice(), "+");
				this.gui.repaintText();
			}
		}
	}

	private void incrementTotalDB(int value) {
		ResultSet rs = Database.listValuesByName("tatalToday", "settings");
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
		updated = Database.updateTable("settings", "totalToday", "" + ever);
		if (!updated) {
			Console.println("[CashDesk] WARNING: COULD NOT UPDATE THE TOTAL OF THE DAY. " + value + " euro");
		}
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
