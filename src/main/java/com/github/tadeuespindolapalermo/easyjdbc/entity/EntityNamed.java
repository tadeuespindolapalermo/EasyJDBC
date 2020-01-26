package com.github.tadeuespindolapalermo.easyjdbc.entity;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;

@PersistentClassNamed("entitynamed")
public class EntityNamed {

	private String number;
	private String description;	

	@Identifier
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		if (number == null) {
			if (other.number != null)
				return false;
		} else {
			if (!number.equals(other.number))
				return false;
		}			
		return true;
	}

	@Override
	public String toString() {
		return "EntityNamed [number=" + number + ", description=" + description + "]";
	}	

}
