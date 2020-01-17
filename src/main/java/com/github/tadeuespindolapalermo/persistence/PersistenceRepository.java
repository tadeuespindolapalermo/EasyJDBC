package com.github.tadeuespindolapalermo.persistence;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface PersistenceRepository<T> {	
	
	T save (T entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	T save (T entity, String table) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	T save (T entity, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	T save (T entity, String table, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	boolean delete (Class<T> t, Long id) throws SQLException;
	
	T update (T entity, Long id) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;

}
