package com.github.tadeuespindolapalermo.persistence;

import java.sql.SQLException;

public interface PersistenceRepository<T> {	
	
	void save (T t) throws SQLException;
	
	void save (Object object, String table) throws SQLException;
	
	void save (Object object, String[] columns) throws SQLException;
	
	void save (Object object, String table, String[] columns) throws SQLException;
	
	Boolean delete (T t, Long id) throws SQLException;

}
