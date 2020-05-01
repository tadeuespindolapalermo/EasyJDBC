package com.github.tadeuespindolapalermo.easyjdbc.repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;

public interface CrudRepository {	
	
	boolean save(Map<String, ?> columnsAndValues, String tableName) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	boolean update(Map<String, ?> columnsAndValues, Map<String, ?> clauseColumnAndValue, String tableName) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	boolean delete(String table, String column, Object value) throws SQLException;	
	
}
