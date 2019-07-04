package com.github.tadeuespindolapalermo.enumeration;

public enum EnumLogMessages {

	CONN_SUCCESS("Connection successful!"),
	CONN_FAILED("Failed to try to establish a connection to the database!");

	private final String message;

	private EnumLogMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
