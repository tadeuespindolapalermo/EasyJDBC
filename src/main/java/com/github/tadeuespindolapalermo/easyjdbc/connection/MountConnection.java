package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNotNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionMySQL;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionOracle;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionPostgreSQL;

public class MountConnection {
	
	private static final String NO_EXTRA_PARAMETERS = "";
	
	private static final String ORACLE_HOST_DEFAULT = EnumConnectionOracle.HOST_DAFAULT.getParameter();
	private static final String ORACLE_PORT_DEFAULT = EnumConnectionOracle.PORT_DAFAULT.getParameter();
	
	private static final String POSTGRESQL_HOST_DEFAULT = EnumConnectionPostgreSQL.HOST_DAFAULT.getParameter();
	private static final String POSTGRESQL_PORT_DEFAULT = EnumConnectionPostgreSQL.PORT_DAFAULT.getParameter();
	
	private static final String MYSQL_HOST_DEFAULT = EnumConnectionMySQL.HOST_DAFAULT.getParameter();
	private static final String MYSQL_PORT_DEFAULT = EnumConnectionMySQL.PORT_DAFAULT.getParameter();	
	private static final String MYSQL_EXTRA_PARAMETER_USE_TIMEZONE_PT_BR = EnumConnectionMySQL.EXTRA_PARAMETER_USE_TIMEZONE_PT_BR.getParameter();
	private static final String MYSQL_EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR = EnumConnectionMySQL.EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR.getParameter();
	
	protected MountConnection () {}
	
	private static String mountURL(
			String initialURL,
			String enumConnectionHostParameter,
			String enumConnectionPortParameter,
			String... extraParametersInURL) {
		
		StringBuilder extraParameters = new StringBuilder();
		
		for (int i = 0; i <= extraParametersInURL.length - 1; i++) {			
			extraParameters.append(extraParametersInURL[i]);
			if (i != extraParametersInURL.length - 1)				
				extraParameters.append("&");
		}
		
		return new StringBuilder(initialURL)
			.append(isNotNull(InfoConnection.getHost()) 
				? InfoConnection.getHost() : enumConnectionHostParameter)
			.append(":")
			.append(isNotNull(InfoConnection.getPort()) 
				? InfoConnection.getPort() : enumConnectionPortParameter)
			.append("/")
			.append(InfoConnection.getNameDatabase())
			.append("?")
			.append(extraParameters.toString())
			.toString();
	}
	
	protected static String mountOracleURL(String initialURL) {		
		return mountURL(initialURL, ORACLE_HOST_DEFAULT, ORACLE_PORT_DEFAULT, NO_EXTRA_PARAMETERS);		
	}
	
	protected static String mountPostgreSQLURL(String initialURL) {			
		return mountURL(initialURL, POSTGRESQL_HOST_DEFAULT, POSTGRESQL_PORT_DEFAULT, NO_EXTRA_PARAMETERS);		
	}	
	
	protected static String mountMySQLURL(String initialURL) {			
		return mountURL(initialURL, MYSQL_HOST_DEFAULT, MYSQL_PORT_DEFAULT, MYSQL_EXTRA_PARAMETER_USE_TIMEZONE_PT_BR, MYSQL_EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR);
	}		

}
