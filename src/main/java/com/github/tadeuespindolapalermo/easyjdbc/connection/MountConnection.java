package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNotNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionMySQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionOracle;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionPostgreSQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionSQLite;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

public class MountConnection {	
	
	private static final String ORACLE_HOST_DEFAULT = EnumConnectionOracle.HOST_DEFAULT.getParameter();
	private static final String ORACLE_PORT_DEFAULT = EnumConnectionOracle.PORT_DEFAULT.getParameter();
	
	private static final String POSTGRESQL_HOST_DEFAULT = EnumConnectionPostgreSQL.HOST_DEFAULT.getParameter();
	private static final String POSTGRESQL_PORT_DEFAULT = EnumConnectionPostgreSQL.PORT_DEFAULT.getParameter();
	
	private static final String MYSQL_HOST_DEFAULT = EnumConnectionMySQL.HOST_DEFAULT.getParameter();
	private static final String MYSQL_PORT_DEFAULT = EnumConnectionMySQL.PORT_DEFAULT.getParameter();
	private static final String MYSQL_EXTRA_PARAMETER_USE_TIMEZONE_PT_BR = EnumConnectionMySQL.EXTRA_PARAMETER_USE_TIMEZONE_PT_BR.getParameter();
	private static final String MYSQL_EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR = EnumConnectionMySQL.EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR.getParameter();
	
	private static final String SQLITE_FILE_PATH_DATABASE_DEFAULT = EnumConnectionSQLite.FILE_PATH_DATABASE_DEFAULT.getParameter();
	private static final String SQLITE_FILE_DATABASE_DEFAULT = EnumConnectionSQLite.FILE_DATABASE_DEFAULT.getParameter();
	
	protected MountConnection () {}
	
	private static String mountURL(			
			String initialURL,
			String hostOrFilePathDatabaseDefault,
			String portOrFileDatabaseDefault,
			String... extraParametersInURL) {
		
		StringBuilder extraParameters = new StringBuilder();
		StringBuilder stringConnection = new StringBuilder(initialURL);
		
		for (int i = 0; i <= extraParametersInURL.length - 1; i++) {			
			extraParameters.append(extraParametersInURL[i]);
			if (i != extraParametersInURL.length - 1)				
				extraParameters.append("&");
		}
		
		if (!InfoConnection.getDatabase().equals(EnumDatabase.SQLITE)) {
			stringConnection
				.append(isNotNull(InfoConnection.getHost()) 
					? InfoConnection.getHost() : hostOrFilePathDatabaseDefault)
				.append(":")
				.append(isNotNull(InfoConnection.getPort()) 
					? InfoConnection.getPort() : portOrFileDatabaseDefault)
				.append("/")
				.append(InfoConnection.getNameDatabase());
		} else {
			stringConnection
				.append(isNotNull(InfoConnection.getDatabaseFilePath()) 
					? InfoConnection.getDatabaseFilePath() : hostOrFilePathDatabaseDefault)				
				.append(isNotNull(InfoConnection.getDatabaseFile()) 
					? InfoConnection.getDatabaseFile() : portOrFileDatabaseDefault);				
		}
		
		if (!extraParameters.toString().isEmpty()) { 
			stringConnection
				.append("?")
				.append(extraParameters.toString());
		}			
		return stringConnection.toString();
	}
	
	protected static String mountOracleURL(String initialURL) {		
		return mountURL(initialURL, ORACLE_HOST_DEFAULT, ORACLE_PORT_DEFAULT);		
	}
	
	protected static String mountPostgreSQLURL(String initialURL) {			
		return mountURL(initialURL, POSTGRESQL_HOST_DEFAULT, POSTGRESQL_PORT_DEFAULT);		
	}	
	
	protected static String mountMySQLURL(String initialURL) {			
		return mountURL(initialURL, MYSQL_HOST_DEFAULT, MYSQL_PORT_DEFAULT, MYSQL_EXTRA_PARAMETER_USE_TIMEZONE_PT_BR, MYSQL_EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR);
	}	
	
	protected static String mountSQLiteURL(String initialURL) {			
		return mountURL(initialURL, SQLITE_FILE_PATH_DATABASE_DEFAULT, SQLITE_FILE_DATABASE_DEFAULT);
	}	

}
