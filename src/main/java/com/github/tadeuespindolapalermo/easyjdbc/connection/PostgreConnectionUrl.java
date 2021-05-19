package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.connection.MountConnection.mountPostgreSQLURL;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionPostgreSQL;

public class PostgreConnectionUrl implements ConnectionUrl {

	@Override
	public String getUrl() throws ClassNotFoundException {
		Class.forName(EnumConnectionPostgreSQL.DRIVER.getParameter());
		return isNull(InfoConnection.getUrl()) ? mountPostgreSQLURL(EnumConnectionPostgreSQL.URL.getParameter()) : InfoConnection.getUrl();
	}

}
