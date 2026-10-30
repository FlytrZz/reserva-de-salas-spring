package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringApplication.DTO.SalaDTO;
import SpringApplication.model.Sala;
import SpringApplication.repository.SalaRepository;

@Service
public class SalaService {
	
	@Autowired
	// Injeta as dependências no repositório
	private SalaRepository repository;

	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public List<Sala> listar() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public Sala buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Sala criarSala(SalaDTO dto) {
		Sala sala = new Sala();
		
		sala.setNome(dto.nome());
		sala.setCapacidade(dto.capacidade());
		sala.setDisponibilidade(dto.disponibilidade());
		
		return repository.save(sala);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Sala atualizarSala(SalaDTO dto, Long id) {
		Sala sala = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
		
		sala.setNome(dto.nome());
		sala.setCapacidade(dto.capacidade());
		sala.setDisponibilidade(dto.disponibilidade());
		
		return repository.save(sala);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void apagarSala(Long id) {
		Sala sala = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada!"));
		repository.delete(sala);
	}

}
