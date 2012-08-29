package it.kApps.core;

import java.util.ArrayList;

public class CashDesk implements Runnable {
	private int			total;
	private final ArrayList<Product>	prods;
	private final boolean				stopThread	= false;

	public CashDesk() {
		this.total = 0;
		this.prods = new ArrayList<Product>();
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
		// TODO UPDATE DATABASE WITH TOTALS
		// TODO UPDATE THE GUI
		this.total = 0;
		this.prods.clear();
	}

	@Override
	public void run() {

		// if a name has not be defined the client listener don't send any message to the server
		while (!this.stopThread) {

			System.out.println("ciao");

			// XXX we should add a counter here, after few time of wrong answer the thread must die
		}


	}
}
