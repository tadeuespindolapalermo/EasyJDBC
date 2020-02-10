package com.github.tadeuespindolapalermo.easyjdbc.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is useful for specifying that certain attributes of the 
 * entity do not refer to any column in the database table. General observations:
 * 
 * <ul><li>
 * If this is the scenario, the attribute must be annotated to avoid a 
 * runtime error, otherwise the attribute will be seen as a column in the 
 * table concerning the referring entity.
 * </li><li>
 * Some entity attributes can only function as an aid or support for 
 * certain purposes. In such cases, it is essential that they be noted!
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
 * @see PersistentClass
 * @see PersistentClassNamed   
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface NotColumn {
	
}