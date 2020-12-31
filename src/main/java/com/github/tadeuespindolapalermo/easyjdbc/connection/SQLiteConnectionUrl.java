package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.connection.MountConnection.mountSQLiteURL;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionSQLite;

public class SQLiteConnectionUrl extends AbstractConnectionUrl {
	
	@Override
	public String getUrl() throws ClassNotFoundException {
		Class.forName(EnumConnectionSQLite.DRIVER.getParameter());					
		return isNull(InfoConnection.getUrl()) ? mountSQLiteURL(EnumConnectionSQLite.URL.getParameter()) : InfoConnection.getUrl();

	}

}
