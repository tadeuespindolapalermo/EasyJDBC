package com.github.tadeuespindolapalermo.util;

import java.util.Collection;

public class Utils {
	
	public static void printOut(Collection<?> objects) {
		for (Object object : objects) {
			System.out.println(object);
		}
	}
	
	public static void printOut(Object object) {
		System.out.println(object);
	}

}
