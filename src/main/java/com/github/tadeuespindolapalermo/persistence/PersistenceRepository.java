package com.github.tadeuespindolapalermo.persistence;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import com.github.tadeuespindolapalermo.exception.NotPersistentClass;

public interface PersistenceRepository<T> {	
	
	T save (T entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClass;
	
	T save (T entity, String table) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClass;
	
	T save (T entity, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClass;
	
	T save (T entity, String table, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClass;
	
	boolean delete (Class<T> t, Object id) throws SQLException;
	
	T update (T entity, Long id) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	T update (T entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	List<T> getAll() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	T searchById(Object id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

}
