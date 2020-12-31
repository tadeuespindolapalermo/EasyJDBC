package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionSQLite {	

	URL("jdbc:sqlite:"),
	DRIVER("org.sqlite.JDBC"),
	FILE_PATH_DATABASE_DEFAULT("src/main/resources/data/"),
	FILE_DATABASE_DEFAULT("database.db"),;
	
	private final String parameter;

	EnumConnectionSQLite(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
