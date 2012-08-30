package it.kApps.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Product {

	private final String name;
	private String cat;
	private ArrayList<Integer>	prices;
	private int					price;
	private String[]			ingredients;
	private String				addToName	= "";

	public Product(String productName, boolean free) {
		this.name = productName;
		Database.connect();
		ResultSet rs = Database.listValuesByName(this.name, "products");
		try {
			while (rs.next()) {
				this.cat = "" + rs.getInt(3);
				this.ingredients = rs.getString(4).split("!");
				this.price = rs.getInt(5);
			}
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println("[Product] Error in handling database values");
			// ###################
		}
		if (free) {
			this.price = 0;
		}
		Database.disconnect();
	}

	public Product(String name) {
		this.name = name;
		this.cat = "0";

	}

	public String[] getIngredients() {
		return this.ingredients;
	}

	public ArrayList<Integer> getPrices() {
		return this.prices;
	}

	public String getCat() {
		return this.cat;
	}

	public String getName() {
		return this.name;
	}

	public String getCompleteName() {
		return this.name + " " + this.addToName;
	}

	public int getPrice() {
		return this.price;
	}

	public void addToName(String add) {
		this.addToName = add;
	}

}
