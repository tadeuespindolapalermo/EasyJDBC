package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionOracle {	

	URL("jdbc:oracle:thin:@"),
	DRIVER("oracle.jdbc.OracleDriver"),
	PORT_DEFAULT("1158"),
	HOST_DEFAULT("127.0.0.1");
	
	private final String parameter;

	EnumConnectionOracle(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
