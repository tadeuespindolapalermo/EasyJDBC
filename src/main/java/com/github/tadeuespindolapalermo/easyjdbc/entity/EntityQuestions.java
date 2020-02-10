package com.github.tadeuespindolapalermo.easyjdbc.entity;

import java.io.Serializable;

import com.github.tadeuespindolapalermo.easyjdbc.annotation.Identifier;
import com.github.tadeuespindolapalermo.easyjdbc.annotation.PersistentClassNamed;

@PersistentClassNamed("perguntas")
public class EntityQuestions implements Serializable {

	private static final long serialVersionUID = 4496983344901641407L;

	private Long id;
	private String nivel;
	private String enunciado;
	private String alternativa1;
	private String alternativa2;
	private String alternativa3;
	private String resposta;

	@Identifier(autoIncrement = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getAlternativa1() {
		return alternativa1;
	}

	public void setAlternativa1(String alternativa1) {
		this.alternativa1 = alternativa1;
	}

	public String getAlternativa2() {
		return alternativa2;
	}

	public void setAlternativa2(String alternativa2) {
		this.alternativa2 = alternativa2;
	}

	public String getAlternativa3() {
		return alternativa3;
	}

	public void setAlternativa3(String alternativa3) {
		this.alternativa3 = alternativa3;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
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
		EntityQuestions other = (EntityQuestions) obj;
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
		return "EntityQuestions [id=" + id + ", nivel=" + nivel + ", enunciado=" + enunciado + ", alternativa1="
				+ alternativa1 + ", alternativa2=" + alternativa2 + ", alternativa3=" + alternativa3 + ", resposta="
				+ resposta + "]";
	}

}
