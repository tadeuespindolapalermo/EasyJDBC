package com.github.tadeuespindolapalermo.model;

import com.github.tadeuespindolapalermo.annotation.Identifier;
import com.github.tadeuespindolapalermo.annotation.PersistentClassNamed;

@PersistentClassNamed("entitynamed")
public class EntityNamed {

	private String name;
	private String lastname;
	private String cpf;
	private Double weight;
	private Boolean approved;
	private Integer age;

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

	@Identifier
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
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
		EntityNamed other = (EntityNamed) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntityNamed [name=" + name + ", lastname=" + lastname + ", cpf=" + cpf + ", weight=" + weight
				+ ", approved=" + approved + ", age=" + age + "]";
	}

}
