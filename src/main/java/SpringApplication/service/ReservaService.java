package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringApplication.DTO.ReservaDTO;
import SpringApplication.exception.DadosInvalidosException;
import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Status;
import SpringApplication.model.Usuario;
import SpringApplication.repository.ReservaRepository;
import SpringApplication.repository.SalaRepository;
import SpringApplication.repository.UsuarioRepository;

@Service
public class ReservaService {
	
	@Autowired
	// Injeta as dependências no repositório
	private ReservaRepository reservaRepository;
	
	@Autowired
	// Injeta as dependências no repositório
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	// Injeta as dependências no repositório
	private SalaRepository salaRepository;
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public List<Reserva> listar() {
		return reservaRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public Reserva buscarPorId(Long id) {
		return reservaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada!"));
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Reserva criarReserva(ReservaDTO dto) {
		if(!dto.fim().isAfter(dto.inicio())) {
			throw new DadosInvalidosException("O fim da reserva deve ser depois do inicio!");
		}
		
		Usuario usuario = usuarioRepository.findById(dto.idUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
		Sala sala = salaRepository.findById(dto.idSala()).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
		
		boolean existeConflito = reservaRepository.existsBySalaReservadaAndInicioBeforeAndFimAfter(sala, dto.fim(), dto.inicio());
		if(existeConflito) {
			throw new IllegalStateException("A sala já possui uma reserva nesse horário!");
		}
		
		Reserva reserva = new Reserva();
		reserva.setSalaReservada(sala);
		reserva.setUsuarioAlugou(usuario);
		reserva.setInicio(dto.inicio());
		reserva.setFim(dto.fim());
		reserva.setStatus(Status.ATIVA);
		
		return reservaRepository.save(reserva);
	}
	
	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Reserva atualizarReserva(ReservaDTO dto, Long id) {
		if(!dto.fim().isAfter(dto.inicio())) {
			throw new IllegalArgumentException("O fim da reserva deve ser depois do inicio!");
		}
		
		Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada!"));
		Usuario usuario = usuarioRepository.findById(dto.idUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
		Sala sala = salaRepository.findById(dto.idSala()).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
		
		boolean existeConflito = reservaRepository.existsBySalaReservadaAndInicioBeforeAndFimAfterAndIdNot(sala, dto.fim(), dto.inicio(), id);
		if(existeConflito) {
			throw new IllegalStateException("A sala já possui uma reserva nesse horário!");
		}
		
		reserva.setSalaReservada(sala);
		reserva.setUsuarioAlugou(usuario);
		reserva.setInicio(dto.inicio());
		reserva.setFim(dto.fim());
		reserva.setStatus(Status.ATIVA);
		return reservaRepository.save(reserva);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void removerReserva(Long id) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada!"));
		reservaRepository.delete(reserva);
}
}