package com.github.tadeuespindolapalermo.util;

import java.util.Collection;

public class Utils {

	private Utils() {
	}

	public static void printOut(Collection<?> objects) {
		for (Object object : objects) {
			LogUtil.getLogger(Utils.class).error(object);
		}
	}

	public static void printOut(Object object) {
		LogUtil.getLogger(Utils.class).error(object);
	}

}
