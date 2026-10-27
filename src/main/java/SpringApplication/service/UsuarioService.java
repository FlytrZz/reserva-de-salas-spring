package SpringApplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		Optional<Usuario> usuario = repository.findById(id);
		if(usuario.isEmpty()) {
			throw new IllegalArgumentException("Usuario não encontrado!");
		} else {
			return usuario.get();
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Usuario criarUsuario(Usuario usuario) {
		usuario.setId(null);
		Optional<Usuario> usuarioExistente = repository.findByEmail(usuario.getEmail());
		if (usuarioExistente.isPresent()) {
			throw new IllegalArgumentException("Usuário já existente!");
		} else {
			return repository.save(usuario);
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public Usuario atualizarUsuario(Usuario usuario, Long id) {
		usuario.setId(id);
		Optional<Usuario> usuarioExistente = repository.findById(id);
		if(usuarioExistente.isEmpty()) {
			throw new IllegalArgumentException("Usuário não existe!"); 
		} else {
			Usuario usuarioAtualizado = usuarioExistente.get();
			usuarioAtualizado.setNome(usuario.getNome());
			usuarioAtualizado.setEmail(usuario.getEmail());
			return repository.save(usuarioAtualizado);
		}
	}

	@Transactional
	// Transactional cria uma parte do objeto na memoria e garante persistência, além de monitorar possiveis alterações
	public void removerUsuario(Long id) {
		Optional<Usuario> usuarioExistente = repository.findById(id);
		if(usuarioExistente.isEmpty()) {
			throw new IllegalArgumentException("Usuário não existe!"); 
		} else {
			repository.deleteById(id);
		}
	}

}
