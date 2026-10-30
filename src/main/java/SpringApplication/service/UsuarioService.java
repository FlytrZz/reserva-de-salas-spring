package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringApplication.DTO.UsuarioDTO;
import SpringApplication.model.Usuario;
import SpringApplication.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	// Injeta as dependências no repositório
	private UsuarioRepository repository;
	
	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public List<Usuario> listar() {
		return repository.findAll();
	}

	@Transactional(readOnly = true)
	// Transactional com readOnly = true faz a consulta somente buscar dados de forma otimizada, sem monitorar alterações ou etc
	public Usuario buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Usuario criarUsuario(UsuarioDTO dto) {
		Usuario usuario = new Usuario();
		
		usuario.setNome(dto.nome());
		usuario.setEmail(dto.email());
		
		return repository.save(usuario);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Usuario atualizarUsuario(UsuarioDTO dto, Long id) {
		Usuario usuario = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
		
		usuario.setNome(dto.nome());
		usuario.setEmail(dto.email());
		
		return repository.save(usuario);
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void removerUsuario(Long id) {
		Usuario usuario = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
		repository.delete(usuario);
	}

}
