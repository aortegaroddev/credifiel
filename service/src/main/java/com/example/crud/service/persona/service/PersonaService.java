package com.example.crud.service.persona.service;

import com.example.crud.service.persona.domain.Persona;
import com.example.crud.service.persona.dto.PersonaRequest;
import com.example.crud.service.persona.dto.PersonaResponse;
import com.example.crud.service.persona.exception.PersonaNotFoundException;
import com.example.crud.service.persona.repository.PersonaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonaService {

	private final PersonaRepository personaRepository;

	public PersonaService(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	public List<PersonaResponse> listar() {
		return personaRepository.findAll().stream()
				.map(this::toResponse)
				.toList();
	}

	public PersonaResponse obtenerPorId(Long id) {
		return toResponse(buscarPorId(id));
	}

	@Transactional
	public PersonaResponse crear(PersonaRequest request) {
		Persona persona = personaRepository.save(new Persona(request.nombre().trim(), request.edad()));
		return toResponse(persona);
	}

	@Transactional
	public PersonaResponse actualizar(Long id, PersonaRequest request) {
		Persona persona = buscarPorId(id);
		persona.actualizar(request.nombre().trim(), request.edad());
		return toResponse(persona);
	}

	@Transactional
	public void eliminar(Long id) {
		personaRepository.delete(buscarPorId(id));
	}

	private Persona buscarPorId(Long id) {
		return personaRepository.findById(id)
				.orElseThrow(() -> new PersonaNotFoundException(id));
	}

	private PersonaResponse toResponse(Persona persona) {
		return new PersonaResponse(persona.getId(), persona.getNombre(), persona.getEdad());
	}
}
