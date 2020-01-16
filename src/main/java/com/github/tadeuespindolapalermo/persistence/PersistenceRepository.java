package com.github.tadeuespindolapalermo.persistence;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface PersistenceRepository<T> {	
	
	T save (T t) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
	
	void save (Object object, String table) throws SQLException;
	
	void save (Object object, String[] columns) throws SQLException;
	
	void save (Object object, String table, String[] columns) throws SQLException;
	
	boolean delete (Class<T> t, Long id) throws SQLException;

}
