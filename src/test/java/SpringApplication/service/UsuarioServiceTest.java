package SpringApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import SpringApplication.model.Usuario;
import SpringApplication.repository.UsuarioRepository;
import SpringApplication.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	@Mock
	private UsuarioRepository repository;
	
	@InjectMocks
	private UsuarioService service;
	
	private Usuario usuario;

	@Test
	@DisplayName("Testar método de listagem")
	void teste1() {
		//ARRANGE
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		Usuario usuario2 = new Usuario("gabriel","gabriel11@gmail.com");
		List<Usuario> usuarios = List.of(usuario,usuario2);
		
		when(repository.findAll()).thenReturn(usuarios);
		
		//ACT
		List<Usuario> resultado = service.listar();
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(usuarios.size(), resultado.size());
		verify(repository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Testar o método buscar por ID caso dê certo")
	void teste2() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(usuario));
		
		//ACT
		Usuario resultado = service.buscarPorId(id);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(usuario.getId(), resultado.getId());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar o método buscar por ID caso dê errado")
	void teste3() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionID = assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(id));
		
		//ASSERT
		Assertions.assertEquals("Usuario não encontrado!", exceptionID.getMessage());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar o método criar usuário caso dê certo")
	void teste4() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());
		when(repository.save(any(Usuario.class))).thenReturn(usuario);
		
		//ACT
		Usuario resultado = service.criarUsuario(usuario);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(usuario.getEmail(), resultado.getEmail());
		verify(repository, times(1)).findByEmail(usuario.getEmail());
		verify(repository, times(1)).save(any(Usuario.class));
	}
	
	@Test
	@DisplayName("Testar o método criar usuário caso dê errado")
	void teste5() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
		
		//ACT
		Exception exceptionCreate = assertThrows(IllegalArgumentException.class, () -> service.criarUsuario(usuario));
		
		//ASSERT
		Assertions.assertEquals("Usuário já existente!", exceptionCreate.getMessage());
		verify(repository, times(1)).findByEmail(usuario.getEmail());
	}
	
	@Test
	@DisplayName("Testar o método atualizar usuário caso dê certo")
	void teste6() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		Usuario usuarioNovo = new Usuario("daniel","daniel90@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(usuario));
		when(repository.save(any(Usuario.class))).thenReturn(usuarioNovo);
		
		//ACT
		Usuario resultado = service.atualizarUsuario(usuarioNovo, id);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(usuarioNovo.getEmail(), resultado.getEmail());
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).save(any(Usuario.class));
	}
	
	@Test
	@DisplayName("Testar o método atualizar usuário caso dê errado")
	void teste7() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		Usuario usuarioNovo = new Usuario("daniel","daniel90@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionAtualiza = assertThrows(IllegalArgumentException.class, () -> service.atualizarUsuario(usuarioNovo, id));
		
		//ASSERT
		Assertions.assertEquals("Usuário não existe!", exceptionAtualiza.getMessage());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar o método de remover usuário caso dê certo")
	void teste8() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(usuario));
		
		//ACT
		service.removerUsuario(id);
		
		//ASSERT
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).deleteById(id);
		
	}
	
	@Test
	@DisplayName("Testar o método de remover usuário caso dê errado")
	void teste9() {
		//ARRANGE
		Long id = 2L;
		Usuario usuario = new Usuario("rodrigo", "rodrigo12@gmail.com");
		usuario.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionRemove = assertThrows(IllegalArgumentException.class, () -> service.removerUsuario(id));
		
		//ASSERT
		Assertions.assertEquals("Usuário não existe!",exceptionRemove.getMessage());
		verify(repository, times(1)).findById(id);
	}
}
