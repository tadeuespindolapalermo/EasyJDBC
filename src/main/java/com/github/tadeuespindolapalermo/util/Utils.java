package com.github.tadeuespindolapalermo.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class Utils {
	
	private static final String BOOLEAN = "boolean";

	private Utils() { }

	public static void printLogError(Collection<?> objects) {
		for (Object object : objects) {
			LogUtil.getLogger(Utils.class).error(object);
		}
	}

	public static void printLogError(Object object) {
		LogUtil.getLogger(Utils.class).error(object);
	}

	public static void print(Collection<?> objects) {
		for (Object object : objects) {
			LogUtil.getLogger(Utils.class).info(object);
		}
	}

	public static void print(Object object) {
		LogUtil.getLogger(Utils.class).info(object);
	}

	public static Class<?> verifyType(Object type) {

		if (type instanceof Long) {
			return Long.class;
		}

		if (type instanceof Double) {
			return Double.class;
		}

		if (type instanceof Float) {
			return Float.class;
		}

		if (type instanceof Integer) {
			return Integer.class;
		}

		if (type instanceof Character) {
			return Character.class;
		}

		if (type instanceof String) {
			return String.class;
		}

		if (type instanceof Boolean) {
			return Boolean.class;
		}

		if (type instanceof Byte) {
			return Byte.class;
		}

		if (type instanceof Short) {
			return Short.class;
		}

		if (type instanceof Number) {
			return Number.class;
		}
		return Object.class;
	}
	
	public static boolean verifyTypeBooleanPrimitive(Object type) {
		return type.toString().equals(BOOLEAN);
	}

	public static Object defineResultSetAttribute(ResultSet result, Field field) throws SQLException {

		Object type = field.getType().getName();

		if (type.equals(Long.class.getName())) {
			return result.getLong(field.getName());
		}

		if (type.equals(Double.class.getName())) {
			return result.getDouble(field.getName());
		}

		if (type.equals(Float.class.getName())) {
			return result.getFloat(field.getName());
		}

		if (type.equals(Integer.class.getName())) {
			return result.getInt(field.getName());
		}

		if (type.equals(Character.class.getName())) {
			return result.getCharacterStream(field.getName());
		}

		if (type.equals(String.class.getName())) {
			return result.getString(field.getName());
		}

		if (type.equals(Boolean.class.getName())) {
			return result.getBoolean(field.getName());
		}

		if (type.equals(Byte.class.getName())) {
			return result.getByte(field.getName());
		}

		if (type.equals(Short.class.getName())) {
			return result.getShort(field.getName());
		}
		return new Object();
	}

}
