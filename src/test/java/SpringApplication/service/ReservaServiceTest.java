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
import SpringApplication.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

	@Mock
	private ReservaRepository repository;
	
	@InjectMocks
	private ReservaService service;
	
	private Reserva reserva;
	
	@Test
	@DisplayName("Testar o método de listagem")
	void teste1() {
		//ARRANGE
		Reserva reserva = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
		Reserva segundaReserva = new Reserva(LocalDateTime.now(),LocalDateTime.now().plusHours(2));
		List<Reserva> listaReservas = List.of(reserva,segundaReserva);
		
		
		when(repository.findAll()).thenReturn(listaReservas);
		
		//ACT
		List<Reserva> resultado = service.listar();
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(2, resultado.size());
		verify(repository, times(1)).findAll();
	}
	
	@Test
	@DisplayName("Testar o metodo de busca por ID caso dê certo")
	void teste2() {
		//ARRANGE
		Long idBuscado = 1l;
		Reserva reservaMock = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
		reservaMock.setId(idBuscado);
		
		when(repository.findById(idBuscado)).thenReturn(Optional.of(reservaMock));
		
		//ACT
		Reserva reservaOP = service.buscarPorId(idBuscado);
		
		//ASSERT
		Assertions.assertNotNull(reservaOP);
		Assertions.assertEquals(idBuscado, reservaOP.getId());
		verify(repository, times(1)).findById(idBuscado);
	}
	
	@Test
	@DisplayName("Testar o método de busca por ID caso dê errado")
	void teste3() {
		//ARRANGE
		Long idBuscado = 1L;
		
		when(repository.findById(idBuscado)).thenReturn(Optional.empty());
		
		//ACT
		Exception exception = assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(idBuscado));
		
		//ASSERT
		Assertions.assertEquals("Reserva não achada!", exception.getMessage());
		verify(repository, times(1)).findById(idBuscado);
	}
	
	@Test
	@DisplayName("Testar o método para criação de reservas caso dê certo")
	void teste4() {
		//ARRANGE
		Long id = 99L;
		Sala salaNova = new Sala("Sala 1B",50,true);
		salaNova.setId(id);
		Usuario usuarioNovo = new Usuario("teste","teste@email.com");
		usuarioNovo.setId(id);
		Reserva reservaNova = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		reservaNova.setId(id);
		reservaNova.setUsuarioAlugou(usuarioNovo);
		reservaNova.setSalaReservada(salaNova);
		
		when(repository.findBySalaReservadaNomeOrUsuarioAlugouEmail(salaNova.getNome(), usuarioNovo.getEmail())).thenReturn(Optional.empty());
		when(repository.save(any(Reserva.class))).thenReturn(reservaNova);
		
		//ACT
		Reserva reservaOP = service.criarReserva(reservaNova, usuarioNovo, salaNova);
				
		//ASSERT
		Assertions.assertNotNull(reservaOP);
		Assertions.assertEquals(50, salaNova.getCapacidade());
		verify(repository, times(1)).findBySalaReservadaNomeOrUsuarioAlugouEmail(salaNova.getNome(), usuarioNovo.getEmail());
		verify(repository, times(1)).save(any(Reserva.class));
	}
	
	@Test
	@DisplayName("Testar o método para criação de reservas caso dê errado")
	void teste5() {
		//ARRANGE
		Sala salaNova = new Sala("Sala 1B",50,true);
		Usuario usuarioNovo = new Usuario("teste","teste@email.com");
		Reserva reservaNova = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		
		when(repository.findBySalaReservadaNomeOrUsuarioAlugouEmail(salaNova.getNome(), usuarioNovo.getEmail())).thenReturn(Optional.of(reservaNova));
		
		//ACT
		Exception exceptionCriacao = assertThrows(IllegalArgumentException.class, () -> service.criarReserva(reservaNova, usuarioNovo, salaNova));
		
		//ASSERT
		Assertions.assertEquals("Reserva já existente!",exceptionCriacao.getMessage());
		verify(repository, times(1)).findBySalaReservadaNomeOrUsuarioAlugouEmail(salaNova.getNome(), usuarioNovo.getEmail());
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva caso dê certo")
	void teste6() {
		//ARRANGE
		Long id = 99L;
		Reserva reservaExistente = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		Reserva reservaNova = new Reserva(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5));
		reservaExistente.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(reservaExistente));
		when(repository.save(any(Reserva.class))).thenReturn(reservaExistente);
		
		//ACT
		Reserva resultado = service.atualizarReserva(reservaNova, id);
		
		//ASSERT
		Assertions.assertNotNull(resultado);
		Assertions.assertEquals(reservaNova.getInicio(), resultado.getInicio());
		Assertions.assertEquals(reservaNova.getFim(), resultado.getFim());
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).save(any(Reserva.class));
	}
	
	@Test
	@DisplayName("Testar o método de atualizar reserva caso dê errado")
	void teste7() {
		//ARRANGE
		Long id = 99L;
		Reserva reservaExistente = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		Reserva reservaNova = new Reserva(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5));
		reservaExistente.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionAtualiza = assertThrows(IllegalArgumentException.class, () -> service.atualizarReserva(reservaNova, id));
		
		//ASSERT
		Assertions.assertEquals("Reserva inexistente!", exceptionAtualiza.getMessage());
		verify(repository, times(1)).findById(id);
	}
	
	@Test
	@DisplayName("Testar o método de remover reserva caso dê certo")
	void teste8() {
		//ARRANGE
		Long id = 99L;
		Reserva reservaExistente = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		reservaExistente.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.of(reservaExistente));
		
		//ACT
		service.removerReserva(id);
		
		//ASSERT
		verify(repository, times(1)).findById(id);
		verify(repository, times(1)).deleteById(id);
	}
	
	@Test
	@DisplayName("Testar o método de remover reserva caso dê errado")
	void teste9() {
		//ARRANGE
		Long id = 99L;
		Reserva reservaExistente = new Reserva(LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		reservaExistente.setId(id);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		//ACT
		Exception exceptionRemove = assertThrows(IllegalArgumentException.class, () -> service.removerReserva(id));
		
		//ASSERT
		Assertions.assertEquals("Reserva inexistente!", exceptionRemove.getMessage());
		verify(repository, times(1)).findById(id);
	}
}
