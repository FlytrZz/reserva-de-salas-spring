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

import SpringApplication.model.Usuario;
import SpringApplication.service.UsuarioService;

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
	public ResponseEntity<Object> buscarPorId(@PathVariable Long id){
		try{
			Usuario usuarioBuscado = service.buscarPorId(id);
			return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> criarUsuario(@RequestBody Usuario usuario){
		try{
			Usuario usuarioNovo = service.criarUsuario(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNovo);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
		try{
			Usuario usuarioAtualizado = service.atualizarUsuario(usuario,id);
			return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerUsuario(@PathVariable Long id){
		try {
			service.removerUsuario(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
