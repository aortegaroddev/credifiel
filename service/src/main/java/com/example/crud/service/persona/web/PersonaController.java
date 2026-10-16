package com.example.crud.service.persona.web;

import com.example.crud.service.persona.dto.PersonaRequest;
import com.example.crud.service.persona.dto.PersonaResponse;
import com.example.crud.service.persona.service.PersonaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

	private final PersonaService personaService;

	public PersonaController(PersonaService personaService) {
		this.personaService = personaService;
	}

	@GetMapping
	public List<PersonaResponse> listar() {
		return personaService.listar();
	}

	@GetMapping("/{id}")
	public PersonaResponse obtenerPorId(@PathVariable Long id) {
		return personaService.obtenerPorId(id);
	}

	@PostMapping
	public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody PersonaRequest request) {
		PersonaResponse persona = personaService.crear(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(persona.id())
				.toUri();
		return ResponseEntity.created(location).body(persona);
	}

	@PutMapping("/{id}")
	public PersonaResponse actualizar(@PathVariable Long id, @Valid @RequestBody PersonaRequest request) {
		return personaService.actualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		personaService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
