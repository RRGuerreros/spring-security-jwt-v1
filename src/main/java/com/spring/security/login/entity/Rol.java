package com.spring.security.login.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_rol", uniqueConstraints = {
	@UniqueConstraint( columnNames = {
		"usuario_id","authority"
	})
})
public class Rol implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	private String authority;
	private String descripcion;

	public Rol() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Basic
	@Column( name = "authority", nullable = false, length = 50 )
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	@Basic
	@Column( name = "descripcion", length = 50)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
