package com.github.tadeuespindolapalermo.easyjdbc.crud;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

public class CrudTest {

	private static final String TABLE = "entity";

	static {
		connectionTest();
	}

	private static void connectionTest() {
		connectionPostgreSQLMain();
	}

	private static void connectionPostgreSQLMain() {
		InfoConnection.setDatabase(EnumDatabase.POSTGRE);
		InfoConnection.setNameDatabase("easyjdbc");
		InfoConnection.setPassword("postgres1985");
		InfoConnection.setUser("postgres");
		SingletonConnection.getConnection();
	}

	@Test
	public void saveTest() throws Exception {
		Crud p = new Crud();
		assertTrue(p.save(createColumnsAndValues(), TABLE));
	}

	@Test
	public void updateTest() throws Exception {
		Crud p = new Crud();
		Map<String, String> clauseColumnAndValue = new HashMap<>();
		clauseColumnAndValue.put("cpf", "555.555.555-55");
		assertTrue(p.update(updateColumnsAndValues(), clauseColumnAndValue, TABLE));
	}	

	@Test
	public void deleteTest() throws Exception {
		Crud p = new Crud();
		assertTrue(p.delete(TABLE, "lastname", "Palermo"));
	}

	private Map<String, Object> createColumnsAndValues() {
		Map<String, Object> columnsAndValues = new HashMap<>();
		columnsAndValues.put("id", new Random().nextInt(999999999));
		columnsAndValues.put("name", "Tadeu test without entity");
		columnsAndValues.put("lastname", "Palermo");
		columnsAndValues.put("cpf", "645.985.326-56");
		columnsAndValues.put("weight", 35.5);
		columnsAndValues.put("approved", true);
		columnsAndValues.put("age", 35);
		columnsAndValues.put("curriculum", "test curriculum");
		return columnsAndValues;
	}

	private Map<String, Object> updateColumnsAndValues() {
		Map<String, Object> columnsAndValues = new HashMap<>();
		columnsAndValues.put("name", "UUUUUUUUUUUUUUU");
		columnsAndValues.put("lastname", "UUUUUUUUUUUUUUUUUU");
		columnsAndValues.put("cpf", "555.555.555-55");
		columnsAndValues.put("age", 88);
		return columnsAndValues;
	}

}
