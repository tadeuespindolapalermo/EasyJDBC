package com.github.tadeuespindolapalermo.dao;

import java.lang.reflect.Field;
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
	public boolean delete(T t, Long id) throws SQLException {	
		
		String sql = mountSQLDelete(t.getClass().getSimpleName(), id);
		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			if(statement.executeUpdate() == 1) {
				connection.commit();
				return  Boolean.TRUE;						
			} else {			
				connection.rollback();
			}			
		}			
		return Boolean.FALSE;
	}	

	@Override
	public T save(T t) throws SQLException {	
		
		String sql = mountSQLInsert(t.getClass().getSimpleName().toLowerCase(), t.getClass().getDeclaredFields());
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			
			for (int i = 0; i <= t.getClass().getDeclaredFields().length - 1; i++) {
				if (t.getClass().getDeclaredFields()[i].getType().equals(Long.class)) {
					stmt.setLong(i + 1, 1);					
					break;
				}
			}			
			stmt.executeUpdate();
		}
		connection.commit();	
		return t;
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
			.append(" ")
			.append(" = ")
			.append(id);
		
		return sql.toString();
	}	

}
