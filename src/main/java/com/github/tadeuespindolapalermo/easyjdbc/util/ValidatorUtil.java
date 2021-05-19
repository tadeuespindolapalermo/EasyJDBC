package com.github.tadeuespindolapalermo.easyjdbc.util;

public class ValidatorUtil {
	
	private ValidatorUtil() {}

	public static boolean isNotNull(Object object) {
		return object != null;
	}

	public static boolean isNotNull(Object... objects) {
		if (objects != null) {
			for (Object object : objects) {
				if (object == null) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public static boolean isNull(Object object) {
		return object == null;
	}	
	
	public static boolean isArrayValid(Object[] array) {
    	return array != null && array.length > 0;
    }

	public static boolean isArrayNotValid(Object[] array) {
    	return array == null || array.length <= 0;
    }

}
