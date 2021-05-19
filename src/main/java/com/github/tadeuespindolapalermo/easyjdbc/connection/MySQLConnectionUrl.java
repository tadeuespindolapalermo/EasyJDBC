package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.connection.MountConnection.mountMySQLURL;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionMySQL;

public class MySQLConnectionUrl implements ConnectionUrl {
	
	@Override
	public String getUrl() throws ClassNotFoundException {
		Class.forName(EnumConnectionMySQL.DRIVER_V8.getParameter());
		return isNull(InfoConnection.getUrl()) ? mountMySQLURL(EnumConnectionMySQL.URL.getParameter()) : InfoConnection.getUrl();
	}

}
