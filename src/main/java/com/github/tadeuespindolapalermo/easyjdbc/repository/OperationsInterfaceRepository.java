package com.github.tadeuespindolapalermo.easyjdbc.repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;

public interface OperationsInterfaceRepository<T> {	
	
	<E extends T> E save (E entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;	
	
	<E extends T> E save (E entity, String table) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	<E extends T> E save (E entity, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	<E extends T> E save (E entity, String table, String[] columns) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	boolean delete (Object id) throws SQLException;
	
	T update (T entity, Long id) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	T update (T entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	List<T> getAll() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	T searchById(Object id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	List<T> search(String query) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	ResultSet operateWithResultSet(String query) throws SQLException; 

}
