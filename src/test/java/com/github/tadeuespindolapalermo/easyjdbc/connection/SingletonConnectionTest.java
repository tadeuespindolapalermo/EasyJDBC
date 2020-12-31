package com.github.tadeuespindolapalermo.easyjdbc.connection;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

public class SingletonConnectionTest {

	@Test
	public void connection() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("tadeu123");
		InfoConnection.setUser("postgres");
		InfoConnection.setHost("127.0.0.1");
		InfoConnection.setPort("5432");
		assertNotNull(SingletonConnection.getConnection());
	}

}
