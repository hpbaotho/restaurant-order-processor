package it.kApps.core;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.hsqldb.Server;

/**
 * The class handling all database stuff. TODO DEBUG!
 * 
 * @author Gianmarco Laggia
 * @version 1.0
 */
public class Database {

	/**
	 * The database server class
	 */
	private static Server	hsqlServer;

	/**
	 * The connection class. Used to manage request to the database.
	 */
	private static Connection	connection;

	/**
	 * The name of the database
	 */
	private static String		dbName;

	private static final String	CN	= "[Database] ";
	/**
	 * Constructor.<br>
	 * Create a new <code>Database</code> connected to a specified hsqldb and saved in a specified file.<br>
	 * Defaults sets logs to null e logWriter to null.
	 * 
	 * @param databaseName
	 *            The name of the database to connect
	 * @param fileName
	 *            The name of the file where the database is saved.
	 */
	public Database(String databaseName, String fileName) {
		hsqlServer = null;
		connection = null;

		dbName = databaseName;

		// ###### DEBUG ######
		Console.println(CN + "Creating a new database server class");
		// ###################
		hsqlServer = new Server();

		// HSQLDB prints out a lot of informations when starting and closing, which we don't need now.
		// Normally you should point the setLogWriter to some Writer object that could store the logs.
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);

		// settings and data will be stored in files
		// testdb.properties and testdb.script
		hsqlServer.setDatabaseName(0, Database.dbName);
		hsqlServer.setDatabasePath(0, "file:db/" + fileName);
	}

	/**
	 * Start the new database class.
	 */
	public static void start() {

		// ###### DEBUG ######
		Console.println(CN + "Starting a new database class");
		// ###################
		hsqlServer.start();
		Database.connection = null;
	}

	/**
	 * Stop the database class.
	 * 
	 * @throws SQLException
	 */
	public static void stop() {
		try{
			// ###### DEBUG ######
			Console.println(CN + "Stopping the database.");
			// ###################
			if (Database.connection != null) {
				Database.connection.close();
			}
			if (hsqlServer != null) {
				hsqlServer.stop();
			}
			// ###### DEBUG ######
			Console.println(CN + "Done");
			// ###################
		}catch(SQLException e){
			Console.println(CN + "Impossible to stop the database class");
		}
	}

	/**
	 * Start the new connection to the specified database, using the default username "sa" e no password
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void connect() {
		try {
			// ###### DEBUG ######
			Console.println(CN + "Getting a connection to the newly started database");
			// ###################
			Class.forName("org.hsqldb.jdbcDriver");
			// Default user of the HSQLDB is 'sa' with an empty password
			Database.connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
		} catch (ClassNotFoundException e) {
			Console.println(CN + "Impossible to connect to the database. ClassNotFound");
		} catch (SQLException e) {
			Console.println(CN + "Impossible to connect to the database. " + e.getCause());
		}
	}

	/**
	 * Start the new connection to the specified database, using the default username "sa" e no password
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void disconnect() {
		try {
			// ###### DEBUG ######
			Console.println(CN + "Stopping the connection to the database");
			// ###################
			if (Database.connection != null) {
				Database.connection.close();
			}
		} catch (SQLException e) {
			Console.println(CN + "Impossible to stop the connection to the database. " + e.getCause());
		}
	}

	// ############################################################
	public static boolean okcancel(String theMessage) {
		int result = JOptionPane.showConfirmDialog((Component) null, theMessage, "alert", JOptionPane.OK_CANCEL_OPTION);
		if (result == 0) {
			return true;
		} else {
			return false;
		}
	}

	// ############################################################

	/**
	 * Create the basic structure of the database if necessary.<br>
	 * This must be called only once, otherwise the database and all the data in it will be lost.
	 * 
	 * TODO read the structure from an XML
	 * 
	 * @throws SQLException
	 */
	public static void tableStructureCreation() {
		try {
			if (!okcancel("Maybe you are deleting all the old database. Sure?")) {
				return;
			}
			// ###### DEBUG ######
			Console.println(CN + "Dropping all existing tables");
			// ###################
			Database.connection.prepareStatement("DROP SCHEMA PUBLIC CASCADE").execute();

			// ###### DEBUG ######
			Console.println(CN + "Setting database default type (chached)");
			// ###################
			Database.connection.prepareStatement("SET DATABASE DEFAULT TABLE TYPE CACHED;").execute();

			// ###### DEBUG ######
			Console.println(CN + "Creating table 'categories'");
			// ###################
			Database.connection.prepareStatement("CREATE TABLE categories (id INTEGER IDENTITY, name VARCHAR(20));").execute();

			// ###### DEBUG ######
			Console.println(CN + "Creating table 'products'");
			// ###################
			Database.connection.prepareStatement("CREATE TABLE products (id INTEGER IDENTITY, name VARCHAR(30) UNIQUE, cat INTEGER, ingr VARCHAR(50), price INTEGER);")
			.execute();

			// ###### DEBUG ######
			Console.println(CN + "Creating table 'ingredients'");
			// ###################
			Database.connection.prepareStatement("CREATE TABLE ingredients (id INTEGER IDENTITY, name VARCHAR(30) UNIQUE);").execute();

			// ###### DEBUG ######
			Console.println(CN + "Creating table 'settings'");
			// ###################
			Database.connection.prepareStatement("CREATE TABLE settings (id INTEGER IDENTITY, name VARCHAR(30) UNIQUE, value VARCHAR(30));").execute();

			// ###### DEBUG ######
			Console.println(CN + "Structure created!");
			// ###################

		} catch (SQLException e) {
			Console.println(CN + "Impossible to complete the creation of the structure");
		}
	}

	public static void populateFromFile(String fileName, String tabName) {
		try {

			FileInputStream fstream = new FileInputStream(fileName);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			ArrayList<String> columns = listTableColumns(tabName);
			while ((strLine = br.readLine()) != null) {
				if (columns.size() == strLine.split("!").length) {
					String[] lineSplitted = strLine.split("!");
					String names = "";
					String values = "";
					for (int i = 0; i < columns.size(); i++) {
						names += columns.get(i) + ",";
						values += "'" + lineSplitted[i] + "',";
					}
					connection.prepareStatement(
							"insert into " + tabName + "(" + names.substring(0, names.length() - 1) + ") values (" + values.substring(0, values.length() - 1)
							+ ");").execute();
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static ArrayList<String> listTableColumns(String table) {
		try{
			// ###### DEBUG ######
			Console.println(CN + "Asking to the database the column list for " + table.toUpperCase());
			// ###################
			ArrayList<String> columnNames = new ArrayList<String>();
			ResultSet rsColumns = Database.connection.prepareStatement(
					"SELECT COLUMN_NAME, TYPE_NAME, COLUMN_SIZE FROM INFORMATION_SCHEMA.SYSTEM_COLUMNS WHERE TABLE_NAME = '" + table.toUpperCase()
					+ "' AND COLUMN_NAME NOT LIKE 'ID' ORDER BY ORDINAL_POSITION").executeQuery();
			while (rsColumns.next()) {
				columnNames.add(rsColumns.getString("COLUMN_NAME"));
				// String columnType = rsColumns.getString("TYPE_NAME");
				// int size = rsColumns.getInt("COLUMN_SIZE");
				// int position = rsColumns.getInt("ORDINAL_POSITION");
			}
			return columnNames;
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println(CN + "Error asking the column list for " + table.toUpperCase());
			// ###################
			return null;
		}
	}

	public static ArrayList<String> listTableValues(String table) {
		try {
			// ###### DEBUG ######
			Console.println(CN + "Asking to the database the values list for " + table.toUpperCase());
			// ###################
			ArrayList<String> values = new ArrayList<String>();
			ResultSet rs = Database.connection.prepareStatement("select * from " + table + ";").executeQuery();
			// Checking if the data is correct
			while (!rs.isLast()) {
				rs.next();
				values.add(rs.getString(2));
			}
			return values;
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println(CN + "Error asking the values list for " + table.toUpperCase());
			// ###################
			return null;
		}
	}

	public static ResultSet listValuesByName(String name, String table) {
		try {
			// ###### DEBUG ######
			Console.println(CN + "Asking to the database the values list for " + table.toUpperCase());
			// ###################
			ArrayList<String> values = new ArrayList<String>();
			ResultSet rs = Database.connection.prepareStatement("select * from " + table + " WHERE name='" + name + "';").executeQuery();
			return rs;
		} catch (SQLException e) {
			// ###### DEBUG ######
			Console.println(CN + "Error asking the values list for " + table.toUpperCase());
			// ###################
			return null;
		}
	}

	public static boolean updateTable(String table, String name, String value) {
		try {
			connection.prepareStatement("UPDATE " + table + " SET value='" + value + "' WHERE name='" + name + "';").execute();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		//
		// // 'Server' is a class of HSQLDB representing
		// // the database server
		// Server hsqlServer = null;
		// try {
		// hsqlServer = new Server();
		//
		// // HSQLDB prints out a lot of informations when
		// // starting and closing, which we don't need now.
		// // Normally you should point the setLogWriter
		// // to some Writer object that could store the logs.
		// hsqlServer.setLogWriter(null);
		// hsqlServer.setSilent(true);
		//
		// // The actual database will be named 'xdb' and its
		// // settings and data will be stored in files
		// // testdb.properties and testdb.script
		// hsqlServer.setDatabaseName(0, "xdb");
		// hsqlServer.setDatabasePath(0, "file:testdb");
		//
		// // Start the database!
		// hsqlServer.start();
		//
		// Connection connection = null;
		// // We have here two 'try' blocks and two 'finally'
		// // blocks because we have two things to close
		// // after all - HSQLDB server and connection
		// try {
		// // Getting a connection to the newly started database
		// Class.forName("org.hsqldb.jdbcDriver");
		// // Default user of the HSQLDB is 'sa'
		// // with an empty password
		// connection =
		// DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa",
		// "");
		//
		// // Here we run a few SQL statements to see if
		// // everything is working.
		// // We first drop an existing 'testtable' (supposing
		// // it was there from the previous run), create it
		// // once again, insert some data and then read it
		// // with SELECT query.
		// connection.prepareStatement("drop table testtable;").execute();
		// connection.prepareStatement("create table testtable ( id INTEGER, " +
		// "name VARCHAR(11));").execute();
		// connection.prepareStatement("insert into testtable(id, name) " +
		// "values (1, 'testvalue');").execute();
		// connection.prepareStatement("insert into testtable(id, name) " +
		// "values (2, 'testvalue');").execute();
		// connection.prepareStatement("insert into testtable(id, name) " +
		// "values (3, 'testvalue');").execute();
		// ResultSet rs =
		// connection.prepareStatement("select * from testtable;").executeQuery();
		//
		// // Checking if the data is correct
		// while (!rs.isLast()) {
		// rs.next();
		// System.out.println("Id: " + rs.getInt(1) + " Name: " +
		// rs.getString(2));
		// }
		// } finally {
		// // Closing the connection
		// if (connection != null) {
		// connection.close();
		// }
		//
		// }
		// } finally {
		// // Closing the server
		// if (hsqlServer != null) {
		// hsqlServer.stop();
		// }
		// }
		Database d = new Database("xdb", "testdb");
		Database.start();
		Database.connect();
		Database.tableStructureCreation();
		Database.populateFromFile("conf/populateCat.txt", "categories");
		Database.populateFromFile("conf/populateProd.txt", "products");
		Database.populateFromFile("conf/populateSett.txt", "settings");

		// d.doSomething();
		Database.stop();
	}
}
