package com.github.tadeuespindolapalermo.easyjdbc.operations;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;
import com.github.tadeuespindolapalermo.easyjdbc.repository.OperationsInterfaceRepository;

public class Persistence<T> extends OperationsUtils<T> implements OperationsInterfaceRepository<T> {	
	
	public Persistence(Class<T> entity) throws NotPersistentClassException {
		super(entity);		
	}

	@Override
	public T save(T entity) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {				
				
		String sql = mountSQLInsert(
				defineTableName(entity.getClass()),
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());	
		return entity;
	}	
	
	@Override
	public T save(T entity, String table) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String sql = mountSQLInsert(
				table, 
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T save(T entity, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String sql = mountSQLInsert(
				defineTableName(entity.getClass()),
				columns,
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T save(T entity, String table, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String sql = mountSQLInsert(table, columns, getAutoIncrementIdentifierValue());		
		processInsertUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T update(T entity, Long id)
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String sql = mountSQLUpdate(
				defineTableName(entity.getClass()), 				
				entity.getClass().getDeclaredFields(), 
				id,
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, sql, UPDATE, getAutoIncrementIdentifierValue());		
		return entity;
	}	
	
	@Override
	public T update(T entity)
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {	
		
		String sql = mountSQLUpdate(
				defineTableName(entity.getClass()), 				
				entity.getClass().getDeclaredFields(), 
				getIdValue(entity), 
				getIdName(entity.getClass()));
		
		processInsertUpdate(entity, sql, UPDATE, getAutoIncrementIdentifierValue());		
		return entity;
	}
	
	@Override
	public boolean delete(Object id) throws SQLException {		
		String sql = mountSQLDelete(defineTableName(entity), id);		
		return processDelete(sql);
	}
	
	@Override
	public List<T> getAll() 
			throws SQLException, InstantiationException, 
			IllegalAccessException, NoSuchMethodException, 
			InvocationTargetException {
		
		String sql = mountSQLGetAll(defineTableName(entity));		
		List<T> entities = new ArrayList<>();
		processSelect(sql, entities);
		return entities;
	}
	
	@Override
	public T searchById(Object id) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, 
			InstantiationException {			
		
		String sql = mountSQLSearchById(defineTableName(entity), id, getIdName(entity));		
		List<T> entities = new ArrayList<>();
		processSelect(sql, entities);
		return entities.get(SINGLE_ELEMENT_COLLECTION);
	}		
	
}
