package com.github.tadeuespindolapalermo.easyjdbc.entity;

import java.io.Serializable;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.NotColumn;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClass;

@PersistentClass
public class Entity implements Serializable {
	
	private static final long serialVersionUID = -1157838021858283482L;	
	
	private Long id;	
	private String name;
	private String lastname;	
	private String cpf;
	private Double weight;
	private Boolean approved;
	private Integer age;
	
	@NotColumn
	private String nonPersistentAttribute;
	
	@NotColumn
	private String nonPersistentAttributeOther;	
	
	@NotColumn
	private String nonPersistentAttributeOtherMore;
	
	@NotColumn
	private String nonPersistentAttributeOtherMoreOne;	

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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getNonPersistentAttribute() {
		return nonPersistentAttribute;
	}
	
	public void setNonPersistentAttribute(String nonPersistentAttribute) {
		this.nonPersistentAttribute = nonPersistentAttribute;
	}
	
	public String getNonPersistentAttributeOther() {
		return nonPersistentAttributeOther;
	}
	
	public void setNonPersistentAttributeOther(String nonPersistentAttributeOther) {
		this.nonPersistentAttributeOther = nonPersistentAttributeOther;
	}
	
	public String getNonPersistentAttributeOtherMore() {
		return nonPersistentAttributeOtherMore;
	}
	
	public void setNonPersistentAttributeOtherMore(String nonPersistentAttributeOtherMore) {
		this.nonPersistentAttributeOtherMore = nonPersistentAttributeOtherMore;
	}
	
	public String getNonPersistentAttributeOtherMoreOne() {
		return nonPersistentAttributeOtherMoreOne;
	}
	
	public void setNonPersistentAttributeOtherMoreOne(String nonPersistentAttributeOtherMoreOne) {
		this.nonPersistentAttributeOtherMoreOne = nonPersistentAttributeOtherMoreOne;
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
		Entity other = (Entity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else {
			if (!id.equals(other.id))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Entity [id=" + id + ", name=" + name + ", lastname=" + lastname + ", cpf=" + cpf + ", weight=" + weight
				+ ", approved=" + approved + ", age=" + age + "]";
	}

}
