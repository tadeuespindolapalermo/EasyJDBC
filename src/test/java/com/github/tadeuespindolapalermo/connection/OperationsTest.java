package com.github.tadeuespindolapalermo.connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.tadeuespindolapalermo.dao.Persistence;
import com.github.tadeuespindolapalermo.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.model.Entity;

public class OperationsTest {
	
	private static final String[] COLUMNS = {"id", "name", "lastname", "cpf", "weight", "approved", "age"};
	
	private static final String ENTITY = "entity";

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
	public void insertTestA() throws Exception {
		Entity e = createEntityInsert();
		Persistence<Entity> p = new Persistence<>();
		assertNotNull(p.save(e));
	}
	
	@Test
	public void insertTestB() throws Exception {
		Entity e = createEntityInsert();
		Persistence<Entity> p = new Persistence<>();
		assertNotNull(p.save(e, ENTITY));
	}
	
	@Test
	public void insertTestC() throws Exception {
		Entity e = createEntityInsert();
		Persistence<Entity> p = new Persistence<>();		
		assertNotNull(p.save(e, COLUMNS));
	}
	
	@Test
	public void insertTestD() throws Exception {
		Entity e = createEntityInsert();
		Persistence<Entity> p = new Persistence<>();
		assertNotNull(p.save(e, ENTITY, COLUMNS));
	}
	
	@Test
	public void updateTest() throws Exception {
		Entity e = createEntityUpdate();
		Persistence<Entity> p = new Persistence<>();
		assertNotNull(p.update(e, 10L));
	}
	
	@Test
    public void deleteTest() throws Exception {
        Persistence<Entity> p = new Persistence<>();
        assertTrue(p.delete(Entity.class, 6L));
    }
	
	private Entity createEntityInsert() {
		Entity e = new Entity();
		e.setAge(34);
		e.setApproved(true);
		e.setCpf("90253056012");
		e.setId(12L);
		e.setLastname("Espíndola Palermo");
		e.setName("Tadeu");
		e.setWeight(82D);
		return e;
	}
	
	private Entity createEntityUpdate() {
		Entity e = new Entity();
		e.setAge(55);
		e.setApproved(false);
		e.setCpf("17731193039");
		e.setId(6L);
		e.setLastname("Pereira da Cruz");
		e.setName("Osvaldo");
		e.setWeight(105D);
		return e;
	}

}
