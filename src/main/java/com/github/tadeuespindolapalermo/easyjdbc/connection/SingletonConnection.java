package com.github.tadeuespindolapalermo.easyjdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;

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
			if (connection == null) {				
				if (InfoConnection.getDatabase().equals(EnumDatabase.ORACLE)) {				
					Class.forName(EnumConnectionOracle.DRIVER.getParameter());					
					connection = DriverManager.getConnection(
						mountOracleURL(EnumConnectionOracle.URL.getParameter()), 
						InfoConnection.getUser(),
						InfoConnection.getPassword()
					);
					connection.setAutoCommit(false);	
				}
				
				if (InfoConnection.getDatabase().equals(EnumDatabase.POSTGRE)) {				
					Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());					
					connection = DriverManager.getConnection(
						mountPostgreSQLURL(EnumConnectionPostgreSQL.URL.getParameter()), 
						InfoConnection.getUser(),
						InfoConnection.getPassword()
					);
					connection.setAutoCommit(false);	
				}	
				
				if (InfoConnection.getDatabase().equals(EnumDatabase.MYSQL)) {				
					Class.forName(EnumConnectionMySQL.DRIVER.getParameter());					
					connection = DriverManager.getConnection(
						mountMySQLURL(EnumConnectionMySQL.URL.getParameter()), 
						InfoConnection.getUser(),
						InfoConnection.getPassword()
					);
					connection.setAutoCommit(false);	
				}	
				
				LogUtil.getLogger(SingletonConnection.class).info(EnumLogMessages.CONN_SUCCESS.getMessage()
						+ "\nBank: " + InfoConnection.getDatabase().name()
						+ "\nDatabase: " + InfoConnection.getNameDatabase());				
			}
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class).error(EnumLogMessages.CONN_FAILED.getMessage()
				+ "\n" + e.getCause().toString());							
		}
	}	

	public static Connection getConnection() {
		return connection;
	}

}
