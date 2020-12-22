package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumLogMessages {

	CONN_SUCCESS("Connection successful!"),
	CONN_FAILED("Failed to try to establish a connection to the database!");

	private final String message;

	EnumLogMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
