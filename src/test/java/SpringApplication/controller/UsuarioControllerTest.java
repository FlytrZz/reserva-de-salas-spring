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
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import SpringApplication.model.Usuario;
import SpringApplication.service.UsuarioService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private UsuarioService service;
	
	private Usuario usuarioPadrao;
	
	@BeforeEach
	void setUp() {
		Long id = 5L;
		usuarioPadrao = new Usuario("Gabriel","gabriel@gmail.com");
		usuarioPadrao.setId(id);
	}

	@Test
	@DisplayName("Testar o método de listar com sucesso (Status 200)")
	void teste1() throws Exception{
		//ARRANGE
		Usuario usuario2 = new Usuario("Teste","teste@email.com");
		
		when(service.listar()).thenReturn(List.of(usuarioPadrao, usuario2));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/usuarios")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.size()").value(2));
	}
	
	@Test
	@DisplayName("Testar o método de listar com erro (Status 404)")
	void teste2() throws Exception {
		//ARRANGE
		when(service.listar()).thenThrow(new IllegalArgumentException("Nenhum usuário encontrado!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/usuarios")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de buscar por id com sucesso (Status 200)")
	void teste3() throws Exception {
		//ARRANGE
		when(service.buscarPorId(usuarioPadrao.getId())).thenReturn(usuarioPadrao);
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/usuarios/{id}",usuarioPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de buscar por id com erro (Status 404)")
	void teste4() throws Exception {
		//ARRANGE
		when(service.buscarPorId(anyLong())).thenThrow(new IllegalArgumentException("Usuário não encontrado!"));
		
		//ACT + ASSERT
		mvc.perform(get("/api/v1/usuarios/{id}",usuarioPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de criar usuário com sucesso (Status 201)")
	void teste5() throws Exception {
		//ARRANGE
		when(service.criarUsuario(usuarioPadrao)).thenReturn(usuarioPadrao);
		
		//ACT + ASSERT
		mvc.perform(post("/api/v1/usuarios")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(usuarioPadrao)))
		.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Testar o método de criar usuário com erro (Status 404)")
	void teste6() throws Exception{
		//ARRANGE
		when(service.criarUsuario(any(Usuario.class))).thenThrow(new IllegalArgumentException("Usuário já existente!"));
				
		//ACT + ASSERT
		mvc.perform(post("/api/v1/usuarios")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(usuarioPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar usuário com sucesso (Status 200)")
	void teste7() throws Exception{
		//ARRANGE
		when(service.atualizarUsuario(usuarioPadrao, usuarioPadrao.getId())).thenReturn(usuarioPadrao);
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/usuarios/{id}",usuarioPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(usuarioPadrao)))
		.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar usuário com erro (Status 404)")
	void teste8() throws Exception{
		//ARRANGE
		when(service.atualizarUsuario(any(Usuario.class), eq(usuarioPadrao.getId()))).thenThrow(new IllegalArgumentException("Usuário não encontrado!"));
		
		//ACT + ASSERT
		mvc.perform(put("/api/v1/usuarios/{id}",usuarioPadrao.getId())
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(mapper.writeValueAsString(usuarioPadrao)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Testar o método de remover usuário com sucesso (Status 204)")
	void teste9() throws Exception{
		//ARRANGE
		doNothing().when(service).removerUsuario(usuarioPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/usuarios/{id}", usuarioPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Testar o método de remover usuário com erro (Status 404)")
	void teste10() throws Exception{
		//ARRANGE
		doThrow(new IllegalArgumentException("Usuário não encontrado!")).when(service).removerUsuario(usuarioPadrao.getId());
		
		//ACT + ASSERT
		mvc.perform(delete("/api/v1/usuarios/{id}", usuarioPadrao.getId())
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
