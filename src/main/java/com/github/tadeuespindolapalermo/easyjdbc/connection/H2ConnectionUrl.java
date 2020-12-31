package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static com.github.tadeuespindolapalermo.easyjdbc.util.ValidatorUtil.isNull;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionH2;

public class H2ConnectionUrl extends AbstractConnectionUrl {
	
	@Override
	public String getUrl() throws ClassNotFoundException {
		Class.forName(EnumConnectionH2.DRIVER.getParameter());					
		return isNull(InfoConnection.getUrl()) ? EnumConnectionH2.URL.getParameter() : InfoConnection.getUrl();

	}

}
