package com.github.tadeuespindolapalermo.connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.tadeuespindolapalermo.dao.Persistence;
import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.model.Entity;

public class OperationsTest {

	static {
		connection();
	}	

	private static void connection() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("postgres1985");
		InfoConnection.setUser("postgres");
		InfoConnection.setHost("127.0.0.1");
		InfoConnection.setPort("5432");
		SingletonConnection.getConnection();
	}

	@Test
	public void insertTest() throws Exception {
		Entity e = createEntity();
		Persistence<Entity> p = new Persistence<>();
		assertNotNull(p.save(e));
	}
	
	@Test
    public void deleteTest() throws Exception {
        Persistence<Entity> p = new Persistence<>();
        assertTrue(p.delete(Entity.class, 6L));
    }
	
	private Entity createEntity() {
		Entity e = new Entity();
		e.setAge(34);
		e.setApproved(true);
		e.setCpf("90253056012");
		e.setId(6L);
		e.setLastname("Espíndola Palermo");
		e.setName("Tadeu");
		e.setWeight(82D);
		return e;
	}

}
