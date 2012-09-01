package it.kApps.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Product {

	private final String			name;
	private String					cat;
	private int						price;
	private String[]				ingredients;
	private final ArrayList<String>	addfee		= new ArrayList<String>();
	private final ArrayList<String>	adds		= new ArrayList<String>();
	private final ArrayList<String>	removes		= new ArrayList<String>();
	private final ArrayList<String>	variations	= new ArrayList<String>();

	public Product(String productName, boolean free) {
		this.name = productName;
		Database.connect();
		ResultSet rs = Database.listValuesByName(this.name, "products");
		try {
			while (rs.next()) {
				this.cat = "" + rs.getInt(3);
				this.ingredients = rs.getString(4).split(PagodaConstants.SPLIT_CHAR);
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

	public void setAdds(String ing, int a) {
		this.adds.add(ing);
		for (int i = 0; i < a; i++) {
			this.addfee.add(ing);
		}
	}

	public void setRemoves(String ing) {
		this.removes.add(ing);
	}

	public void setVariations(String ing) {
		this.variations.add(ing);
	}

	public String getAdds() {
		String s = "";
		for (int i = 0; i < this.adds.size(); i++) {
			s += "\n AGGIUNGI: " + this.adds.get(i);
		}
		return s;
	}

	public String getRemoves() {
		String s = "";
		for (int i = 0; i < this.removes.size(); i++) {
			s += "\n TOGLI: " + this.removes.get(i);
		}
		return s;
	}

	public String getVariations() {
		String s = "";
		for (int i = 0; i < this.variations.size(); i++) {
			s += " " + this.variations.get(i);
		}
		return s;
	}

	public String getName() {
		return this.name;
	}

	public String getCat() {
		return this.cat;
	}

	public String getCompleteName() {
		return this.name;
	}

	public int getPrice() {
		int v = 0;
		for(int i = 0; i<this.addfee.size();i++){
			v += 50;
		}
		return this.price + v;
	}
}
