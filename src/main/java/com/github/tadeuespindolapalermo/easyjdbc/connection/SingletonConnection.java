package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection.getDatabase;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNotNull;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import java.sql.Connection;
import java.sql.DriverManager;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionMySQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionOracle;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionPostgreSQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionSQLite;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumLogMessages;
import com.github.tadeuespindolapalermo.easyjdbc.util.LogUtil;

public class SingletonConnection extends MountConnection {
	
	private static Connection connection = InfoConnection.getConnection();	
	
	private static final String STRING_EMPTY = "";
	
	private SingletonConnection() { }

	static {
		toConnect();
	}

	private static void toConnect() {
		try {
			if (isNull(connection)) {	
				
				String url = STRING_EMPTY;
				boolean connectionOK = false;
				
				if (getDatabase().equals(EnumDatabase.ORACLE)) {
					url = getOracleURL();		
				}
				
				if (getDatabase().equals(EnumDatabase.POSTGRE)) {
					url = getPostgreSQLURL();		
				}	
				
				if (getDatabase().equals(EnumDatabase.MYSQL)) {
					url = getMySQLURL();
				}
				
				if (getDatabase().equals(EnumDatabase.SQLITE)) {
					connection = DriverManager.getConnection(getSQLiteURL());
					connectionOK = true;
				}	
				
				if (!connectionOK)
					connection = DriverManager.getConnection(url, InfoConnection.getUser(), InfoConnection.getPassword());
				
				LogUtil.getLogger(SingletonConnection.class).info(EnumLogMessages.CONN_SUCCESS.getMessage()
						+ "\nBank: " + getDatabase().name()
						+ "\nDatabase: " + (isNotNull(InfoConnection.getNameDatabase()) 
								? InfoConnection.getNameDatabase() : "informed directly in the connection URL"));				
			}
			connection.setAutoCommit(false);	
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class).error(EnumLogMessages.CONN_FAILED.getMessage()
				+ "\n" + e.getCause().toString());							
		}
	}

	private static String getMySQLURL() throws ClassNotFoundException {
		Class.forName(EnumConnectionMySQL.DRIVER_V8.getParameter());
		return isNull(InfoConnection.getUrl()) ? mountMySQLURL(EnumConnectionMySQL.URL.getParameter()) : InfoConnection.getUrl();
	}

	private static String getPostgreSQLURL() throws ClassNotFoundException {
		Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());	
		return isNull(InfoConnection.getUrl()) ? mountPostgreSQLURL(EnumConnectionPostgreSQL.URL.getParameter()) : InfoConnection.getUrl();
	}

	private static String getOracleURL() throws ClassNotFoundException {
		Class.forName(EnumConnectionOracle.DRIVER.getParameter());					
		return isNull(InfoConnection.getUrl()) ? mountOracleURL(EnumConnectionOracle.URL.getParameter()) : InfoConnection.getUrl();
	}
	
	private static String getSQLiteURL() throws ClassNotFoundException {
		Class.forName(EnumConnectionSQLite.DRIVER.getParameter());					
		return isNull(InfoConnection.getUrl()) ? mountSQLiteURL(EnumConnectionSQLite.URL.getParameter()) : InfoConnection.getUrl();
	}

	public static Connection getConnection() {
		return connection;
	}

}
