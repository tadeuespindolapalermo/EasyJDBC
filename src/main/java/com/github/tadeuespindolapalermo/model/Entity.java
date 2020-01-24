package com.github.tadeuespindolapalermo.model;

import com.github.tadeuespindolapalermo.annotation.Identifier;
import com.github.tadeuespindolapalermo.annotation.PersistentClass;

@PersistentClass
public class Entity {
	
	private Long id;	
	private String name;
	private String lastname;
	private String cpf;
	private Double weight;
	private Boolean approved;
	private Integer age;

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
