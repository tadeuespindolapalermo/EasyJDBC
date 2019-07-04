package com.github.tadeuespindolapalermo.enumeration;

public enum EnumConnectionPostgreSQL {	

	URL("jdbc:postgresql://localhost:5432/easyjdbc"),
	PASSWORD("postgres1985"),
	USER("postgres"),
	DRIVER("org.postgresql.Driver");	
	
	private final String parameter;

	private EnumConnectionPostgreSQL(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
