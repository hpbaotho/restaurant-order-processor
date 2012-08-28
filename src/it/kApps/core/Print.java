package it.kApps.core;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;

public class Print implements Printable {

	public static void main(String[] args) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		PrintService[] ps = PrinterJob.lookupPrintServices();
		for (int i = 0; i < ps.length; i++) {
			System.out.println(ps[i].getName());
		}
	}

	@Override
	public int print(Graphics arg0, PageFormat arg1, int arg2) throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}

}
