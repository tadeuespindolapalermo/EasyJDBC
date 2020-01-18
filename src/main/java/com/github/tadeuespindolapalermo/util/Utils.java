package com.github.tadeuespindolapalermo.util;

import java.util.Collection;

public class Utils {

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
		LogUtil.getLogger(Utils.class).error(object);
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

}
