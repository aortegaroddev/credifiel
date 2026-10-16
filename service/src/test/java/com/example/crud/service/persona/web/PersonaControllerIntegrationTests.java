package com.example.crud.service.persona.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PersonaControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void debeCrearConsultarActualizarYEliminarUnaPersona() throws Exception {
		String response = mockMvc.perform(post("/api/personas")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Ana López","edad":30}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.nombre").value("Ana López"))
				.andReturn()
				.getResponse()
				.getContentAsString();
		JsonNode persona = objectMapper.readTree(response);
		long id = persona.required("id").asLong();

		mockMvc.perform(get("/api/personas/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.edad").value(30));

		mockMvc.perform(put("/api/personas/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Ana María López","edad":31}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Ana María López"))
				.andExpect(jsonPath("$.edad").value(31));

		mockMvc.perform(delete("/api/personas/{id}", id))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/personas/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.title").value("Recurso no encontrado"));
	}
}
