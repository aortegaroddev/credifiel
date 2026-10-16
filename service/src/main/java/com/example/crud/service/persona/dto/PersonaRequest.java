package com.example.crud.service.persona.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PersonaRequest(
		@NotBlank(message = "El nombre es obligatorio")
		@Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
		String nombre,
		@NotNull(message = "La edad es obligatoria")
		@Min(value = 0, message = "La edad debe ser mayor o igual a 0")
		@Max(value = 150, message = "La edad debe ser menor o igual a 150")
		Integer edad) {
}
