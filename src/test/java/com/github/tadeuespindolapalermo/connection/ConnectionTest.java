package com.github.tadeuespindolapalermo.connection;

import org.junit.Test;

public class ConnectionTest {
	
	@Test
	public void init() {
		SingletonConnection.getConnection();
	}

}
