package SpringApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SpringApplication.DTO.UsuarioDTO;
import SpringApplication.model.Usuario;
import SpringApplication.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public List<Usuario> listarTodos(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarPorId(@PathVariable("id") Long id){
			Usuario usuarioBuscado = service.buscarPorId(id);
			return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado);
	}
	
	@PostMapping
	public ResponseEntity<Object> criarUsuario(@Valid @RequestBody UsuarioDTO dto){
			Usuario usuarioNovo = service.criarUsuario(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNovo);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarUsuario(@Valid @RequestBody UsuarioDTO dto, @PathVariable("id") Long id){
			Usuario usuarioAtualizado = service.atualizarUsuario(dto,id);
			return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerUsuario(@PathVariable("id") Long id){
			service.removerUsuario(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
