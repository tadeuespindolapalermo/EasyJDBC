package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionMySQL {	

	URL("jdbc:mysql://"),
	DRIVER_V5("com.mysql.jdbc.Driver"),
	DRIVER_V8("com.mysql.cj.jdbc.Driver"),
	PORT_DEFAULT("3306"),
	HOST_DEFAULT("127.0.0.1"),
	EXTRA_PARAMETER_USE_TIMEZONE_PT_BR("useTimezone=true"),
	EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR("serverTimezone=America/Sao_Paulo");
	
	private final String parameter;

	EnumConnectionMySQL(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
