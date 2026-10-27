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

import SpringApplication.service.SalaService;
import SpringApplication.model.Sala;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {
	
	@Autowired
	private SalaService service;
	
	@GetMapping
	public List<Sala> listarTodas(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarPorId(@PathVariable Long id){
		try{
			Sala salaBuscada = service.buscarPorId(id);
			return ResponseEntity.status(HttpStatus.OK).body(salaBuscada);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<Object> criarSala(@RequestBody Sala sala){
		try{
			Sala novaSala = service.criarSala(sala);
			return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarSala(@PathVariable Long id, @RequestBody Sala sala){
		try{
			Sala salaAtualizada = service.atualizarSala(sala,id);
			return ResponseEntity.status(HttpStatus.OK).body(salaAtualizada);
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerSala(@PathVariable Long id){
		try {
			service.apagarSala(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
