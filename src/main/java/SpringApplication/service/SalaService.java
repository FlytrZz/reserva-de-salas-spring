package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Optional<Sala> sala = repository.findById(id);
		if(sala.isEmpty()) {
			throw new IllegalArgumentException("Sala não encontrada!");
		} else {
			return sala.get();
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Sala criarSala(Sala sala) {
		sala.setId(null);
		Optional<Sala> salaExistente = repository.findByNome(sala.getNome());
		if(salaExistente.isPresent()) {
			throw new IllegalArgumentException("Sala já existente!");
		}
		return repository.save(sala);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Sala atualizarSala(Sala sala, Long id) {
		sala.setId(id);
		Optional<Sala> salaExistente = repository.findById(id);
		if(salaExistente.isEmpty()) {
			throw new IllegalArgumentException("Sala não encontrada para atualização dos dados!");
		} else {
			Sala salaAtualizada = salaExistente.get();
			salaAtualizada.setNome(sala.getNome());
			salaAtualizada.setCapacidade(sala.getCapacidade());
			salaAtualizada.setDisponibilidade(sala.getDisponibilidade());
			return repository.save(salaAtualizada);
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void apagarSala(Long id) {
		Optional<Sala> sala = repository.findById(id);
		if(sala.isEmpty()) {
			throw new IllegalArgumentException("Sala não encontrada para remoção!");
		} else {
			repository.deleteById(id);
		}
	}

}
