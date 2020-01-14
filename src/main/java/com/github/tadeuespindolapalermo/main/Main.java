package com.github.tadeuespindolapalermo.main;

import com.github.tadeuespindolapalermo.connection.InfoConnection;
import com.github.tadeuespindolapalermo.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;

public class Main {

	public static void main(String[] args) {

		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("postgres1985");
		InfoConnection.setUser("postgres");
		InfoConnection.setHost("localhost");
		InfoConnection.setPort("5432");

		SingletonConnection.getConnection();
	}

}
