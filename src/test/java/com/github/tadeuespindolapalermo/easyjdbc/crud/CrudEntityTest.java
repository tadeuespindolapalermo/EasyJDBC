package com.github.tadeuespindolapalermo.easyjdbc.crud;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.easyjdbc.entity.Entity;
import com.github.tadeuespindolapalermo.easyjdbc.entity.EntityNamed;
import com.github.tadeuespindolapalermo.easyjdbc.entity.EntityUniqueAttribute;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;
import com.github.tadeuespindolapalermo.easyjdbc.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CrudEntityTest {
	
	private static final String CPF_MOCK = "12121212121AAB7CY7T";	
	
	private static final String DESCRIPTION_MOCK = "Mock Description";

	@Before
	public void connectionPostgreSQLMain() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("tadeu123");
		InfoConnection.setUser("postgres");		
		SingletonConnection.getConnection();
	}
	
	private void connectionMySQL() {
		InfoConnection.setDatabase(EnumDatabase.MYSQL);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("mysql1985");
		InfoConnection.setUser("root");
		InfoConnection.setHost("127.0.0.1");
		InfoConnection.setPort("3306");		
		assertNotNull(SingletonConnection.getConnection());
	}
	
	private static void connectionPostgreSQL() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);		
		InfoConnection.setPassword("tadeu123");
		InfoConnection.setUser("postgres");			
		InfoConnection.setUrl("jdbc:postgresql://127.0.0.1:5432/easyjdbc");
		assertNotNull(SingletonConnection.getConnection());
	}	
	
	private static void connectionSQLite() {
		InfoConnection.setDatabase(EnumDatabase.SQLITE);			
		InfoConnection.setDatabaseFilePath("src/main/resources/data/");
		InfoConnection.setDatabaseFile("sqlite_database.db");
		assertNotNull(SingletonConnection.getConnection());
	}	
	
	@Test
	public void connectionMySQLTest() {
		connectionMySQL();
	}
	
	@Test
	public void connectionPostgreSQLTest() {
		connectionPostgreSQL();
	}	
	
	@Test
	public void connectionSQLiteTest() {
		connectionSQLite();
	}
	
	@Test
	public void savePersistenceEntityUniqueAttribute() throws Exception {
		EntityUniqueAttribute e = createEntityUniqueAttribute();
		CrudEntity<EntityUniqueAttribute> p = new CrudEntity<>(EntityUniqueAttribute.class);
		assertNotNull(p.save(e));
	}

	@Test
	public void savePersistenceClassTestA() throws Exception {
		Entity e = createEntityInsert();
		CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
		assertNotNull(p.save(e));
	}
	
	@Test
	public void savePersistenceClassNamedTest() throws Exception {
		EntityNamed e = createEntityNamedInsert();
		CrudEntity<EntityNamed> p = new CrudEntity<>(EntityNamed.class);
		assertNotNull(p.save(e));
	}	
	
	@Test
    public void deleteTest() throws Exception {
        CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
        List<Entity> entities = p.getAll();
        if (entities.size() == 0 ||entities.isEmpty()) {
        	Entity e = createEntityInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        boolean deleted = false;
        for (int i = 0; i <= entities.size(); i++) {
        	if (deleted)
        		break;
        	for (Entity entity : entities) {
        		deleted = p.deleteById(entity.getId());
        		break;
			}
        }        
        assertTrue(deleted);
    }
	
	@Test
    public void deleteEntityNamedTest() throws Exception {
        CrudEntity<EntityNamed> p = new CrudEntity<>(EntityNamed.class);
        List<EntityNamed> entities = p.getAll();
        if (entities.size() == 0 ||entities.isEmpty()) {
        	EntityNamed e = createEntityNamedInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        boolean deleted = false;
        for (int i = 0; i <= entities.size(); i++) {
        	if (deleted)
        		break;
        	for (EntityNamed entity : entities) {
        		deleted = p.deleteById(entity.getNumber());
        		break;
			}
        }        
        assertTrue(deleted);
    }
	
	@Test
	public void updateEntityTest() throws Exception {
		CrudEntity<Entity> p = new CrudEntity<>(Entity.class);		
		List<Entity> entities = p.getAll();
		Entity entityAssert = null;
        if (entities.size() == 0 ||entities.isEmpty()) {
        	Entity e = createEntityInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
		for (Entity entity : entities) {
			entityAssert = p.searchById(entity.getId());
			entityAssert = createEntityUpdate(entityAssert);
			entityAssert = p.update(entityAssert);
			break;
		}
		assertNotNull(entityAssert);
	}
	
	@Test
	public void seacrhByIdAndUpdateEntityTest() throws Exception {		
		CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
		List<Entity> entities = p.getAll();
        long idSearch = 0;
        if (entities.size() == 0 ||entities.isEmpty()) {
        	Entity e = createEntityInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        for (Entity entity : entities) {
			idSearch = entity.getId();
			break;
		}
        Entity e = p.searchById(idSearch);
		e.setCpf(CPF_MOCK);
		assertNotNull(p.update(e));
	}
	
	@Test
	public void seacrhByIdAndUpdateEntityClassNamedTest() throws Exception {		
		CrudEntity<EntityNamed> p = new CrudEntity<>(EntityNamed.class);		
		List<EntityNamed> entities = p.getAll();
		String idSearch = "";        
        if (entities.size() == 0 ||entities.isEmpty()) {
        	EntityNamed e = createEntityNamedInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        for (EntityNamed entity : entities) {
			idSearch = entity.getNumber();
			break;
		}
        EntityNamed e = p.searchById(idSearch);		
		e.setDescription(DESCRIPTION_MOCK);
		assertNotNull(p.update(e));
	}	
	
	@Test
    public void getAllEntityTest() throws Exception {
        CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
        Utils.print(p.getAll());
        assertNotNull(p.getAll());
    }	

    /*@Test
    public void getAllEntityQuestionsTest() throws Exception {
		CrudEntity<EntityQuestions> p = new CrudEntity<>(EntityQuestions.class);
        Utils.print(p.getAll());
        assertNotNull(p.getAll());
    }*/
	
	@Test
    public void searchTest() throws Exception {
		CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
        List<Entity> entities = p.getAll();
        String cpfSearch = null;
        if (entities.size() == 0 ||entities.isEmpty()) {
        	Entity e = createEntityInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        for (Entity entity : entities) {
			cpfSearch = entity.getCpf();
			break;
		}
        List<Entity> entity = p.search("SELECT * FROM entity WHERE cpf = '"+ cpfSearch +"'");
        Utils.print(entity);
        assertNotNull(entity);
    }
	
	@Test
	public void operateWithResultSetTest() throws Exception {
		CrudEntity<Entity> p = new CrudEntity<>(Entity.class);		
		assertTrue(p.operateWithResultSet(
			 "SELECT COUNT(1) AS qtde FROM entity WHERE approved = true").getInt("qtde") >= 0);		 
	}
	
	@Test
    public void searchByIdTest() throws Exception {
        CrudEntity<Entity> p = new CrudEntity<>(Entity.class);
        List<Entity> entities = p.getAll();
        long idSearch = 0;
        if (entities.size() == 0 ||entities.isEmpty()) {
        	Entity e = createEntityInsert();    		
    		p.save(e);
    		entities = p.getAll();
        }
        for (Entity entity : entities) {
			idSearch = entity.getId();
			break;
		}
        Utils.print(p.searchById(idSearch));
        assertNotNull(p.searchById(idSearch));
    }
	
	private Entity createEntityInsert() {
		Entity e = new Entity();
		e.setAge(55);
		e.setApproved(true);
		e.setCpf("90253056012");		
		e.setLastname("Espíndola Palermo");
		e.setName("Tadeu");
		e.setWeight(82D);
		e.setCurriculumEntity("UYT&jkh9878iuhBGUYGT&5t");
		return e;
	}
	
	private EntityUniqueAttribute createEntityUniqueAttribute() {
		EntityUniqueAttribute e = new EntityUniqueAttribute();
		e.setCanceled(true);
		return e;
	}	
	
	private EntityNamed createEntityNamedInsert() {
		EntityNamed e = new EntityNamed();
		Integer number = new Random().nextInt(999999999);		
		e.setDescription("Test");
		e.setNumber(number.toString());
		return e;
	}
	
	private Entity createEntityUpdate(Entity e) {		
		e.setAge(55);
		e.setApproved(false);
		e.setCpf("17731193039");		
		e.setLastname("ANAANAANAANAANAANAANA");
		e.setName("Osvaldooooooooo");
		e.setWeight(199D);
		e.setCurriculumEntity("iuyUKIH9787&iugIGU");
		return e;
	}

}
