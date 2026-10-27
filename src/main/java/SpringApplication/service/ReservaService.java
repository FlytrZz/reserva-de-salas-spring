package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Usuario;
import SpringApplication.repository.ReservaRepository;

@Service
public class ReservaService {
	
	@Autowired
	// Injeta as dependências no repositório
	private ReservaRepository repository;
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public List<Reserva> listar() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public Reserva buscarPorId(Long id) {
		Optional<Reserva> reserva = repository.findById(id);
		if(reserva.isEmpty()) {
			throw new IllegalArgumentException("Reserva não achada!");
		} else {
			return reserva.get();
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Reserva criarReserva(Reserva reserva, Usuario usuario, Sala sala) {
		reserva.setId(null);
		reserva.setUsuarioAlugou(usuario);
		reserva.setSalaReservada(sala);
		Optional<Reserva> reservaExistente = repository.findByNameOrEmail(sala.getNome(), usuario.getEmail());
		if(reservaExistente.isPresent()) {
			throw new IllegalArgumentException("Reserva já existente!");
		} else {
			return repository.save(reserva);
		}
	}
	
	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Reserva atualizarReserva(Reserva reserva, Long id) {
		reserva.setId(id);
		Optional<Reserva> reservaExistente = repository.findById(id);
		if(reservaExistente.isEmpty()) {
			throw new IllegalArgumentException("Reserva inexistente!");
		} else {
			Reserva reservaAtualizada = reservaExistente.get();
			reservaAtualizada.setInicio(reserva.getInicio());
			reservaAtualizada.setFim(reserva.getFim());
			reservaAtualizada.setUsuarioAlugou(reserva.getUsuarioAlugou());
			reservaAtualizada.setSalaReservada(reserva.getSalaReservada());
			return repository.save(reservaAtualizada);
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void removerReserva(Long id) {
		Optional<Reserva> reserva = repository.findById(id);
		if(reserva.isEmpty()) {
			throw new IllegalArgumentException("Reserva inexistente!");
		} else {
			repository.deleteById(id);
		}
	}

}
