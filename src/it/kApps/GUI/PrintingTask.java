package it.kApps.GUI;

import it.kApps.core.Console;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.SwingWorker;

public class PrintingTask extends SwingWorker<Object, Object> {
	private final MessageFormat	headerFormat;
	private final MessageFormat	footerFormat;
	private final boolean		interactive;

	public PrintingTask(MessageFormat header, MessageFormat footer, boolean interactive) {
		headerFormat = header;
		footerFormat = footer;
		this.interactive = interactive;
	}

	@Override
	protected Object doInBackground() {
		try {
			// ###### DEBUG ######
			Console.println("[PrintingTask] Start to print");
			// ###################
			PrintService[] ps = PrinterJob.lookupPrintServices();
			for (int i = 0; i < ps.length; i++) {
				if (ps[i].getName().equals(GUI.getDeskPrinter())) {
					PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
					attr_set.add(new MediaPrintableArea(5, 5, 90, 200, MediaPrintableArea.MM));
					CashDeskFrame.getTextPane().print(headerFormat, footerFormat, false, ps[i], attr_set, interactive);
				}
			}
			for (int i = 0; i < ps.length; i++) {
				if (ps[i].getName().equals(GUI.getKitchenPrinter())) {
					CashDeskFrame.onlyBody();
					PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
					attr_set.add(new MediaPrintableArea(5, 5, 90, 200, MediaPrintableArea.MM));
					CashDeskFrame.getTextPane().print(headerFormat, footerFormat, false, ps[i], attr_set, interactive);
				}
			}
		} catch (PrinterException ex) {
			Console.printWarning("Sorry, a printer error occurred");
		} catch (SecurityException ex) {
			Console.printWarning("Sorry, cannot access the printer due to security reasons");
		}
		return null;
	}
}