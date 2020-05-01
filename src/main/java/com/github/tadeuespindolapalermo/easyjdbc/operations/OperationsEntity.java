package com.github.tadeuespindolapalermo.easyjdbc.operations;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.ColumnConfig;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.NotColumn;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClass;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;
import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumExceptionMessages;
import com.github.tadeuespindolapalermo.easyjdbc.exception.NotPersistentClassException;

public class OperationsEntity<T> {	
	
	protected Connection connection;
	
	protected Class<T> entity;
	
	private boolean flag;
	
	private static final String METHOD_GETTER_PREFIX = "get";
	
	private static final String METHOD_SETTER_PREFIX = "set";
	
	private static final String METHOD_GETTER_IS_PREFIX = "is";
	
	protected static final String UPDATE = "update";
	
	protected static final String INSERT = "insert";
	
	private static final int INDEX_DIFERENCE_UPDATE = 1;
	
	protected static final int SINGLE_ELEMENT_COLLECTION = 0;
	
	private static final int LIMIT_TOKEN_SPLIT = 2;
	
	private static final String STRING_EMPTY = "";
	
	private static final String WHERE_CLAUSE = " WHERE ";	
	
	private static final String BOOLEAN = "boolean";
	
	private static final String SERIAL_VERSION_UID = "serialVersionUID";
	
	protected static final String INSERT_INTO = "INSERT INTO ";
	
	public OperationsEntity(Class<T> entity) throws NotPersistentClassException {	
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
	
	private String verifyEntityName(Class<?> entity) {
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClassNamed != null) {
			return annotClassNamed.value();
		}
		return STRING_EMPTY;
	}
	
	private void validatePersistentClass(Class<T> entity) throws NotPersistentClassException {		
		PersistentClass annotClass = entity.getDeclaredAnnotation(PersistentClass.class);
		PersistentClassNamed annotClassNamed = entity.getDeclaredAnnotation(PersistentClassNamed.class);
		if (annotClass == null && annotClassNamed == null) {
			throw new NotPersistentClassException(EnumExceptionMessages.NOT_PERSISTENT_CLASS.getMessage());
		}	
	}
	
	protected void processSelect(String query, List<T> entities) 
			throws SQLException, NoSuchMethodException, 
			IllegalAccessException, InvocationTargetException, 
			InstantiationException {	
		
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			try (ResultSet result = stmt.executeQuery()) {
				Field[] fields = removeSerialVersionUIDAttribute(entity.getDeclaredFields());
				fields = removeAttributeNotColumn(fields);
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
	
	protected ResultSet processOperateWithResultSet(String query) throws SQLException  {		
	    PreparedStatement statement = connection.prepareStatement(query);
		ResultSet result = statement.executeQuery() ;
		if (result.next()) {
		    return result;
		}		
		return null;
	}
	
	protected boolean processDeleteById(String query) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			if(statement.executeUpdate() == 1) {
				connection.commit();
				return Boolean.TRUE;						
			}
		}			
		return Boolean.FALSE;
	}

	private String getMethodSetterName(Field field) {
		String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
		String[] token = field.getName().split(firstCharacterUppercase.toLowerCase(), LIMIT_TOKEN_SPLIT);	
		return METHOD_SETTER_PREFIX + firstCharacterUppercase + token[1];
	}

	protected void processInsertUpdate(T entity, String query, String operation, boolean idAutoIncrement)
			throws SQLException, IllegalAccessException, 
			InvocationTargetException, NoSuchMethodException {
		
		Class<?> entityClass = entity.getClass();		
		
		Field[] fields = removeSerialVersionUIDAttribute(entity.getClass().getDeclaredFields());
		fields = removeAttributeNotColumn(fields);
		
		try (PreparedStatement stmt = connection.prepareStatement(query)) {			
			for (int i = 0; i <= fields.length - 1; i++) {
				if (operation.equals(UPDATE) && fields[i].getName().equals(getIdValue(entity))) {
					continue;
				}
				if (!(idAutoIncrement && fields[i].getName().equals(getIdName(entity.getClass()))))
					setStatementProcess(entity, stmt, i, getMethodGetter(entityClass, i, fields).invoke(entity), operation, idAutoIncrement, fields);
			}			
			stmt.executeUpdate();
			connection.commit();
		}		
	}		

    private Method getMethodGetter(Class<?> entityClass, int i, Field[] fields) throws NoSuchMethodException {
        Field field = fields[i];
        String firstCharacterUppercase = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));        
        String[] token = field.getName().split(firstCharacterUppercase.toLowerCase(), LIMIT_TOKEN_SPLIT);	        
        String prefix = isTypeBooleanPrimitive(field.getType()) ? METHOD_GETTER_IS_PREFIX : METHOD_GETTER_PREFIX;
        return entityClass.getDeclaredMethod(prefix + firstCharacterUppercase + token[1]);
    }
    
    private boolean isTypeBooleanPrimitive(Class<?> type) {
    	return verifyTypeBooleanPrimitive(type);
    }
    
    private int computeIndexUpdate(int i, boolean idAutoIncrement) {
    	return i == 0 && !idAutoIncrement ? ++ i : ++ i - INDEX_DIFERENCE_UPDATE;
    } 
    
    private int computeIndexInsert(int i, boolean idAutoIncrement) {
    	return i != 0 && idAutoIncrement ? i : ++ i;
    } 

	private void setStatementProcess(T entity, PreparedStatement stmt, int i, Object value, String operation, boolean idAutoIncrement, Field[] fields) throws SQLException {
		
		if (operation.equals(UPDATE) 
				&& fields[i].getName().equals(getIdName(entity.getClass()))
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
	    
		setStatement(stmt, i, value, index, fields);
	}

	private void setStatement(PreparedStatement stmt, int i, Object value, int index, Field[] fields) throws SQLException {
		
		if (verifyTypeWrapperPrimitiveLong(i, fields)) {
			stmt.setLong(index, (Long) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveDouble(i, fields)) {
			stmt.setDouble(index, (Double) value);				
			return;
		}
		
		if (verifyTypeWrapperPrimitiveFloat(i, fields)) {
			stmt.setFloat(index, (Float) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveInteger(i, fields)) {
			stmt.setInt(index, (Integer) value);					
			return;
		}	
		
		if (verifyTypeWrapperPrimitiveCharacter(i, fields)) {
			stmt.setCharacterStream(index, (Reader) value);				
			return;
		}
		
		if (verifyTypeWrapperPrimitiveBoolean(i, fields)) {
			stmt.setBoolean(index, (Boolean) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveByte(i, fields)) {
			stmt.setByte(index, (Byte) value);			
		}

		if (verifyTypeWrapperPrimitiveShort(i, fields)) {
			stmt.setShort(index, (Short) value);			
		}		
		
		if (verifyTypeClassString(i, fields)) {
			stmt.setString(index, (String) value);					
			return;
		}
		
		if (verifyTypeClassBigDecimal(i, fields)) {			
			stmt.setBigDecimal(index, (BigDecimal) value);			
		}
		
		if (verifyTypeClassArray(i, fields)) {			
			stmt.setArray(index, (Array) value);			
		}
		
		if (verifyTypeClassBlob(i, fields)) {			
			stmt.setBlob(index, (Blob) value);			
		}
		
		if (verifyTypeClassDate(i, fields)) {			
			stmt.setDate(index, (Date) value);			
		}
	}

	private boolean verifyTypeWrapperPrimitiveLong(int i, Field[] fields) {
		return fields[i].getType().equals(Long.class) || fields[i].getType().equals(long.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveDouble(int i, Field[] fields) {
		return fields[i].getType().equals(Double.class) || fields[i].getType().equals(double.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveFloat(int i, Field[] fields) {
		return fields[i].getType().equals(Float.class) || fields[i].getType().equals(float.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveInteger(int i, Field[] fields) {
		return fields[i].getType().equals(Integer.class) || fields[i].getType().equals(int.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveCharacter(int i, Field[] fields) {
		return fields[i].getType().equals(Character.class) || fields[i].getType().equals(char.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveBoolean(int i, Field[] fields) {
		return fields[i].getType().equals(Boolean.class) || fields[i].getType().equals(boolean.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveByte(int i, Field[] fields) {
		return fields[i].getType().equals(Byte.class) || fields[i].getType().equals(byte.class);
	}
	
	private boolean verifyTypeWrapperPrimitiveShort(int i, Field[] fields) {
		return fields[i].getType().equals(Short.class) || fields[i].getType().equals(short.class);
	}	
	
	private boolean verifyTypeClassString(int i, Field[] fields) {
		return fields[i].getType().equals(String.class);
	}
	
	private boolean verifyTypeClassBigDecimal(int i, Field[] fields) {		
		return fields[i].getType().equals(BigDecimal.class);
	}
	
	private boolean verifyTypeClassArray(int i, Field[] fields) {		
		return fields[i].getType().equals(Array.class);
	}
	
	private boolean verifyTypeClassBlob(int i, Field[] fields) {		
		return fields[i].getType().equals(Blob.class);
	}
	
	private boolean verifyTypeClassDate(int i, Field[] fields) {		
		return fields[i].getType().equals(Date.class);
	}

	protected String mountQueryInsert(String table, Field[] fields, boolean idAutoIncrement) {
		
		fields = removeSerialVersionUIDAttribute(fields);
		fields = removeAttributeNotColumn(fields);		

		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();		

		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				if (idAutoIncrement && getFieldWithModifiedColumnName(fields[i]).equals(getIdName(entity)))
					continue;
				columnsName.append(getFieldWithModifiedColumnName(fields[i])).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(getFieldWithModifiedColumnName(fields[i]));
				values.append("?");
			}
		}
		
		StringBuilder sql = new StringBuilder(INSERT_INTO)		
			.append(table)
			.append(" (" + columnsName + ") ")
			.append("VALUES")
			.append(" (" + values + ") ");

		return sql.toString();
	}	
	
	private Field[] removeSerialVersionUIDAttribute(Field[] fields) {
		boolean flagExistAttributeSerialVersionUID = false;
		int fieldsLength = containsAttributeSerialVersionUID(fields)
				? fields.length - 1 : fields.length;		
		Field[] fieldsWithoutSerialVersionUIDAttribute = new Field[fieldsLength];
		for (int i = 0; i <= fields.length - 1; i++) {
			int indexFieldsWithoutSerialVersionUIDAttribute = 
					flagExistAttributeSerialVersionUID ? i - 1 : i;
			if (!fields[i].getName().equalsIgnoreCase(SERIAL_VERSION_UID)) {
				fieldsWithoutSerialVersionUIDAttribute[indexFieldsWithoutSerialVersionUIDAttribute] = fields[i];
			} else {
				flagExistAttributeSerialVersionUID = true;
			}
		}
		return fieldsWithoutSerialVersionUIDAttribute;
	}
	
	private Field[] removeAttributeNotColumn(Field[] fields) {
		boolean flagExistAttributeNotColumn = false;
		int indexSubtract = 0;
		int fieldsLength = containsNotColumnAnnotation(fields)
				? fields.length - amountNotColumnAnnotationExisting(fields) 
				: fields.length;	
		Field[] fieldsWithoutNotColumnAnnotation = new Field[fieldsLength];		
		for (int i = 0; i <= fields.length - 1; i++) {
			int indexFieldsWithoutNotColumnAnnotation = 
					flagExistAttributeNotColumn ? i - indexSubtract : i;					
			if (!containsNotColumnAnnotation(fields[i])) {
				fieldsWithoutNotColumnAnnotation[indexFieldsWithoutNotColumnAnnotation] = fields[i];				
			} else {
				flagExistAttributeNotColumn = true;
				indexSubtract ++;
			}		
		}		
		return fieldsWithoutNotColumnAnnotation;
	}
	
	private String getFieldWithModifiedColumnName(Field field) {		
		ColumnConfig annotAttribute = field.getDeclaredAnnotation(ColumnConfig.class);
		if (annotAttribute != null) {
			return annotAttribute.columnName();
		}
		return field.getName();
	}
	
	private boolean containsAttributeSerialVersionUID(Field[] fields) {
		for (int i = 0; i <= fields.length - 1; i++) {
			if (fields[i].getName().equalsIgnoreCase(SERIAL_VERSION_UID)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsNotColumnAnnotation(Field[] fields) {
		for (Field field : fields) {
			NotColumn annotAttribute = field.getDeclaredAnnotation(NotColumn.class);
			if (annotAttribute != null) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsNotColumnAnnotation(Field field) {		
		return field.getDeclaredAnnotation(NotColumn.class) != null; 
	}
	
	private int amountNotColumnAnnotationExisting(Field[] fields) {
		int amount = 0;
		for (Field field : fields) {
			NotColumn annotAttribute = field.getDeclaredAnnotation(NotColumn.class);
			if (annotAttribute != null) {
				amount ++;
			}
		}
		return amount;
	}		
	
	protected String mountQueryUpdate(String table, Field[] fields, Object idValue, String idName) {
		
		fields = removeSerialVersionUIDAttribute(fields);
		fields = removeAttributeNotColumn(fields);
		
		StringBuilder columnsName = new StringBuilder();		
		
		int qtdColumns = fields.length;

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {
				if (getFieldWithModifiedColumnName(fields[i]).equals(getIdName(entity)))
					continue;
				columnsName.append(getFieldWithModifiedColumnName(fields[i])).append(" = ?").append(", ");
			} else {
				columnsName.append(getFieldWithModifiedColumnName(fields[i])).append(" = ?");
			}
		}

		StringBuilder sql = new StringBuilder("UPDATE ")		
			.append(table)
			.append(" SET ")
			.append(columnsName.toString())
			.append(WHERE_CLAUSE + idName + " = '" + idValue + "'");		

		return sql.toString().replace("id = ?,", "");		
	}
	
	protected String mountQueryDelete(String table, Object id) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ")		
			.append(table)
			.append(WHERE_CLAUSE)
			.append(getIdName(entity))
			.append(" = ")
			.append("'" + id + "'");
		
		return sql.toString();
	}		
	
	protected String mountQueryGetAll(String table) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table);			
		
		return sql.toString();
	}
	
	protected String mountQuerySearchById(String table, Object idValue, String idName) {
		
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
	
	private boolean verifyTypeBooleanPrimitive(Object type) {
		return type.toString().equals(BOOLEAN);
	}

	private Object defineResultSetAttribute(ResultSet result, Field field) throws SQLException {

		Object type = field.getType().getName();

		if (verifyTypeWrapperPrimitiveLong(type)) {
			return result.getLong(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveDouble(type)) {
			return result.getDouble(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveFloat(type)) {
			return result.getFloat(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveInteger(type)) {
			return result.getInt(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveCharacter(type)) {
			return result.getCharacterStream(getFieldWithModifiedColumnName(field));
		}		

		if (verifyTypeWrapperPrimitiveBoolean(type)) {
			return result.getBoolean(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveByte(type)) {
			return result.getByte(getFieldWithModifiedColumnName(field));
		}

		if (verifyTypeWrapperPrimitiveShort(type)) {
			return result.getShort(getFieldWithModifiedColumnName(field));
		}		
		
		if (verifyTypeClassString(type)) {
			return result.getString(getFieldWithModifiedColumnName(field));
		}
		
		if (verifyTypeClassBigDecimal(type)) {
			return result.getBigDecimal(getFieldWithModifiedColumnName(field));
		}
		
		if (verifyTypeClassArray(type)) {
			return result.getArray(getFieldWithModifiedColumnName(field));
		}
		
		if (verifyTypeClassBlob(type)) {
			return result.getBlob(getFieldWithModifiedColumnName(field));
		}
		
		if (verifyTypeClassDate(type)) {
			return result.getDate(getFieldWithModifiedColumnName(field));
		}
		return new Object();
	}
	
	private boolean verifyTypeWrapperPrimitiveLong(Object type) {
		return type.equals(Long.class.getName()) || type.equals(long.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveDouble(Object type) {
		return type.equals(Double.class.getName()) || type.equals(double.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveFloat(Object type) {
		return type.equals(Float.class.getName()) || type.equals(float.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveInteger(Object type) {
		return type.equals(Integer.class.getName()) || type.equals(int.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveCharacter(Object type) {
		return type.equals(Character.class.getName()) || type.equals(char.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveBoolean(Object type) {
		return type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveByte(Object type) {
		return type.equals(Byte.class.getName()) || type.equals(byte.class.getName());
	}
	
	private boolean verifyTypeWrapperPrimitiveShort(Object type) {
		return type.equals(Short.class.getName()) || type.equals(short.class.getName());
	}	
	
	private boolean verifyTypeClassString(Object type) {
		return type.equals(String.class.getName());
	}
	
	private boolean verifyTypeClassBigDecimal(Object type) {
		return type.equals(BigDecimal.class.getName());
	}
	
	private boolean verifyTypeClassArray(Object type) {
		return type.equals(Array.class.getName());
	}
	
	private boolean verifyTypeClassBlob(Object type) {
		return type.equals(Blob.class.getName());
	}
	
	private boolean verifyTypeClassDate(Object type) {
		return type.equals(Date.class.getName());
	}

}
