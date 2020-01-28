package com.github.tadeuespindolapalermo.easyjdbc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Identifier
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Identifier {	 
	
	boolean autoIncrement() default false;   

}
