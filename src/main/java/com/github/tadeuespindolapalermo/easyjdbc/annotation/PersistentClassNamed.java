package com.github.tadeuespindolapalermo.easyjdbc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation must be used in persistent classes, that is, in classes 
 * that correspond to or refer to the tables in the database. 
 * Optionally, you can use the {@link PersistentClass} annotation. 
 * General observations:
 * 
 * <ul><li>
 * When using this annotation, the <b>value informed by parameter in the annotation</b> 
 * will be taken into account as a reference to the name of the table in the database, 
 * that is, the name informed by parameter in the annotation must have the same name 
 * of the table in the bank.
 * </li><li>
 * Only persistent classes should be noted.
 * </li><li>
 * If a persistent class is not annotated with <code>PersistentClass</code> or 
 * <code>PersistentClassNamed</code>, an exception of type 
 * <code>NotPersistentClassException</code> will be thrown.
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
 * @see Identifier
 * @see NotColumn
 * @see PersistentClass
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PersistentClassNamed {		
	
	String value();

}
