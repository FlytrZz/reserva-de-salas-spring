package SpringApplication.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import SpringApplication.model.Reserva;
import SpringApplication.service.ReservaService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private ReservaService service;

	@Test
	@DisplayName("Testar o que o método de listar com sucesso (Status 200)")
	void teste1() throws Exception {
		//ARRANGE
		Reserva reserva1 = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reserva1.setId(1L);
		Reserva reserva2 = new Reserva(LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(5));
		reserva2.setId(2L);
		
		when(service.listar()).thenReturn(List.of(reserva1, reserva2));
		
		//ACT + ASSERT
		mvc.perform(get("/api/reservas")
		.accept(org.springframework.http.MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(2))
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[1].id").value(2));
	}

}
