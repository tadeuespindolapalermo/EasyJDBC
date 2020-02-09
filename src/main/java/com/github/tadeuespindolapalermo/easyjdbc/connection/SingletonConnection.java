package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionMySQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionOracle;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionPostgreSQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumLogMessages;
import com.github.tadeuespindolapalermo.easyjdbc.util.LogUtil;

public class SingletonConnection extends MountConnection {
	
	private static Connection connection = InfoConnection.getConnection();	
	
	private SingletonConnection() { }

	static {
		toConnect();
	}

	private static void toConnect() {
		try {
			if (isNull(connection)) {	
				
				if (InfoConnection.getDatabase().equals(EnumDatabase.ORACLE)) {		
					establishOracleConnection();
				}
				
				if (InfoConnection.getDatabase().equals(EnumDatabase.POSTGRE)) {
					establishPostgreSQLConnection();
				}	
				
				if (InfoConnection.getDatabase().equals(EnumDatabase.MYSQL)) {				
					establishMySQLConnection();
				}	
				
				LogUtil.getLogger(SingletonConnection.class).info(EnumLogMessages.CONN_SUCCESS.getMessage()
						+ "\nBank: " + InfoConnection.getDatabase().name()
						+ "\nDatabase: " + (isNotNull(InfoConnection.getNameDatabase()) 
								? InfoConnection.getNameDatabase() : "informed directly in the connection URL"));				
			}
			connection.setAutoCommit(false);	
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class).error(EnumLogMessages.CONN_FAILED.getMessage()
				+ "\n" + e.getCause().toString());							
		}
	}

	private static void establishMySQLConnection() throws ClassNotFoundException, SQLException {
		String url = isNull(InfoConnection.getUrl()) ? mountMySQLURL(EnumConnectionMySQL.URL.getParameter()) : InfoConnection.getUrl();
		Class.forName(EnumConnectionMySQL.DRIVER_V8.getParameter());					
		connection = DriverManager.getConnection(url, InfoConnection.getUser(), InfoConnection.getPassword());
	}

	private static void establishPostgreSQLConnection() throws ClassNotFoundException, SQLException {
		String url = isNull(InfoConnection.getUrl()) ? mountPostgreSQLURL(EnumConnectionPostgreSQL.URL.getParameter()) : InfoConnection.getUrl();		
		Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());					
		connection = DriverManager.getConnection(url, InfoConnection.getUser(), InfoConnection.getPassword());
	}

	private static void establishOracleConnection() throws ClassNotFoundException, SQLException {
		String url = isNull(InfoConnection.getUrl()) ? mountOracleURL(EnumConnectionOracle.URL.getParameter()) : InfoConnection.getUrl();		
		Class.forName(EnumConnectionOracle.DRIVER.getParameter());					
		connection = DriverManager.getConnection(url, InfoConnection.getUser(),	InfoConnection.getPassword());
	}	

	public static Connection getConnection() {
		return connection;
	}

}
