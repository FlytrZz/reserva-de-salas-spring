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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import SpringApplication.model.Sala;
import SpringApplication.service.SalaService;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(SalaController.class)
class SalaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private SalaService service;
	
	private Sala salaPadrao;
	
	@BeforeEach
	void setUp() {
		Long id = 2L;
		salaPadrao = new Sala("Sala 2B",45,true);
		salaPadrao.setId(id);
	}

	@Test
	@DisplayName("Testar método de listar com sucesso (Status 200)")
	void teste1() throws Exception {
		//ARRANGE
		Sala sala2 = new Sala("Sala 1B",30,true);
		sala2.setId(3L);
		
		when(service.listar()).thenReturn(List.of(salaPadrao, sala2));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/salas")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(2));
	}
	
	@Test
	@DisplayName("Testar método de listar com erro (Status 404)")
	void teste2() throws Exception {
		//ARRANGE
		when(service.listar()).thenThrow(new IllegalArgumentException("Nenhuma sala encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/salas")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de buscar sala por ID com sucesso (Status 200)")
	void teste3() throws Exception {
		//ARRANGE
		when(service.buscarPorId(anyLong())).thenReturn(salaPadrao);
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/salas/{id}",salaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de buscar sala por ID com erro (Status 404)")
	void teste4() throws Exception {
		//ARRANGE
		when(service.buscarPorId(anyLong())).thenThrow(new IllegalArgumentException("Sala não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/salas/{id}",salaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de criar sala com sucesso (Status 201)")
	void teste5() throws Exception {
		//ARRANGE
		when(service.criarSala(salaPadrao)).thenReturn(salaPadrao);
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/salas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(salaPadrao)))
		.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Testar o método de criar sala com erro (Status 404)")
	void teste6() throws Exception {
		//ARRANGE
		when(service.criarSala(any(Sala.class))).thenThrow(new IllegalArgumentException("Sala já existente!"));
				
		//ACT + ASSERT
		mvc.perform(post("/api/v1/salas")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(salaPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar sala com sucesso (Status 200)")
	void teste7() throws JacksonException, Exception {
		//ARRANGE
		when(service.atualizarSala(salaPadrao, salaPadrao.getId())).thenReturn(salaPadrao);
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/salas/{id}",salaPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(salaPadrao)))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar sala com erro (Status 404)")
	void teste8() throws JacksonException, Exception {
		//ARRANGE
		when(service.atualizarSala(any(Sala.class), eq(salaPadrao.getId()))).thenThrow(new IllegalArgumentException("Sala não encontrada!"));
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/salas/{id}",salaPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(salaPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de remover sala com sucesso (Status 204)")
	void teste9() throws Exception {
		//ARRANGE
		doNothing().when(service).apagarSala(salaPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/salas/{id}",salaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Testar o método de remover sala com erro (Status 404)")
	void teste10() throws Exception {
		//ARRANGE
		doThrow(new IllegalArgumentException("Sala não encontrada!")).when(service).apagarSala(salaPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/salas/{id}",salaPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
