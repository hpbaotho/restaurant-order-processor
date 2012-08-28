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

	public Product(String productName) {
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
		Database.disconnect();
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

}
