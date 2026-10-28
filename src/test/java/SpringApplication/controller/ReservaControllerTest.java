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
	
	private Reserva reserva;

	@Test
	@DisplayName("Testar método de listar com sucesso (Status 200)")
	void teste1() throws Exception {
		//ARRANGE
		Reserva reserva1 = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reserva1.setId(1L);
		Reserva reserva2 = new Reserva(LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(5));
		reserva2.setId(2L);
		
		when(service.listar()).thenReturn(List.of(reserva1, reserva2));
		
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
	@DisplayName("Testar método de buscar por ID com sucesso (Status 200)")
	void teste3() throws Exception {
		//ARRANGE
		Long id = 1L;
		Reserva reserva1 = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reserva1.setId(id);
		
		when(service.buscarPorId(anyLong())).thenReturn(reserva1);
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas/{id}",id)
		.accept(org.springframework.http.MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar método de buscar por ID com erro (Status 404)")
	void teste4() throws Exception {
		//ARRANGE
		Long id = 99L;
		when(service.buscarPorId(id)).thenThrow(new IllegalArgumentException("Reserva não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/reservas/{id}",id)
		.accept(org.springframework.http	.MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de criar reserva com sucesso (Status 201)")
	void teste5() throws Exception {
		//ARRANGE
		Long id = 3L;
		Reserva reservaNova = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reservaNova.setId(id);
		Usuario usuarioNovo = new Usuario("Daniel","daniel12@gmail.com");
		usuarioNovo.setId(id);
		Sala salaNova = new Sala("Sala 2B",45,true);
		salaNova.setId(id);
		reservaNova.setSalaReservada(salaNova);
		reservaNova.setUsuarioAlugou(usuarioNovo);
		
		when(service.criarReserva(reservaNova, usuarioNovo, salaNova)).thenReturn(reservaNova);
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/reservas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaNova)))
		.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Testar o método de criar reserva com erro (Status 404)")
	void teste6() throws Exception {
		//ARRANGE
		Long id = 3L;
		Reserva reservaNova = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		reservaNova.setId(id);
		Usuario usuarioNovo = new Usuario("Daniel","daniel12@gmail.com");
		usuarioNovo.setId(id);
		Sala salaNova = new Sala("Sala 2B",45,true);
		salaNova.setId(id);
		reservaNova.setSalaReservada(salaNova);
		reservaNova.setUsuarioAlugou(usuarioNovo);
		
		when(service.criarReserva(any(Reserva.class), any(Usuario.class), any(Sala.class))).thenThrow(new IllegalArgumentException("Reserva já existente!"));
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/reservas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaNova)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva com sucesso (Status 200)")
	void teste7() throws JacksonException, Exception{
		//ARRANGE
		Long id = 3L;
		Reserva reservaAtualizada = new Reserva(LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(5));
		reservaAtualizada.setId(id);
		
		when(service.atualizarReserva(reservaAtualizada, id)).thenReturn(reservaAtualizada);
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/reservas/{id}",id)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaAtualizada)))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva com erro (Status 404)")
	void teste8() throws JacksonException, Exception {
		//ARRANGE
		Long id = 3L;
		Reserva reservaAtualizada = new Reserva(LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(5));
		reservaAtualizada.setId(id);
		
		when(service.atualizarReserva(any(Reserva.class), eq(id))).thenThrow(new IllegalArgumentException("Reserva não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/reservas/{id}",id)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(reservaAtualizada)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de deletar reserva com sucesso (Status 204)")
	void teste9() throws Exception {
		//ARRANGE
		Long id = 3L;
		
		doNothing().when(service).removerReserva(id);
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/reservas/{id}",id)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Testar o método de deletar reserva com erro (Status 404)")
	void teste10() throws Exception {
		//ARRANGE
		Long id = 3L;
		
		doThrow(new IllegalArgumentException("Reserva não encontrada!")).when(service).removerReserva(id);
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/reservas/{id}",id)
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
