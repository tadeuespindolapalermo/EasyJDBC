package com.github.tadeuespindolapalermo.util;

import org.apache.log4j.Logger;

public class LogUtil {
	
	private static Logger logger;
	
	public static Logger getLogger(Object object) {
		logger = Logger.getLogger(object.getClass());
		return logger;
	}

}
