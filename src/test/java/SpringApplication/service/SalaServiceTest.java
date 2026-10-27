package SpringApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Usuario;
import SpringApplication.repository.SalaRepository;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {
	
	@Mock
	private SalaRepository repository;
	
	@InjectMocks
	private SalaService service;
	
	private Sala sala;

	@Test
	@DisplayName("Testar se o método de listagem está funcionando")
	void teste1() {
		//ARRANGE
		Sala sala = new Sala("Sala 1B",50,true);
		Sala sala2 = new Sala("Sala 2B",45,false);
		List<Sala> salas = List.of(sala,sala2);
		
		when(repository.findAll()).thenReturn(salas);
		
		//ACT
		List<Sala> resultado = service.listar();
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(2, resultado.size());
		verify(repository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Testar método de buscar por ID caso dê certo")
	void teste2() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 1B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(sala));
		
		//ACT
		Sala resultado = service.buscarPorId(id);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(id, resultado.getId());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar método de buscar por ID caso dê errado")
	void teste3() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 1B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionID = assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(id));
				
		//ASSERT
		Assertions.assertEquals("Sala não encontrada!", exceptionID.getMessage());		
		verify(repository, times(1)).findById(id);	
	}
	
	@Test
	@DisplayName("Testar método de criar sala caso dê certo")
	void teste4() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 1B",50,true);
		sala.setId(id);
				
		when(repository.findByNome(sala.getNome())).thenReturn(Optional.empty());
		when(repository.save(any(Sala.class))).thenReturn(sala);
		
		//ACT
		Sala resultado = service.criarSala(sala);	
				
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals("Sala 1B", sala.getNome());
		verify(repository, times(1)).findByNome(sala.getNome());
		verify(repository, times(1)).save(any(Sala.class));
	}
	
	@Test
	@DisplayName("Testar método de criar sala caso dê errado")
	void teste5() {
		//ARRANGE
		Long id = 99L;
		Sala salaNova = new Sala("Sala 1B",50,true);
		salaNova.setId(id);
		
		when(repository.findByNome(salaNova.getNome())).thenReturn(Optional.of(salaNova));
		
		//ACT
		Exception exceptionCreate = assertThrows(IllegalArgumentException.class, () -> service.criarSala(salaNova));
		
		//ASSERT
		Assertions.assertEquals("Sala já existente!", exceptionCreate.getMessage());
		verify(repository, times(1)).findByNome(salaNova.getNome());
	}
	
	@Test
	@DisplayName("Testar método de atualizar sala caso dê certo")
	void teste6() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 1B",30,true);
		Sala salaNova = new Sala("Sala 2B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(sala));
		when(repository.save(any(Sala.class))).thenReturn(salaNova);
		
		//ACT
		Sala resultado = service.atualizarSala(salaNova, id);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(salaNova.getDisponibilidade(), resultado.getDisponibilidade());
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).save(any(Sala.class));
	}
	
	@Test
	@DisplayName("Testar método de atualizar sala caso dê errado")
	void teste7() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 1B",30,true);
		Sala salaNova = new Sala("Sala 2B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionAtualiza = assertThrows(IllegalArgumentException.class, () -> service.atualizarSala(salaNova, id));
		
		//ASSERT
		Assertions.assertEquals("Sala não encontrada para atualização dos dados!", exceptionAtualiza.getMessage());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar método de remover sala caso dê certo")
	void teste8() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 2B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(sala));
		
		//ACT
		service.apagarSala(id);
		
		//ASSERT
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).deleteById(id);
	}
	
	@Test
	@DisplayName("Testar método de remover sala caso dê errado")
	void teste9() {
		//ARRANGE
		Long id = 99L;
		Sala sala = new Sala("Sala 2B",50,true);
		sala.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionRemove = assertThrows(IllegalArgumentException.class, () -> service.apagarSala(id));
		
		//ASSERT
		Assertions.assertEquals("Sala não encontrada para remoção!", exceptionRemove.getMessage());
		verify(repository,times(1)).findById(id);
	}
}
