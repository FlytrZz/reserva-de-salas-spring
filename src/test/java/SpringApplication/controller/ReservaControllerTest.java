package SpringApplication.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Usuario;
import SpringApplication.service.ReservaService;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private ReservaService service;
	
	private Reserva reservaPadrao;
	
	@BeforeEach
	void setUp() {
		Long id = 1L;
		reservaPadrao = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reservaPadrao.setId(1L);
	}

	@Test
	@DisplayName("Testar método de listar com sucesso (Status 200)")
	void teste1() throws Exception {
		//ARRANGE
		Reserva reserva2 = new Reserva(LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(5));
		reserva2.setId(2L);
		
		when(service.listar()).thenReturn(List.of(reservaPadrao, reserva2));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas")
		.accept(org.springframework.http.MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(2))
		.andExpect(jsonPath("$[0].id").value(1))
		.andExpect(jsonPath("$[1].id").value(2));
	}
	
	@Test
	@DisplayName("Testar método de listar com erro (Status 404)")
	void teste2() throws Exception {
		//ARRANGE
		when(service.listar()).thenThrow(new IllegalArgumentException("Nenhuma reserva encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas")
		.accept(org.springframework.http.MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar método de buscar reserva por ID com sucesso (Status 200)")
	void teste3() throws Exception {
		//ARRANGE
		when(service.buscarPorId(anyLong())).thenReturn(reservaPadrao);
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas/{id}",reservaPadrao.getId())
		.accept(org.springframework.http.MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar método de buscar reserva por ID com erro (Status 404)")
	void teste4() throws Exception {
		//ARRANGE
		when(service.buscarPorId(anyLong())).thenThrow(new IllegalArgumentException("Reserva não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas/{id}",reservaPadrao.getId())
		.accept(org.springframework.http	.MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de criar reserva com sucesso (Status 201)")
	void teste5() throws Exception {
		//ARRANGE
		Long id = 3L;
		Usuario usuarioNovo = new Usuario("Daniel","daniel12@gmail.com");
		usuarioNovo.setId(id);
		Sala salaNova = new Sala("Sala 2B",45,true);
		salaNova.setId(id);
		reservaPadrao.setSalaReservada(salaNova);
		reservaPadrao.setUsuarioAlugou(usuarioNovo);
		
		when(service.criarReserva(reservaPadrao, usuarioNovo, salaNova)).thenReturn(reservaPadrao);
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/reservas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaPadrao)))
		.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Testar o método de criar reserva com erro (Status 404)")
	void teste6() throws Exception {
		//ARRANGE
		Long id = 3L;
		Usuario usuarioNovo = new Usuario("Daniel","daniel12@gmail.com");
		usuarioNovo.setId(id);
		Sala salaNova = new Sala("Sala 2B",45,true);
		salaNova.setId(id);
		reservaPadrao.setSalaReservada(salaNova);
		reservaPadrao.setUsuarioAlugou(usuarioNovo);
		
		when(service.criarReserva(any(Reserva.class), any(Usuario.class), any(Sala.class))).thenThrow(new IllegalArgumentException("Reserva já existente!"));
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/reservas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva com sucesso (Status 200)")
	void teste7() throws JacksonException, Exception{
		//ARRANGE
		when(service.atualizarReserva(reservaPadrao, reservaPadrao.getId())).thenReturn(reservaPadrao);
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/reservas/{id}",reservaPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaPadrao)))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva com erro (Status 404)")
	void teste8() throws JacksonException, Exception {
		//ARRANGE
		when(service.atualizarReserva(any(Reserva.class), eq(reservaPadrao.getId()))).thenThrow(new IllegalArgumentException("Reserva não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/reservas/{id}",reservaPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de deletar reserva com sucesso (Status 204)")
	void teste9() throws Exception {
		//ARRANGE
		doNothing().when(service).removerReserva(reservaPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/reservas/{id}",reservaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Testar o método de deletar reserva com erro (Status 404)")
	void teste10() throws Exception {
		//ARRANGE
		doThrow(new IllegalArgumentException("Reserva não encontrada!")).when(service).removerReserva(reservaPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/reservas/{id}",reservaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
