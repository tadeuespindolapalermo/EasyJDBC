package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionMySQL {	

	URL("jdbc:mysql://"),
	DRIVER("com.mysql.cj.jdbc.Driver"),
	PORT_DAFAULT("3306"),
	HOST_DAFAULT("127.0.0.1"),
	EXTRA_PARAMETER_USE_TIMEZONE_PT_BR("useTimezone=true"),
	EXTRA_PARAMETER_SERVER_TIMEZONE_PT_BR("serverTimezone=America/Sao_Paulo");
	
	private final String parameter;

	private EnumConnectionMySQL(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
