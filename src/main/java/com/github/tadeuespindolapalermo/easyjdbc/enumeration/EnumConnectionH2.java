package com.github.tadeuespindolapalermo.easyjdbc.enumeration;

public enum EnumConnectionH2 {	

	DRIVER("org.h2.Driver"),
	URL("jdbc:h2:mem:test");
	
	private final String parameter;

	private EnumConnectionH2(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

}
