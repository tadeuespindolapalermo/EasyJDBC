# Easy JDBC Library

Easy JDBC is a library built to facilitate the use of JDBC for data persistence in Java projects.

Latest version of the library: <b><a href="https://mvnrepository.com/artifact/com.github.tadeuespindolapalermo.easyjdbc/easy-jdbc/1.8-beta" >1.8-beta</a></b>

<hr>

How to incorporate into your project?

<b>Incorporate using Maven:</b><br>
<pre>
&lt;dependency&gt;
&#32;&#32;&#32;&#32;&lt;groupId&gt;com.github.tadeuespindolapalermo.easyjdbc&lt;/groupId&gt;
&#32;&#32;&#32;&#32;&lt;artifactId&gt;easy-jdbc&lt;/artifactId&gt;
&#32;&#32;&#32;&#32;&lt;version&gt;1.8-beta&lt;/version&gt;
&lt;/dependency&gt;
</pre>

<b>Incorporate using Gradle:</b><br>
<pre>implementation 'com.github.tadeuespindolapalermo.easyjdbc:easy-jdbc:1.8-beta'</pre>

Main links:<br>

MVN Repository: <br>
<b> https://mvnrepository.com/artifact/com.github.tadeuespindolapalermo.easyjdbc/easy-jdbc </b><br>

Sonatype: <br>
<b> https://search.maven.org/artifact/com.github.tadeuespindolapalermo.easyjdbc/easy-jdbc </b><br>

Video presentation of the library at the event FLISOL DF 2020 (02/05/2020): <br>
<b> https://www.youtube.com/watch?v=Vg-VT86z65Q </b><br>

Source code used to present the library at the FLISOL event: <br>
<b> https://github.com/tadeuespindolapalermo/flisol-easyjdbc </b><br>

<hr>

Be part of this project reporting bug's or becoming a committer! <br>
Support this project and be part of the Easy JDBC community!

Send e-mail to <b>tadeupalermoti@gmail.com</b> and make your request! <br>

<hr>

<h2>SIMPLE USAGE EXAMPLES</h2>
<h2>EXAMPLE 1</h2>

<b>PRODUCT SEQUENCER - PostgreSQL</b>

<pre>CREATE SEQUENCE public.product_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;</pre>

<b>PRODUCT TABLE - PostgreSQL</b>

<pre>CREATE TABLE public.tb_product (
    id bigint NOT NULL DEFAULT nextval('product_sequence'::regclass),
    desc_product character varying COLLATE pg_catalog."default" NOT NULL,
    amt bigint NOT NULL,
    value numeric NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id)
)</pre>

<hr>

<b>PRODUCT ENTITY - PERSISTENT CLASS</b>

<pre>
import com.github.tadeuespindolapalermo.easyjdbc.annotation.ColumnConfig;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.NotColumn;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;

@PersistentClassNamed("tb_product")
public class Product {

    private Long id;

    @ColumnConfig(columnName = "desc_product")
    private String name;

    private double value;

    @ColumnConfig(columnName = "amt")
    private int amount;

    @NotColumn
    private boolean bestSeller;

    public Product() { }	

    public Product(String name, double value, int amount) {		
        this.name = name;
	this.value = value;
	this.amount = amount;
    }
	
    @Identifier(autoIncrement = true)
    public Long getId() {
        return id;
    }
	
    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public double getValue() {
	return value;
    }

    public void setValue(double value) {
	this.value = value;
    }

    public int getAmount() {
	return amount;
    }

    public void setAmount(int amount) {
	this.amount = amount;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
   	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Product other = (Product) obj;
	if (id == null) {
	    if (other.id != null)
	        return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Product - id: " + id + " | name: " + name + " | value: " + value + " | amount: " + amount;
    }
}
</pre>

<b>OPERATIONS WITH THE PRODUCT ENTITY</b>

<pre>  
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.crud.CrudEntity;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

import br.com.productregistration.model.Product;

public class Main {
        
    public static void main(String[] args) throws Exception {
                
        toConnect();

	CrudEntity&lt;Product&gt; crudEntity = new CrudEntity<>(Product.class);
		
	crudEntity.save(createProduct("Product A", 400.0, 30));
	crudEntity.save(createProduct("Product B", 800.5, 45));
	crudEntity.save(createProduct("Product C", 1900.3, 02));
	crudEntity.save(createProduct("Product D", 50.1, 15));

	Product product = crudEntity.searchById(1L);
		
	product.setName("Product A Update");
	crudEntity.update(product);

	List&lt;Product&gt; products = crudEntity.getAll();
	products.forEach(System.out::println);

	crudEntity.deleteById(2L);

	crudEntity.search("select * from tb_product").forEach(System.out::println);		
    }

    private static void toConnect() {
	InfoConnection.setDatabase(EnumDatabase.POSTGRE);
	InfoConnection.setNameDatabase("product-registration");
	InfoConnection.setUser("your-user-db");
	InfoConnection.setPassword("your-password-db");
    }

    private static Product createProduct(String name, double value, int amount) {
	return new Product(name, value, amount);
    }
}</pre>

<b>Output result</b>:

22:44:02,729  INFO -> Connection successful!<br>
Bank: POSTGRE<br>
Database: product-registration<br>
Product - id: 2 | name: Product B | value: 800.5 | amount: 45<br>
Product - id: 3 | name: Product C | value: 1900.3 | amount: 2<br>
Product - id: 4 | name: Product D | value: 50.1 | amount: 15<br>
Product - id: 1 | name: Product A Update | value: 400.0 | amount: 30<br>
Product - id: 3 | name: Product C | value: 1900.3 | amount: 2<br>
Product - id: 4 | name: Product D | value: 50.1 | amount: 15<br>
Product - id: 1 | name: Product A Update | value: 400.0 | amount: 30

<hr>

<h2>EXAMPLE 2</h2>

<b>PEOPLE SEQUENCER - PostgreSQL</b>

<pre>CREATE SEQUENCE public.people_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;</pre>

<b>PEOPLE TABLE - PostgreSQL</b>

<pre>CREATE TABLE public.people (
    id bigint NOT NULL DEFAULT nextval('people_sequence'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    age bigint NOT NULL,    
    CONSTRAINT people_pkey PRIMARY KEY (id)
)</pre>

<hr>

<b>PEOPLE ENTITY - PERSISTENT CLASS</b>

<pre>
import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClass;

@PersistentClass
public class People {

    private Long id;

    private String name;

    private int age;

    public People() { }

    public People(String name, int age) {
	this.name = name;
	this.age = age;
    }

    @Identifier(autoIncrement = true)
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getAge() {
	return age;
    }

    public void setAge(int age) {
	this.age = age;
    }
}
</pre>

<b>OPERATIONS WITH THE PEOPLE ENTITY</b>

<pre>  
import java.util.List;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.crud.CrudEntity;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

import br.com.peopleregistration.model.People;

public class Main {

    public static void main(String[] args) throws Exception {

	toConnect();

	CrudEntity&lt;People&gt; crudEntity = new CrudEntity<>(People.class);

	crudEntity.save(createPeople("Tadeu", 35));
	crudEntity.save(createPeople("Joseph", 95));

	printPeople(crudEntity.getAll());
    }

    private static void toConnect() {
	InfoConnection.setDatabase(EnumDatabase.POSTGRE);
	InfoConnection.setNameDatabase("people-registration");
	InfoConnection.setUser("your-user-db");
	InfoConnection.setPassword("your-password-db");
    }

    private static People createPeople(String name, int age) {
	return new People(name, age);
    }
	
    private static void printPeople(List&lt;People&gt; peoples) {
	peoples.forEach(p -> System.out.println("Name: " + p.getName() + " - Age: " + p.getAge()));
    }
}</pre>

<b>Output result</b>:

14:38:31,597  INFO -> Connection successful!<br>
Bank: POSTGRE<br>
Database: people-registration<br>
Name: Tadeu - Age: 35<br>
Name: Joseph - Age: 95<br>

<hr>

<h2>EXAMPLE 3</h2>

<b>PEOPLE SEQUENCER - PostgreSQL</b>

<pre>CREATE SEQUENCE public.user_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;</pre>

<b>USER TABLE - PostgreSQL</b>

<pre>CREATE TABLE public.tb_user (
    id bigint NOT NULL DEFAULT nextval('user_sequence'::regclass),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    phone character varying COLLATE pg_catalog."default" NOT NULL,
    children bigint NOT NULL,    
    CONSTRAINT user_pkey PRIMARY KEY (id)
)</pre>

<hr>

<b>OPERATIONS WITH THE USER TABLE</b>

<pre>  
import java.util.HashMap;
import java.util.Map;

import com.github.tadeuespindolapalermo.easyjdbc.connection.InfoConnection;
import com.github.tadeuespindolapalermo.easyjdbc.crud.Crud;
import com.github.tadeuespindolapalermo.easyjdbc.enumeration.EnumDatabase;

public class Main {

    public static void main(String[] args) throws Exception {

	toConnect();

	Crud crud = new Crud();		
		
	crud.save(createColumnsAndValues(), "tb_user");	
	crud.save(createColumnsAndValues(), "tb_user");	
	crud.save(createColumnsAndValues(), "tb_user");	
	crud.update(updateColumnsAndValues(), clauseColumnAndValue(), "tb_user");
	crud.delete("tb_user", "id", 3);
    }

    private static void toConnect() {
	InfoConnection.setDatabase(EnumDatabase.POSTGRE);
	InfoConnection.setNameDatabase("user-registration");
	InfoConnection.setUser("your-user-db");
	InfoConnection.setPassword("your-password-db");
    }

    private static Map&lt;String, Object&gt; createColumnsAndValues() {
	Map&lt;String, Object&gt; columnsAndValues = new HashMap<>();		
	columnsAndValues.put("name", "User Pool");
	columnsAndValues.put("email", "user@user.com");
	columnsAndValues.put("password", "123");
	columnsAndValues.put("phone", "96521456");
	columnsAndValues.put("children", 3);				
	return columnsAndValues;
    }
	
    private static Map&lt;String, Object&gt; clauseColumnAndValue() {
        Map&lt;String, Object&gt; columnsAndValues = new HashMap<>();		
	columnsAndValues.put("id", 2);					
	return columnsAndValues;
    }
	
    private static Map&lt;String, Object&gt; updateColumnsAndValues() {
	Map&lt;String, Object&gt; columnsAndValues = new HashMap<>();		
	columnsAndValues.put("name", "User Pool Update");
	columnsAndValues.put("email", "userupdate@userupdate.com");
	columnsAndValues.put("password", "123update");
	columnsAndValues.put("phone", "96521456000");
	columnsAndValues.put("children", 6);				
	return columnsAndValues;
    }   
}</pre>

<b>Output result</b>:

20:00:35,570  INFO -> Connection successful!<br>
Bank: POSTGRE<br>
Database: user-registration<br>

<hr>
