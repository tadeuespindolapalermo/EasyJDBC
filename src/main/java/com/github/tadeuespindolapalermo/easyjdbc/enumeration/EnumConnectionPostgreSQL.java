package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionPostgreSQL {	

	URL("jdbc:postgresql://"),
	DRIVER("org.postgresql.Driver"),
	PORT_DAFAULT("5432"),
	HOST_DAFAULT("127.0.0.1");
	
	private final String parameter;

	private EnumConnectionPostgreSQL(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
