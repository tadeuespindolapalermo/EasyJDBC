package com.github.tadeuespindolapalermo.enumeration;

public enum EnumConnectionOracle {	

	URL("jdbc:oracle:thin:@"),
	DRIVER("oracle.jdbc.OracleDriver"),
	PORT_DAFAULT("1158"),
	HOST_DAFAULT("127.0.0.1");
	
	private final String parameter;

	private EnumConnectionOracle(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
