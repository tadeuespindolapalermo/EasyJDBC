package com.github.tadeuespindolapalermo.easyjdbc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation must be used to annotate the <b>Getter method</b> of the 
 * attribute concerning the entity's unique identifier (id). General rules:
 * 
 * <ul><li>
 * If no Getter method is noted, the entity's unique identifier will not 
 * be recognized, which may cause an error at runtime according to some 
 * implementation scenarios.
 * </li><li>
 * If the entity's unique identifier is self-incrementing, the autoIncrement 
 * property must be declared with true value, because by default the value 
 * is implicitly declared as false.
 * </li></ul>
 * 
 * E-mail to report bugs and problems: <i>tadeupalermoti@gmail.com</i>
 * 
 * <p>This annotation is part of the library
 * <a href="https://github.com/tadeuespindolapalermo/EasyJDBC">
 * Easy JDBC Library</a>
 *
 * @author Tadeu Espíndola Palermo 
 * @since 1.0
 * 
 * @see NotColumn
 * @see PersistentClass
 * @see PersistentClassNamed   
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Identifier {	 
	
	boolean autoIncrement() default false;   

}
