package com.example.crud.service.persona.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personas")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false)
	private Integer edad;

	protected Persona() {
	}

	public Persona(String nombre, Integer edad) {
		this.nombre = nombre;
		this.edad = edad;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getEdad() {
		return edad;
	}

	public void actualizar(String nombre, Integer edad) {
		this.nombre = nombre;
		this.edad = edad;
	}
}
