package com.github.tadeuespindolapalermo.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.github.tadeuespindolapalermo.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.persistence.PersistenceRepository;

public class Persistence<T> implements PersistenceRepository<T> {	
	
	private Connection connection;
	
	private static final String METHOD_GETTER_PREFIX = "get";
	
	private static final String UPDATE = "update";
	
	private static final String INSERT = "insert";
	
	private static final int INDEX_DIFERENCE_UPDATE = 1;
	
	public Persistence() {
		connection = SingletonConnection.getConnection();
	}
	
	@Override
	public T save(T entity) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {	
		
		String sql = mountSQLInsert(
				entity.getClass().getSimpleName().toLowerCase(), 
				entity.getClass().getDeclaredFields());	
		
		processInsertion(entity, sql, INSERT);	
		return entity;
	}
	
	@Override
	public T save(T entity, String table) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String sql = mountSQLInsert(table, entity.getClass().getDeclaredFields());
		processInsertion(entity, sql, INSERT);
		return entity;
	}
	
	@Override
	public T save(T entity, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String sql = mountSQLInsert(entity.getClass().getSimpleName(), columns);
		processInsertion(entity, sql, INSERT);
		return entity;
	}
	
	@Override
	public T save(T entity, String table, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String sql = mountSQLInsert(table, columns);
		processInsertion(entity, sql, INSERT);
		return entity;
	}
	
	@Override
	public T update(T entity, Long id)
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException {
		
		String sql = mountSQLUpdate(
				entity.getClass().getSimpleName().toLowerCase(), 				
				entity.getClass().getDeclaredFields(), id);
		
		processInsertion(entity, sql, UPDATE);		
		return entity;
	}	
	
	@Override
	public boolean delete(Class<T> t, Long id) throws SQLException {		
		String sql = mountSQLDelete(t.getSimpleName().toLowerCase(), id);		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			if(statement.executeUpdate() == 1) {
				connection.commit();
				return Boolean.TRUE;						
			}
		}			
		return Boolean.FALSE;
	}	

	private void processInsertion(T entity, String sql, String operation)
			throws SQLException, IllegalAccessException, 
			InvocationTargetException, NoSuchMethodException {
		
		Class<?> entityClass = entity.getClass();		
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {			
			for (int i = 0; i <= entity.getClass().getDeclaredFields().length - 1; i++) {
				if (operation.equals(UPDATE) && entity.getClass().getDeclaredFields()[i].getName().equals("id")) {
					continue;
				}
				setStatement(entity, stmt, i, getMethod(entity, entityClass, i).invoke(entity), operation);
			}			
			stmt.executeUpdate();
		}
		connection.commit();
	}

    private Method getMethod(T t, Class<?> tClass, int i) throws NoSuchMethodException {
        Field field = t.getClass().getDeclaredFields()[i];
        String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
        String[] token = field.getName().split(firstCharacterUppercase.toLowerCase());			    
        return tClass.getDeclaredMethod(METHOD_GETTER_PREFIX + firstCharacterUppercase + token[1]);
    }
    
    private int computeIndexUpdate(int i) {
    	return ++ i - INDEX_DIFERENCE_UPDATE;
    } 
    
    private int computeIndexInsert(int i) {
    	return ++ i;
    } 

	private void setStatement(T entity, PreparedStatement stmt, int i, Object value, String operation) throws SQLException {
		
		int index = computeIndexInsert(i);
		
	    if (operation.equals(UPDATE)) {
	    	index = computeIndexUpdate(i);
	    } 
	    
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Long.class)) {
			stmt.setLong(index, (Long) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Double.class)) {
			stmt.setDouble(index, (Double) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Float.class)) {
			stmt.setFloat(index, (Float) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Integer.class)) {
			stmt.setInt(index, (Integer) value);					
			return;
		}

		if (entity.getClass().getDeclaredFields()[i].getType().equals(String.class)) {
			stmt.setString(index, (String) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Boolean.class)) {
			stmt.setBoolean(index, (Boolean) value);					
			return;
		}

		if (entity.getClass().getDeclaredFields()[i].getType().equals(Short.class)) {
			stmt.setShort(index, (Short) value);			
		}		
	}

	private String mountSQLInsert(String table, Field[] fields) {

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();
		
		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				columnsName.append(fields[i].getName()).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(fields[i].getName());
				values.append("?");
			}
		}

		StringBuilder sql = new StringBuilder("INSERT INTO ")		
			.append(table)
			.append(" (" + columnsName.toString() + ") ")
			.append("VALUES")
			.append(" (" + values.toString() + ") ");

		return sql.toString();
	}
	
	private String mountSQLInsert(String table, String[] columns) {

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();
		
		int qtdColumns = columns.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				columnsName.append(columns[i]).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(columns[i]);
				values.append("?");
			}
		}

		StringBuilder sql = new StringBuilder("INSERT INTO ")		
			.append(table)
			.append(" (" + columnsName.toString() + ") ")
			.append("VALUES")
			.append(" (" + values.toString() + ") ");

		return sql.toString();
	}	
	
	private String mountSQLUpdate(String table, Field[] fields, Long id) {
		
		StringBuilder columnsName = new StringBuilder();		
		
		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				columnsName.append(fields[i].getName()).append(" = ?").append(", ");
			} else {
				columnsName.append(fields[i].getName()).append(" = ?");
			}
		}

		StringBuilder sql = new StringBuilder("UPDATE ")		
			.append(table)
			.append(" SET")
			.append(columnsName.toString())
			.append(" WHERE id = " + id);		

		return sql.toString().replace("id = ?,", "");		
	}
	
	private String mountSQLDelete(String table, Long id) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ")		
			.append(table)
			.append(" WHERE ")
			.append("id")
			.append(" = ")
			.append(id);
		
		return sql.toString();
	}	

}
