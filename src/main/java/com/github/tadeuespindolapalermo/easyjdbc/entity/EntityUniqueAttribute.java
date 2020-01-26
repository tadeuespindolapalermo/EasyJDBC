package com.github.tadeuespindolapalermo.easyjdbc.entity;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;

@PersistentClassNamed("entityuniqueattribute")
public class EntityUniqueAttribute {

	private Long id;
	private boolean canceled;

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Identifier(autoIncrement = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		EntityUniqueAttribute other = (EntityUniqueAttribute) obj;
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
		return "EntityUniqueAttribute [id=" + id + ", canceled=" + canceled + "]";
	}

}
