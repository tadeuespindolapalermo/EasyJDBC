package com.github.tadeuespindolapalermo.easyjdbc.crud;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumConnectionH2;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class CrudTest {

	private static final String TABLE = "pessoa";

	@Before
	public void getConnection() throws Exception {
		InfoConnection.setDatabase(EnumDatabase.H2);
		InfoConnection.setUser("sa");
		InfoConnection.setPassword("");
		SingletonConnection.getConnection();
		loadDDL();
	}

	private void loadDDL() throws SQLException {
		RunScript.execute(EnumConnectionH2.URL.getParameter(), "sa", "", "src/test/resources/schema.sql", Charset.forName("UTF8"), false);
	}

	@Test
	public void saveTest() throws Exception {
		save();
	}

	private void save() throws SQLException {
		Crud p = new Crud();
		boolean save = p.save(createColumnsAndValues(), TABLE);
		assertTrue(save);
	}

	@Test
	public void updateTest() throws Exception {
		save();
		Crud p = new Crud();
		Map<String, String> clauseColumnAndValue = new HashMap<>();
		clauseColumnAndValue.put("cpf", "555.555.555-55");
		Map<String, Object> updateColumnsAndValues = updateColumnsAndValues();
		assertTrue(p.update(updateColumnsAndValues, clauseColumnAndValue, TABLE));
	}	

	@Test
	public void deleteTest() throws Exception {
		save();
		Crud p = new Crud();
		assertTrue(p.delete(TABLE, "lastname", "Palermo"));
	}

	private Map<String, Object> createColumnsAndValues() {
		Map<String, Object> columnsAndValues = new HashMap<>();
		columnsAndValues.put("id", new Random().nextInt(999999999));
		columnsAndValues.put("name", "Tadeu test without entity");
		columnsAndValues.put("lastname", "Palermo");
		columnsAndValues.put("cpf", "555.555.555-55");
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
		columnsAndValues.put("age", 88);
		return columnsAndValues;
	}

}
