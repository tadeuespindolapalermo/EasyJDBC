package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.connection.MountConnection.mountOracleURL;
import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionOracle;

public class OracleConnectionUrl extends AbstractConnectionUrl {
	
	@Override
	public String getUrl() throws ClassNotFoundException {
		Class.forName(EnumConnectionOracle.DRIVER.getParameter());					
		return isNull(InfoConnection.getUrl()) ? mountOracleURL(EnumConnectionOracle.URL.getParameter()) : InfoConnection.getUrl();
	}

}
