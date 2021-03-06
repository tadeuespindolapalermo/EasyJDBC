package com.github.tadeuespindolapalermo.easyjdbc.crud;

import com.github.tadeuespindolapalermo.easyjdbc.operations.Operations;
import com.github.tadeuespindolapalermo.easyjdbc.repository.CrudRepository;

import java.sql.SQLException;
import java.util.Map;

public class Crud extends Operations implements CrudRepository {		
	
	@Override
	public boolean save(Map<String, ?> columnsAndValues, String tableName) throws SQLException {
	    String query = mountQueryInsert(columnsAndValues, tableName);		
		return processInsertUpdate(columnsAndValues, query);
	}	
	
	@Override
	public boolean update(Map<String, ?> columnsAndValues, Map<String, ?> clauseColumnAndValue, String tableName) throws SQLException {
		String query = mountQueryUpdate(columnsAndValues, clauseColumnAndValue, tableName);		
		return processInsertUpdate(columnsAndValues, query);		
	}
	
	@Override
	public boolean delete(String table, String column, Object value) throws SQLException {		
		String query = mountQueryDelete(table, column, value);		
		return processDelete(query);
	}	
	
}