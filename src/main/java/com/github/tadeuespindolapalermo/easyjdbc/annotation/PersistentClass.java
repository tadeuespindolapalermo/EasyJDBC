package com.github.tadeuespindolapalermo.easyjdbc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation must be used in persistent classes, that is, in classes 
 * that correspond to or refer to the tables in the database. 
 * Optionally, you can use the {@link PersistentClassNamed} annotation. General observations:
 * 
 * <ul><li>
 * When using this annotation, the exact name of the class will be taken 
 * into account as a reference to the name of the table in the database, 
 * that is, <b>the name of the entity class must have the same name as the table in the bank</b>.
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
 * @see PersistentClassNamed   
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface PersistentClass {
	
}