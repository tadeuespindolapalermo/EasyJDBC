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
	
	public Persistence() {
		connection = SingletonConnection.getConnection();
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

	@Override
	public T save(T entity) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {	
		
		String sql = mountSQLInsert(entity.getClass().getSimpleName().toLowerCase(), entity.getClass().getDeclaredFields());		
		
		Class<?> entityClass = entity.getClass();
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {			
			for (int i = 0; i <= entity.getClass().getDeclaredFields().length - 1; i++) {			   	   
				setStatement(entity, stmt, i, getMethod(entity, entityClass, i).invoke(entity));
			}			
			stmt.executeUpdate();
		}
		connection.commit();	
		return entity;
	}

    private Method getMethod(T t, Class<?> tClass, int i) throws NoSuchMethodException {
        Field field = t.getClass().getDeclaredFields()[i];
        String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
        String[] token = field.getName().split(firstCharacterUppercase.toLowerCase());			    
        return tClass.getDeclaredMethod("get" + firstCharacterUppercase + token[1]);
    }

	private void setStatement(T entity, PreparedStatement stmt, int i, Object value) throws SQLException {
	  
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Long.class)) {
			stmt.setLong(i + 1, (Long) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Double.class)) {
			stmt.setDouble(i + 1, (Double) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Float.class)) {
			stmt.setFloat(i + 1, (Float) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Integer.class)) {
			stmt.setInt(i + 1, (Integer) value);					
			return;
		}

		if (entity.getClass().getDeclaredFields()[i].getType().equals(String.class)) {
			stmt.setString(i + 1, (String) value);					
			return;
		}
		
		if (entity.getClass().getDeclaredFields()[i].getType().equals(Boolean.class)) {
			stmt.setBoolean(i + 1, (Boolean) value);					
			return;
		}

		if (entity.getClass().getDeclaredFields()[i].getType().equals(Short.class)) {
			stmt.setShort(i + 1, (Short) value);			
		}		
	}
	
	@Override
	public void save(Object object, String[] columns) {
		mountSQLInsert(object.getClass().getSimpleName(), columns);
	}
	
	@Override
	public void save(Object object, String table) {
		mountSQLInsert(table, object.getClass().getDeclaredFields());
	}
	
	@Override
	public void save(Object object, String table, String[] columns) {
		mountSQLInsert(table, columns);		
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
