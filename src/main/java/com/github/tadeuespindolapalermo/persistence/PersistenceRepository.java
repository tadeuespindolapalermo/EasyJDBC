package com.github.tadeuespindolapalermo.persistence;

public interface PersistenceRepository {	
	
	void save (Object object, String table);
	void save (Object object, String table, String[] columns);

}
