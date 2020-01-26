package com.github.tadeuespindolapalermo.easyjdbc.operations;

import static com.github.tadeuespindolapalermo.easyjdbc.util.Utils.defineResultSetAttribute;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClass;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;
import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumExceptionMessages;
import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;
import com.github.tadeuespindolapalermo.easyjdbc.util.Utils;

public class OperationsUtils<T> {	
	
	protected Connection connection;
	
	protected Class<T> entity;
	
	protected boolean flag;
	
	protected static final String METHOD_GETTER_PREFIX = "get";
	
	protected static final String METHOD_SETTER_PREFIX = "set";
	
	protected static final String METHOD_GETTER_IS_PREFIX = "is";
	
	protected static final String UPDATE = "update";
	
	protected static final String INSERT = "insert";
	
	protected static final int INDEX_DIFERENCE_UPDATE = 1;
	
	protected static final int SINGLE_ELEMENT_COLLECTION = 0;
	
	protected static final int LIMIT_TOKEN_SPLIT = 2;
	
	protected static final String STRING_EMPTY = "";
	
	protected static final String WHERE_CLAUSE = " WHERE ";	
	
	public OperationsUtils(Class<T> entity) throws NotPersistentClassException {	
		validatePersistentClass(entity);	
		this.entity = entity;
		connection = SingletonConnection.getConnection();
	}	
	
	protected Object getIdValue(T entity) throws IllegalAccessException, InvocationTargetException {
		for (Method m : entity.getClass().getDeclaredMethods()) {
			Identifier annotId = m.getDeclaredAnnotation(Identifier.class);	
			if (annotId != null) {
				return m.invoke(entity);
			}			
		}
		return new Object();
	}	
	
	protected String getIdName(Class<?> entity) {
		for (Method m : entity.getDeclaredMethods()) {
			Identifier annotId = m.getDeclaredAnnotation(Identifier.class);	
			if (annotId != null) {
				return m.getName().replace("get", "").toLowerCase();
			}			
		}
		return STRING_EMPTY;
	}	
	
	protected String defineTableName(Class<?> entity) {
		return !verifyEntityName(entity).isEmpty() 
				? verifyEntityName(entity) 
				: entity.getSimpleName().toLowerCase();
	}	
	
	protected String verifyEntityName(Class<?> entity) {
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClassNamed != null) {
			return annotClassNamed.value();
		}
		return STRING_EMPTY;
	}
	
	protected void validatePersistentClass(Class<T> entity) throws NotPersistentClassException {		
		PersistentClass annotClass = entity.getDeclaredAnnotation(PersistentClass.class);
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClass == null && annotClassNamed == null) {
			throw new NotPersistentClassException(EnumExceptionMessages.NOT_PERSISTENT_CLASS.getMessage());
		}	
	}
	
	protected void processSearch(String sql, List<T> entities) 
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

	protected String getMethodSetterName(Field field) {
		String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
		String[] token = field.getName().split(firstCharacterUppercase.toLowerCase());	
		return METHOD_SETTER_PREFIX + firstCharacterUppercase + token[1];
	}

	protected void processInsertionUpdate(T entity, String sql, String operation, boolean idAutoIncrement)
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

    protected Method getMethodGetter(T entity, Class<?> entityClass, int i) throws NoSuchMethodException { // Tadeu
        Field field = entity.getClass().getDeclaredFields()[i];
        String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));        
        String[] token = field.getName().split(firstCharacterUppercase.toLowerCase(), LIMIT_TOKEN_SPLIT);	        
        String prefix = isTypeBooleanPrimitive(field.getType()) ? METHOD_GETTER_IS_PREFIX : METHOD_GETTER_PREFIX;
        return entityClass.getDeclaredMethod(prefix + firstCharacterUppercase + token[1]);
    }
    
    protected boolean isTypeBooleanPrimitive(Class<?> type) {
    	return Utils.verifyTypeBooleanPrimitive(type);
    }
    
    protected int computeIndexUpdate(int i, boolean idAutoIncrement) {
    	return i == 0 && !idAutoIncrement ? ++ i : ++ i - INDEX_DIFERENCE_UPDATE;
    } 
    
    protected int computeIndexInsert(int i, boolean idAutoIncrement) {
    	return i != 0 && idAutoIncrement ? i : ++ i;
    } 

	protected void setStatement(T entity, PreparedStatement stmt, int i, Object value, String operation, boolean idAutoIncrement) throws SQLException {
		
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
	    
		if (verifyTypeWrapperPrimitiveLong(entity, i)) {
			stmt.setLong(index, (Long) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveDouble(entity, i)) {
			stmt.setDouble(index, (Double) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveFloat(entity, i)) {
			stmt.setFloat(index, (Float) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveInteger(entity, i)) {
			stmt.setInt(index, (Integer) value);					
			return;
		}

		if (verifyTypeClassString(entity, i)) {
			stmt.setString(index, (String) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveBoolean(entity, i)) {
			stmt.setBoolean(index, (Boolean) value);					
			return;
		}

		if (verifyTypeWrapperPrimitiveShort(entity, i)) {
			stmt.setShort(index, (Short) value);				
		}
	}

	protected boolean verifyTypeWrapperPrimitiveLong(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Long.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(long.class);
	}
	
	protected boolean verifyTypeWrapperPrimitiveDouble(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Double.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(double.class);
	}
	
	protected boolean verifyTypeWrapperPrimitiveFloat(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Float.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(float.class);
	}
	
	protected boolean verifyTypeWrapperPrimitiveInteger(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Integer.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(int.class);
	}
	
	protected boolean verifyTypeWrapperPrimitiveBoolean(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Boolean.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(boolean.class);
	}
	
	protected boolean verifyTypeWrapperPrimitiveShort(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(Short.class)
				|| entity.getClass().getDeclaredFields()[i].getType().equals(short.class);
	}
	
	protected boolean verifyTypeClassString(T entity, int i) {
		return entity.getClass().getDeclaredFields()[i].getType().equals(String.class);
	}

	protected String mountSQLInsert(String table, Field[] fields, boolean idAutoIncrement) {					

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
	
	protected String mountSQLInsert(String table, String[] columns, boolean idAutoIncrement) {

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
	
	protected String mountSQLUpdate(String table, Field[] fields, Long id, boolean idAutoIncrement) {
		
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
	
	protected String mountSQLUpdate(String table, Field[] fields, Object idValue, String idName) {
		
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
	
	protected String mountSQLDelete(String table, Object id) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ")		
			.append(table)
			.append(WHERE_CLAUSE)
			.append(getIdName(entity))
			.append(" = ")
			.append("'" + id + "'");
		
		return sql.toString();
	}		
	
	protected String mountSQLGetAll(String table) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table);			
		
		return sql.toString();
	}
	
	protected String mountSQLSearchById(String table, Object idValue, String idName) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table)
			.append(WHERE_CLAUSE + idName + " = '")
			.append(idValue).append("'");			
		
		return sql.toString();
	}
	
	protected boolean getAutoIncrementIdentifierValue() {
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
