package com.github.tadeuespindolapalermo.main;

import com.github.tadeuespindolapalermo.connection.InfoConnection;
import com.github.tadeuespindolapalermo.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.dao.Persistence;
import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.model.Entity;

public class Main {

	public static void main(String[] args) {
		
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("postgres1985");
		InfoConnection.setUser("postgres");
		InfoConnection.setHost("127.0.0.1");
		InfoConnection.setPort("5432");	
		
		SingletonConnection.getConnection();
		
		Entity entity = new Entity();	
		
		Persistence persistence = new Persistence();
		String[] columns = {"id", "name", "lastname", "cpf", "weight", "approved", "age"};
		persistence.save(entity, "entyty", columns);		
	}

}
