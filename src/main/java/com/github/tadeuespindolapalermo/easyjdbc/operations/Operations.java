package com.github.tadeuespindolapalermo.easyjdbc.operations;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.github.tadeuespindolapalermo.easyjdbc.connection.SingletonConnection;

public class Operations {	
	
	protected Connection connection;	
	
	protected static final String UPDATE = "update";
	
	protected static final String INSERT = "insert";
	
	protected static final int SINGLE_ELEMENT_COLLECTION = 0;	
	
	private static final String WHERE_CLAUSE = " WHERE ";	
	
	public Operations() {		
		connection = SingletonConnection.getConnection();
	}

	/**
	 * ATTENTION: this method must not close appeal!
	 * Do not use the close method or try-with-resources
	 */
	protected ResultSet processOperateWithResultSet(String query) throws SQLException  {		
	    PreparedStatement statement = connection.prepareStatement(query);
		ResultSet result = statement.executeQuery() ;
		if (result.next()) {
		    return result;
		}		
		return null;
	}
	
	protected boolean processDelete(String query) throws SQLException {
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			if(statement.executeUpdate() >= 1) {
				connection.commit();
				return Boolean.TRUE;						
			}
		}			
		return Boolean.FALSE;
	}	
	
	protected boolean processInsertUpdate(Map<String, ?> columnsAndValues, String query) throws SQLException {	
		
		boolean success;
		
		try (PreparedStatement stmt = connection.prepareStatement(query)) {			
			for (int i = 0; i <= columnsAndValues.values().size() - 1; i++) {
				setStatementProcess(stmt, i, columnsAndValues.values().toArray()[i], columnsAndValues);
			}			
			success = stmt.executeUpdate() > 0;
			connection.commit();
		}
		return success;
	}   
	
	private void setStatementProcess(PreparedStatement stmt, int i, Object value, Map<String, ?> columnsAndValues) throws SQLException {	    
		setStatement(stmt, i, value, ++ i, columnsAndValues);
	}

	private void setStatement(PreparedStatement stmt, int i, Object value, int index, Map<String, ?> columnsAndValues) throws SQLException {
		
		if (verifyTypeWrapperPrimitiveLong(i, columnsAndValues)) {
			stmt.setLong(index, (Long) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveDouble(i, columnsAndValues)) {
			stmt.setDouble(index, (Double) value);				
			return;
		}
		
		if (verifyTypeWrapperPrimitiveFloat(i, columnsAndValues)) {
			stmt.setFloat(index, (Float) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveInteger(i, columnsAndValues)) {
			stmt.setInt(index, (Integer) value);					
			return;
		}	
		
		if (verifyTypeWrapperPrimitiveCharacter(i, columnsAndValues)) {
			stmt.setCharacterStream(index, (Reader) value);				
			return;
		}
		
		if (verifyTypeWrapperPrimitiveBoolean(i, columnsAndValues)) {
			stmt.setBoolean(index, (Boolean) value);					
			return;
		}
		
		if (verifyTypeWrapperPrimitiveByte(i, columnsAndValues)) {
			stmt.setByte(index, (Byte) value);	
			return;
		}

		if (verifyTypeWrapperPrimitiveShort(i, columnsAndValues)) {
			stmt.setShort(index, (Short) value);
			return;
		}		
		
		if (verifyTypeClassString(i, columnsAndValues)) {
			stmt.setString(index, (String) value);					
			return;
		}
		
		if (verifyTypeClassBigDecimal(i, columnsAndValues)) {			
			stmt.setBigDecimal(index, (BigDecimal) value);		
			return;
		}
		
		if (verifyTypeClassArray(i, columnsAndValues)) {			
			stmt.setArray(index, (Array) value);
			return;
		}
		
		if (verifyTypeClassBlob(i, columnsAndValues)) {			
			stmt.setBlob(index, (Blob) value);
			return;
		}
		
		if (verifyTypeClassDate(i, columnsAndValues)) {			
			stmt.setDate(index, (Date) value);
		}
	}

	private boolean verifyTypeWrapperPrimitiveLong(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Long;
	}
	
	private boolean verifyTypeWrapperPrimitiveDouble(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Double;
	}
	
	private boolean verifyTypeWrapperPrimitiveFloat(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Float;
	}
	
	private boolean verifyTypeWrapperPrimitiveInteger(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Integer;
	}
	
	private boolean verifyTypeWrapperPrimitiveCharacter(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Character;
	}
	
	private boolean verifyTypeWrapperPrimitiveBoolean(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Boolean;
	}
	
	private boolean verifyTypeWrapperPrimitiveByte(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Byte;
	}
	
	private boolean verifyTypeWrapperPrimitiveShort(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof Short;
	}	
	
	private boolean verifyTypeClassString(int i, Map<String, ?> columnsAndValues) {
		return columnsAndValues.values().toArray()[i] instanceof String;
	}
	
	private boolean verifyTypeClassBigDecimal(int i, Map<String, ?> columnsAndValues) {		
		return columnsAndValues.values().toArray()[i] instanceof BigDecimal;
	}
	
	private boolean verifyTypeClassArray(int i, Map<String, ?> columnsAndValues) {		
		return columnsAndValues.values().toArray()[i] instanceof Array;
	}
	
	private boolean verifyTypeClassBlob(int i, Map<String, ?> columnsAndValues) {		
		return columnsAndValues.values().toArray()[i] instanceof Blob;
	}
	
	private boolean verifyTypeClassDate(int i, Map<String, ?> columnsAndValues) {		
		return columnsAndValues.values().toArray()[i] instanceof Date;
	}	
	
	protected String mountQueryInsert(Map<String, ?> columnsAndValues, String tableName) {
		
		StringBuilder columnsName = new StringBuilder();
		StringBuilder values = new StringBuilder();				
		
		int qtdColumns = columnsAndValues.values().size();

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {				
				columnsName.append(columnsAndValues.keySet().toArray()[i]).append(", ");
				values.append("?").append(", ");
			} else {
				columnsName.append(columnsAndValues.keySet().toArray()[i]);
				values.append("?");
			}
		}		
		
		StringBuilder sql = new StringBuilder("INSERT INTO ")		
				.append(tableName)
				.append(" (" + columnsName + ") ")
				.append("VALUES")
				.append(" (" + values + ") ");

		return sql.toString();		
	}	
	
	protected String mountQueryUpdate(Map<String, ?> columnsAndValues, Map<String, ?> clauseColumnAndValue, String tableName) {
		
		StringBuilder columnsName = new StringBuilder();
		
		int qtdColumns = columnsAndValues.values().size();

		for (int i = 0; qtdColumns - 1 >= i; i++) {
			if (i != qtdColumns - 1) {				
				columnsName.append(columnsAndValues.keySet().toArray()[i]).append(" = ?").append(", ");
			} else {
				columnsName.append(columnsAndValues.keySet().toArray()[i]).append(" = ?");
			}
		}					
		
		StringBuilder sql = new StringBuilder("UPDATE ")		
			.append(tableName)
			.append(" SET ")
			.append(columnsName)
			.append(WHERE_CLAUSE + 
				clauseColumnAndValue.keySet().toArray()[0].toString().replace("[", "").replace("]", "") + " = '" + 
				clauseColumnAndValue.values().toArray()[0].toString().replace("[", "").replace("]", "") + "'");		

		return sql.toString();
	}	
	
	protected String mountQueryDelete(String table, String column, Object value) {
		
		StringBuilder sql = new StringBuilder("DELETE FROM ")		
			.append(table)
			.append(WHERE_CLAUSE)
			.append(column)
			.append(" = ")
			.append("'" + value + "'");
		
		return sql.toString();
	}	
	
	protected String mountQueryGetAll(String table) {
		
		StringBuilder sql = new StringBuilder("SELECT * FROM ")		
			.append(table);			
		
		return sql.toString();
	}

}
