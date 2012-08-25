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
	private static Server hsqlServer;

	/**
	 * The connection class. Used to manage request to the database.
	 */
	private Connection connection;

	/**
	 * The name of the database
	 */
	private final String dbName;

	/**
	 * Constructor.<br>
	 * Create a new <code>Database</code> connected to a specified hsqldb and
	 * saved in a specified file.<br>
	 * Defaults sets logs to null e logWriter to null.
	 * 
	 * @param dbName
	 *            The name of the database to connect
	 * @param fileName
	 *            The name of the file where the database is saved.
	 */
	public Database(String databaseName, String fileName) {
		hsqlServer = null;
		this.connection = null;

		this.dbName = databaseName;

		// Create a new database server class.
		hsqlServer = new Server();

		// HSQLDB prints out a lot of informations when starting and closing,
		// which we don't need now.
		// Normally you should point the setLogWriter to some Writer object that
		// could store the logs.
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);

		// The actual database will be named 'xdb' and its
		// settings and data will be stored in files
		// testdb.properties and testdb.script
		hsqlServer.setDatabaseName(0, this.dbName);
		hsqlServer.setDatabasePath(0, "file:" + fileName);
	}

	public void start() {
		hsqlServer.start();
		this.connection = null;
	}

	public void stop() {
		if (hsqlServer != null) {
			hsqlServer.stop();
		}
	}

	public void connect() throws ClassNotFoundException, SQLException {
		// Getting a connection to the newly started database
		Class.forName("org.hsqldb.jdbcDriver");
		// Default user of the HSQLDB is 'sa' with an empty password
		this.connection = DriverManager.getConnection(
				"jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
	}

	// ############################################################
	public static boolean okcancel(String theMessage) {
		int result = JOptionPane.showConfirmDialog((Component) null,
				theMessage, "alert", JOptionPane.OK_CANCEL_OPTION);
		if (result == 0) {
			return true;
		} else {
			return false;
		}
	}

	// ############################################################

	/**
	 * Create the basic structure of the database if necessary.<br>
	 * This must be called only once, although the database and all the data in
	 * it will be lost.
	 * 
	 * TODO read the structure from an XML
	 * 
	 * @throws SQLException
	 */
	public void structureCreation() throws SQLException {
		if (!okcancel("Maybe you are deleting all the old database. Sure?")) {
			return;
		}
		this.connection.prepareStatement("DROP SCHEMA PUBLIC CASCADE").execute();
		this.connection.prepareStatement("SET DATABASE DEFAULT TABLE TYPE CACHED;")
		.execute();
		this.connection
		.prepareStatement(
				"CREATE TABLE categories (id INTEGER IDENTITY, name VARCHAR(20));")
				.execute();
		this.connection
		.prepareStatement(
				"CREATE TABLE products (id INTEGER IDENTITY, name VARCHAR(30) UNIQUE, cat INTEGER, ingr VARCHAR(50));")
				.execute();
		this.connection
		.prepareStatement(
				"CREATE TABLE ingredients (id INTEGER IDENTITY, name VARCHAR(30) UNIQUE);")
				.execute();
	}

	public void populateFromFile(String fileName) {
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(fileName);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				System.out.println(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void doSomething() throws ClassNotFoundException, SQLException {
		try {

			// Here we run a few SQL statements to see if
			// everything is working.
			// We first drop an existing 'testtable' (supposing
			// it was there from the previous run), create it
			// once again, insert some data and then read it
			// with SELECT query.
			// connection.prepareStatement("drop table testtable;").execute();
			// connection.prepareStatement("create table testtable ( id INTEGER, name VARCHAR(11));").execute();
			this.connection.prepareStatement(
					"insert into cat(name) values ('testvalue');").execute();
			this.connection.prepareStatement(
					"insert into cat(name) values ('testvalue');").execute();
			// connection.prepareStatement("insert into testtable(id, name) values (2, 'testvalue');").execute();
			// connection.prepareStatement("insert into testtable(id, name) values (3, 'testvalue');").execute();
			ResultSet rs = this.connection.prepareStatement("select * from cat;")
					.executeQuery();

			// Checking if the data is correct
			while (!rs.isLast()) {
				rs.next();
				System.out.println("Id: " + rs.getInt(1) + " Name: "
						+ rs.getString(2));
			}
			this.connection.prepareStatement("drop table cat;").execute();

		} finally {
			// Closing the connection
			if (this.connection != null) {
				this.connection.close();
			}
			this.stop();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException,
	SQLException {
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
		Database d = new Database("xdb", "lib/testdb");
		d.start();
		d.connect();
		d.structureCreation();
		// d.doSomething();
		d.stop();
	}
}
