package it.kApps.GUI;

import it.kApps.core.Console;

public class Menu implements ActionListener, ItemListener {
	JTextArea output;
	JScrollPane scrollPane;
	/**
	 * The main menu bar
	 */
	private JMenuBar menuBar;
	/**
	 * Each menu
	 */
	private JMenu menu;
	/**
	 * Each submenu;
	 */
	private JMenu submenu;
	/**
	 * Each element of the menu
	 */
	private JMenuItem menuItem;
	/**
	 * Each check-box in the menu
	 */
	private JCheckBoxMenuItem cbMenuItem;

	private static final String NEW_LINE = "\n";

	public JMenuBar createMenuBar() {

		JRadioButtonMenuItem rbMenuItem;

		// Create the menu bar.
		this.menuBar = new JMenuBar();

		// Build the first menu.
		this.menu = new JMenu("File");
		this.menu.setMnemonic(KeyEvent.VK_F);
		this.menuBar.add(this.menu);

		// a group of JMenuItems
		this.menuItem = new JMenuItem("Start new day", KeyEvent.VK_S);
		// menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
		this.menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		this.menuItem.addActionListener(this);
		this.menu.add(this.menuItem);

		// ImageIcon icon = createImageIcon("images/middle.gif");
		this.menuItem = new JMenuItem("Both text and icon");// , icon);
		this.menuItem.setMnemonic(KeyEvent.VK_B);
		this.menuItem.addActionListener(this);
		this.menu.add(this.menuItem);

		this.menuItem = new JMenuItem("icon");
		this.menuItem.setMnemonic(KeyEvent.VK_D);
		this.menuItem.addActionListener(this);
		this.menu.add(this.menuItem);

		// a group of radio button menu items
		this.menu.addSeparator();
		ButtonGroup group = new ButtonGroup();

		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		this.menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		this.menu.add(rbMenuItem);

		// a submenu
		this.menu.addSeparator();
		this.submenu = new JMenu("A submenu");
		this.submenu.setMnemonic(KeyEvent.VK_S);

		this.menuItem = new JMenuItem("An item in the submenu");
		this.menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		this.menuItem.addActionListener(this);
		this.submenu.add(this.menuItem);

		this.menuItem = new JMenuItem("Another item");
		this.menuItem.addActionListener(this);
		this.submenu.add(this.menuItem);
		this.menu.add(this.submenu);

		this.menu.addSeparator();
		this.menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		this.menuItem.addActionListener(this);
		this.menu.add(this.menuItem);

		// Build second menu in the menu bar.
		this.menu = new JMenu("Window");
		this.menu.setMnemonic(KeyEvent.VK_W);
		this.menuBar.add(this.menu);

		// a group of JMenuItems
		this.menuItem = new JMenuItem("Set defaults windows", KeyEvent.VK_D);
		this.menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				ActionEvent.CTRL_MASK));
		this.menuItem.addActionListener(this);
		this.menu.add(this.menuItem);

		this.menu.addSeparator();

		this.cbMenuItem = new JCheckBoxMenuItem("Console");
		if (GUI.getIntFrameList().get("Console").isVisible()) {
			this.cbMenuItem.setSelected(true);
		} else {
			this.cbMenuItem.setSelected(false);
		}
		this.cbMenuItem.setMnemonic(KeyEvent.VK_C);
		this.cbMenuItem.addItemListener(this);
		this.menu.add(this.cbMenuItem);

		this.cbMenuItem = new JCheckBoxMenuItem("Tables' Map");
		// if(GUI.getIntFrameList().get("Tables").isVisible()){
		// cbMenuItem.setSelected(true);
		// }else{
		// cbMenuItem.setSelected(false);
		// }
		this.cbMenuItem.setMnemonic(KeyEvent.VK_M);
		this.cbMenuItem.addItemListener(this);
		this.menu.add(this.cbMenuItem);

		this.cbMenuItem = new JCheckBoxMenuItem("Cash Desk");
		this.cbMenuItem.setMnemonic(KeyEvent.VK_K);
		this.cbMenuItem.addItemListener(this);
		this.menu.add(this.cbMenuItem);

		return this.menuBar;
	}

	public Container createContentPane() {
		// Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);

		// Create a scrolled text area.
		this.output = new JTextArea(5, 30);
		this.output.setEditable(false);
		this.scrollPane = new JScrollPane(this.output);

		// Add the text area to the content pane.
		contentPane.add(this.scrollPane, BorderLayout.CENTER);

		return contentPane;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		String action = source.getText();

		if (action.equalsIgnoreCase("exit")) {
			System.exit(0);
		}
		String s = "Action event detected." + NEW_LINE + "    Event source: "
				+ source.getText() + " (an instance of "
				+ this.getClassName(source) + ")";
		Console.println(s);

	}

	public void itemStateChanged(ItemEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		if (source.getText().equalsIgnoreCase("console")) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				GUI.getIntFrameList().get("Console").setVisible(true);
			} else {
				GUI.getIntFrameList().get("Console").setVisible(false);
			}
		}
		String s = "Item event detected."
				+ NEW_LINE
				+ "    Event source: "
				+ source.getText()
				+ " (an instance of "
				+ this.getClassName(source)
				+ ")"
				+ NEW_LINE
				+ "    New state: "
				+ ((e.getStateChange() == ItemEvent.SELECTED) ? "selected"
						: "unselected");
		Console.println(s);
	}

	// Returns just the class name -- no package info.
	protected String getClassName(Object o) {
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");
		return classString.substring(dotIndex + 1);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Menu.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
