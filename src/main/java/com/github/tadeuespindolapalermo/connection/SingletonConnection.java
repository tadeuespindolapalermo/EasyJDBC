package com.github.tadeuespindolapalermo.connection;

import java.sql.Connection;
import java.sql.DriverManager;

import com.github.tadeuespindolapalermo.enumeration.EnumConnectionPostgreSQL;
import com.github.tadeuespindolapalermo.enumeration.EnumLogMessages;
import com.github.tadeuespindolapalermo.util.LogUtil;

public class SingletonConnection {
	
	private static Connection connection = null;	

	static {
		toConnect();
	}

	private static void toConnect() {
		try {
			if (connection == null) {				
				Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());				
				connection = DriverManager.getConnection(
					EnumConnectionPostgreSQL.URL.getParameter(), 
					EnumConnectionPostgreSQL.USER.getParameter(),
					EnumConnectionPostgreSQL.PASSWORD.getParameter()
				);				
				connection.setAutoCommit(false);				
				LogUtil.getLogger(SingletonConnection.class).info(EnumLogMessages.CONN_SUCCESS.getMessage());				
			}
		} catch (Exception e) {
			LogUtil.getLogger(SingletonConnection.class).error(EnumLogMessages.CONN_FAILED.getMessage());
			e.printStackTrace();			
		}
	}

	public SingletonConnection() {
		toConnect();
	}

	public static Connection getConnection() {
		return connection;
	}

}
