package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionSQLite {	

	URL("jdbc:sqlite:"),
	DRIVER("org.sqlite.JDBC"),
	FILE_PATH_DATABASE_DAFAULT("src/main/resources/data/"),
	FILE_DATABASE_DAFAULT("database.db"),;
	
	private final String parameter;

	private EnumConnectionSQLite(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
