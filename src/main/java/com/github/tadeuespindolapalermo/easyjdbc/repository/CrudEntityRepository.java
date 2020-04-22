package com.github.tadeuespindolapalermo.easyjdbc.repository;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;

public interface CrudEntityRepository<T> {	
	
	<E extends T> E save (E entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NotPersistentClassException;
	
	boolean deleteById (Object id) throws SQLException;	
	
	<E extends T> E update (E entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	List<T> getAll() throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	T searchById(Object id) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	List<T> search(String query) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
	
	ResultSet operateWithResultSet(String query) throws SQLException; 

}
