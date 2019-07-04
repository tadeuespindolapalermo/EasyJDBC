package com.github.tadeuespindolapalermo.util;

public class ValidatorUtil {

	public static Boolean isNotNull(Object object) {
		return object != null;
	}

	public static Boolean isNotNull(Object... objects) {
		if (objects != null) {
			for (Object object : objects) {
				if (object == null) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

}
