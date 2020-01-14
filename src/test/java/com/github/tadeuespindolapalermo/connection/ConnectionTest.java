package com.github.tadeuespindolapalermo.connection;

import org.junit.Test;

import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;

public class ConnectionTest {

	@Test
	public void init() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("postgres1985");
		InfoConnection.setUser("postgres");
		InfoConnection.setHost("127.0.0.1");
		InfoConnection.setPort("5432");
		SingletonConnection.getConnection();
	}

}
