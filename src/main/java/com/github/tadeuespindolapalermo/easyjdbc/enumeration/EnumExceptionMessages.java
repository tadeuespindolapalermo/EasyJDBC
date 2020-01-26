package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumExceptionMessages {
	
	NOT_PERSISTENT_CLASS("Class not persistent! If you want to persist the object, it is necessary to declare the @PersistentClass or @PersistentClassNamed annotation in the class concerning the entity in question!");	

	private final String message;

	private EnumExceptionMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
