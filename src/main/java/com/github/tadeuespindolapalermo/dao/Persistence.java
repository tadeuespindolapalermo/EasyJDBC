package com.github.tadeuespindolapalermo.dao;

import static com.github.tadeuespindolapalermo.util.Utils.defineResultSetAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.tadeuespindolapalermo.annotation.Identifier;
import com.github.tadeuespindolapalermo.annotation.PersistentClass;
import com.github.tadeuespindolapalermo.annotation.PersistentClassNamed;
import com.github.tadeuespindolapalermo.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.enumeration.EnumExceptionMessages;
import com.github.tadeuespindolapalermo.exception.NotPersistentClass;
import com.github.tadeuespindolapalermo.persistence.PersistenceRepository;

public class Persistence<T> implements PersistenceRepository<T> {	
	
	private Connection connection;
	
	private Class<T> entity;
	
	private boolean flag;
	
	private static final String METHOD_GETTER_PREFIX = "get";
	
	private static final String METHOD_SETTER_PREFIX = "set";
	
	private static final String UPDATE = "update";
	
	private static final String INSERT = "insert";
	
	private static final int INDEX_DIFERENCE_UPDATE = 1;
	
	private static final int SINGLE_ELEMENT_COLLECTION = 0;
	
	private static final String STRING_EMPTY = "";
	
	private static final String WHERE_CLAUSE = " WHERE ";	
	
	public Persistence(Class<T> entity) throws NotPersistentClass {	
		validatePersistentClass(entity);	
		this.entity = entity;
		connection = SingletonConnection.getConnection();
	}
	
	@Override
	public T save(T entity) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClass {				
				
		String sql = mountSQLInsert(
				defineTableName(entity.getClass()),
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertionUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());	
		return entity;
	}	
	
	@Override
	public T save(T entity, String table) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClass {		
		
		String sql = mountSQLInsert(
				table, 
				entity.getClass().getDeclaredFields(), 
				getAutoIncrementIdentifierValue());
		
		processInsertionUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T save(T entity, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClass {		
		
		String sql = mountSQLInsert(
				defineTableName(entity.getClass()), 
				columns,
				getAutoIncrementIdentifierValue());
		
		processInsertionUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
		return entity;
	}
	
	@Override
	public T save(T entity, String table, String[] columns) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, NotPersistentClass {		
		
		String sql = mountSQLInsert(table, columns, getAutoIncrementIdentifierValue());
		
		processInsertionUpdate(entity, sql, INSERT, getAutoIncrementIdentifierValue());
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
		
		processInsertionUpdate(entity, sql, UPDATE, getAutoIncrementIdentifierValue());		
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
		
		processInsertionUpdate(entity, sql, UPDATE, getAutoIncrementIdentifierValue());		
		return entity;
	}
	
	@Override
	public boolean delete(Class<T> entity, Object id) throws SQLException {		
		String sql = mountSQLDelete(entity.getSimpleName().toLowerCase(), id);		
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			if(statement.executeUpdate() == 1) {
				connection.commit();
				return Boolean.TRUE;						
			}
		}			
		return Boolean.FALSE;
	}
	
	@Override
	public List<T> getAll() 
			throws SQLException, InstantiationException, 
			IllegalAccessException, NoSuchMethodException, 
			InvocationTargetException {
		
		String sql = mountSQLGetAll(defineTableName(entity));
		List<T> entities = new ArrayList<>();
		processSearch(sql, entities);
		return entities;
	}
	
	@Override
	public T searchById(Object id) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, 
			InstantiationException {			
		
		String sql = mountSQLSearchById(defineTableName(entity), id, getIdName(entity));
		List<T> entities = new ArrayList<>();
		processSearch(sql, entities);
		return entities.get(SINGLE_ELEMENT_COLLECTION);
	}	
	
	private Object getIdValue(T entity) throws IllegalAccessException, InvocationTargetException {
		for (Method m : entity.getClass().getDeclaredMethods()) {
			Identifier annotId = m.getDeclaredAnnotation(Identifier.class);	
			if (annotId != null) {
				return m.invoke(entity);
			}			
		}
		return new Object();
	}	
	
	private String getIdName(Class<?> entity) {
		for (Method m : entity.getDeclaredMethods()) {
			Identifier annotId = m.getDeclaredAnnotation(Identifier.class);	
			if (annotId != null) {
				return m.getName().replace("get", "").toLowerCase();
			}			
		}
		return STRING_EMPTY;
	}	
	
	private String defineTableName(Class<?> entity) {
		return !verifyEntityName(entity).isEmpty() 
				? verifyEntityName(entity) 
				: entity.getSimpleName().toLowerCase();
	}	
	
	private String verifyEntityName(Class<?> entity) {
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClassNamed != null) {
			return annotClassNamed.value();
		}
		return STRING_EMPTY;
	}
	
	private void validatePersistentClass(Class<T> entity) throws NotPersistentClass {		
		PersistentClass annotClass = entity.getDeclaredAnnotation(PersistentClass.class);
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClass == null && annotClassNamed == null) {
			throw new NotPersistentClass(EnumExceptionMessages.NOT_PERSISTENT_CLASS.getMessage());
		}	
	}
	
	private void processSearch(String sql, List<T> entities) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, 
			InstantiationException {	
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			try (ResultSet result = stmt.executeQuery()) {
				Field[] fields = entity.getDeclaredFields();
				while (result.next()) {					
					T entityResult = entity.newInstance();
					for (Field field : fields) {						
						Method methodSetAttribute = entityResult.getClass().getDeclaredMethod(getMethodSetterName(field), field.getType());
						methodSetAttribute.invoke(entityResult, defineResultSetAttribute(result, field));						
					}										
					entities.add(entityResult);
				}
			}
		}
	}

	private String getMethodSetterName(Field field) {
		String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
		String[] token = field.getName().split(firstCharacterUppercase.toLowerCase());	
		return METHOD_SETTER_PREFIX + firstCharacterUppercase + token[1];
	}

	private void processInsertionUpdate(T entity, String sql, String operation, boolean idAutoIncrement)
			throws SQLException, IllegalAccessException, 
			InvocationTargetException, NoSuchMethodException {
		
		Class<?> entityClass = entity.getClass();		
		
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {			
			for (int i = 0; i <= entity.getClass().getDeclaredFields().length - 1; i++) {
				if (operation.equals(UPDATE) && entity.getClass().getDeclaredFields()[i].getName().equals(getIdValue(entity))) {
					continue;
				}
				if (!(idAutoIncrement && entity.getClass().getDeclaredFields()[i].getName().equals(getIdName(entity.getClass()))))
					setStatement(entity, stmt, i, getMethodGetter(entity, entityClass, i).invoke(entity), operation, idAutoIncrement);
			}			
			stmt.executeUpdate();
		}
		connection.commit();
	}	

    private Method getMethodGetter(T entity, Class<?> entityClass, int i) throws NoSuchMethodException {
        Field field = entity.getClass().getDeclaredFields()[i];
        String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
        String[] token = field.getName().split(firstCharacterUppercase.toLowerCase());			    
        return entityClass.getDeclaredMethod(METHOD_GETTER_PREFIX + firstCharacterUppercase + token[1]);
    }
    
    private int computeIndexUpdate(int i, boolean idAutoIncrement) {
    	return i == 0 && !idAutoIncrement ? ++ i : ++ i - INDEX_DIFERENCE_UPDATE;
    } 
    
    private int computeIndexInsert(int i, boolean idAutoIncrement) {
    	return i != 0 && idAutoIncrement ? i : ++ i;
    } 

	private void setStatement(T entity, PreparedStatement stmt, int i, Object value, String operation, boolean idAutoIncrement) throws SQLException {
		
		if (operation.equals(UPDATE) 
				&& entity.getClass().getDeclaredFields()[i].getName().equals(getIdName(entity.getClass()))
				&& !idAutoIncrement) {
			flag = true;
			return;
		}
		
		if (flag)
			i --;
			
		int index = computeIndexInsert(i, idAutoIncrement);
		
	    if (operation.equals(UPDATE)) {
	    	index = computeIndexUpdate(i, idAutoIncrement);
	    	if (!idAutoIncrement && i != 0)
	    		index ++;
	    } 
	    
	    if (flag)
	    	i ++;
	    
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

	private String mountSQLInsert(String table, Field[] fields, boolean idAutoIncrement) {					

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();		

		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				if (idAutoIncrement && fields[i].getName().equals(getIdName(entity)))
					continue;
				columnsName.append(fields[i].getName()).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(fields[i].getName());
				values.append("?");
			}
		}
		
		StringBuilder sql = new StringBuilder("INSERT INTO ")		
			.append(table)
			.append(" (" + columnsName + ") ")
			.append("VALUES")
			.append(" (" + values + ") ");

		return sql.toString();
	}	
	
	private String mountSQLInsert(String table, String[] columns, boolean idAutoIncrement) {

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();
		
		int qtdColumns = columns.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {	
				if (idAutoIncrement && columns[i].equals(getIdName(entity)))
					continue;
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
	
	private String mountSQLUpdate(String table, Field[] fields, Long id, boolean idAutoIncrement) {
		
		StringBuilder columnsName = new StringBuilder();		
		
		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				if (idAutoIncrement && fields[i].getName().equals(getIdName(entity)))
					continue;
				columnsName.append(fields[i].getName()).append(" = ?").append(", ");
			} else {
				columnsName.append(fields[i].getName()).append(" = ?");
			}
		}

		StringBuilder sql = new StringBuilder("UPDATE ")		
			.append(table)
			.append(" SET ")
			.append(columnsName.toString())
			.append(" WHERE id = " + id);		

		return sql.toString().replace("id = ?,", "");		
	}
	
	private String mountSQLUpdate(String table, Field[] fields, Object idValue, String idName) {
		
		StringBuilder columnsName = new StringBuilder();		
		
		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				if (fields[i].getName().equals(getIdName(entity)))
					continue;
				columnsName.append(fields[i].getName()).append(" = ?").append(", ");
			} else {
				columnsName.append(fields[i].getName()).append(" = ?");
			}
		}

		StringBuilder sql = new StringBuilder("UPDATE ")		
			.append(table)
			.append(" SET ")
			.append(columnsName.toString())
			.append(WHERE_CLAUSE + idName + " = '" + idValue + "'");		

		return sql.toString().replace("id = ?,", "");		
	}
	
	private String mountSQLDelete(String table, Object id) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ")		
			.append(table)
			.append(WHERE_CLAUSE)
			.append(getIdName(entity))
			.append(" = ")
			.append("'" + id + "'");
		
		return sql.toString();
	}		
	
	private String mountSQLGetAll(String table) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table);			
		
		return sql.toString();
	}
	
	private String mountSQLSearchById(String table, Object idValue, String idName) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table)
			.append(WHERE_CLAUSE + idName + " = '")
			.append(idValue).append("'");			
		
		return sql.toString();
	}
	
	private boolean getAutoIncrementIdentifierValue() {
		boolean value = false;
		for (Method method : entity.getDeclaredMethods()) {
			Identifier annotAttribute = method.getDeclaredAnnotation(Identifier.class);
			if (annotAttribute != null) {
				value = annotAttribute.autoIncrement();
			}
		}
		return value;
	}

}
