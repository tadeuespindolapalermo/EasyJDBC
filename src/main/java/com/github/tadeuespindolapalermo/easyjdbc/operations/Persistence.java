package com.github.tadeuespindolapalermo.easyjdbc.operations;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
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
	public <E extends T> E save(E entity) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {				
				
		String query = mountQueryInsert(
				defineTableName(entity.getClass()),
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, query, INSERT, getAutoIncrementIdentifierValue());	
		return entity;
	}	
	
	@Override
	public <E extends T> E save(E entity, String table) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String query = mountQueryInsert(
				table, 
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, query, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public <E extends T> E save(E entity, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String query = mountQueryInsert(
				defineTableName(entity.getClass()),
				columns,
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, query, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public <E extends T> E save(E entity, String table, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClassException {		
		
		String query = mountQueryInsert(table, columns, getAutoIncrementIdentifierValue());		
		processInsertUpdate(entity, query, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T update(T entity, Long id)
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String query = mountQueryUpdate(
				defineTableName(entity.getClass()), 				
				entity.getClass().getDeclaredFields(), 
				id,
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, query, UPDATE, getAutoIncrementIdentifierValue());		
		return entity;
	}	
	
	@Override
	public T update(T entity)
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {	
		
		String query = mountQueryUpdate(
				defineTableName(entity.getClass()), 				
				entity.getClass().getDeclaredFields(), 
				getIdValue(entity), 
				getIdName(entity.getClass()));
		
		processInsertUpdate(entity, query, UPDATE, getAutoIncrementIdentifierValue());		
		return entity;
	}
	
	@Override
	public boolean delete(Object id) throws SQLException {		
		String query = mountQueryDelete(defineTableName(entity), id);		
		return processDelete(query);
	}
	
	@Override
	public List<T> getAll() 
			throws SQLException, InstantiationException, 
			IllegalAccessException, NoSuchMethodException, 
			InvocationTargetException {
		
		String query = mountQueryGetAll(defineTableName(entity));		
		List<T> entities = new ArrayList<>();
		processSelect(query, entities);
		return entities;
	}
	
	@Override
	public T searchById(Object id) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, 
			InstantiationException {			
		
		String query = mountQuerySearchById(defineTableName(entity), id, getIdName(entity));		
		List<T> entities = new ArrayList<>();
		processSelect(query, entities);
		return entities.get(SINGLE_ELEMENT_COLLECTION);
	}	

	@Override
	public List<T> search(String query) 
			throws SQLException, InstantiationException, 
			IllegalAccessException, NoSuchMethodException, 
			InvocationTargetException {
		
		List<T> entities = new ArrayList<>();
		processSelect(query, entities);
		return entities;		
	}

	@Override
	public ResultSet operateWithResultSet(String query) throws SQLException {
		return processOperateWithResultSet(query);
	}
	
}