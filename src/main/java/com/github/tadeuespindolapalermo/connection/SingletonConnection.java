package com.github.tadeuespindolapalermo.connection;

import java.sql.Connection;
import java.sql.DriverManager;

import com.github.tadeuespindolapalermo.enumeration.EnumConnectionOracle;
import com.github.tadeuespindolapalermo.enumeration.EnumConnectionPostgreSQL;
import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.enumeration.EnumLogMessages;
import com.github.tadeuespindolapalermo.util.LogUtil;
import com.github.tadeuespindolapalermo.util.ValidatorUtil;

public class SingletonConnection {
	
	private static Connection connection = null;	

	static {
		toConnect();
	}

	private static void toConnect() {
		try {
			if (connection == null) {				
				if (InfoConnection.getDatabase() == EnumDatabase.ORACLE) {				
					Class.forName(EnumConnectionOracle.DRIVER.getParameter());					
					connection = DriverManager.getConnection(
						mountOracleURL(EnumConnectionOracle.URL.getParameter()), 
						InfoConnection.getUser(),
						InfoConnection.getPassword()
					);
				}
				
				if (InfoConnection.getDatabase() == EnumDatabase.POSTGRE) {				
					Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());					
					connection = DriverManager.getConnection(
						mountPostgreSQLURL(EnumConnectionPostgreSQL.URL.getParameter()), 
						InfoConnection.getUser(),
						InfoConnection.getPassword()
					);
				}
				
				connection.setAutoCommit(false);				
				LogUtil.getLogger(SingletonConnection.class).info(EnumLogMessages.CONN_SUCCESS.getMessage());				
			}
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class).error(EnumLogMessages.CONN_FAILED.getMessage()
				+ "\n" + e.getCause().toString());
			e.printStackTrace();						
		}
	}
	
	private static String mountOracleURL(String initialURL) {			
		return new StringBuilder(initialURL)
			.append(ValidatorUtil.isNotNull(InfoConnection.getHost()) 
				? InfoConnection.getHost() : EnumConnectionOracle.HOST_DAFAULT.getParameter())
			.append(":")
			.append(ValidatorUtil.isNotNull(InfoConnection.getPort()) 
				? InfoConnection.getPort() : EnumConnectionOracle.PORT_DAFAULT.getParameter())
			.append("/")
			.append(InfoConnection.getNameDatabase())
			.toString();
	}
	
	private static String mountPostgreSQLURL(String initialURL) {			
		return new StringBuilder(initialURL)
			.append(ValidatorUtil.isNotNull(InfoConnection.getHost()) 
				? InfoConnection.getHost() : EnumConnectionPostgreSQL.HOST_DAFAULT.getParameter())
			.append(":")
			.append(ValidatorUtil.isNotNull(InfoConnection.getPort()) 
				? InfoConnection.getPort() : EnumConnectionPostgreSQL.PORT_DAFAULT.getParameter())
			.append("/")
			.append(InfoConnection.getNameDatabase())
			.toString();
	}

	public SingletonConnection() {
		toConnect();
	}

	public static Connection getConnection() {
		return connection;
	}

}
