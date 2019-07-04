package com.github.tadeuespindolapalermo.main;

import com.github.tadeuespindolapalermo.connection.SingletonConnection;

public class Main {

	public static void main(String[] args) {			
		SingletonConnection.getConnection();
	}

}
