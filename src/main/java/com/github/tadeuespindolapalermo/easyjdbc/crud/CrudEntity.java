package com.github.tadeuespindolapalermo.easyjdbc.crud;

import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;
import com.github.tadeuespindolapalermo.easyjdbc.operations.OperationsEntity;
import com.github.tadeuespindolapalermo.easyjdbc.repository.CrudEntityRepository;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrudEntity<T> extends OperationsEntity<T> implements CrudEntityRepository<T> {	
	
	public CrudEntity(Class<T> entity) throws NotPersistentClassException {
		super(entity);		
	}		

	@Override
	public <E extends T> E save(E entity) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
				
		String query = mountQueryInsert(
				defineTableName(entity.getClass()),
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertUpdate(entity, query, INSERT, getAutoIncrementIdentifierValue());	
		return entity;
	}		
	
	@Override
	public <E extends T> E update(E entity)
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
	public boolean deleteById(Object id) throws SQLException {		
		String query = mountQueryDelete(defineTableName(entity), id);		
		return processDeleteById(query);
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