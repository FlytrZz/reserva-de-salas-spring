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
import jakarta.validation.Valid;
import SpringApplication.DTO.SalaDTO;
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
	public ResponseEntity<Object> buscarPorId(@PathVariable("id") Long id){
			Sala salaBuscada = service.buscarPorId(id);
			return ResponseEntity.status(HttpStatus.OK).body(salaBuscada);
	}
	
	@PostMapping
	public ResponseEntity<Object> criarSala(@Valid @RequestBody SalaDTO dto){
			Sala novaSala = service.criarSala(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarSala(@Valid @RequestBody SalaDTO dto, @PathVariable("id") Long id){
			Sala salaAtualizada = service.atualizarSala(dto,id);
			return ResponseEntity.status(HttpStatus.OK).body(salaAtualizada);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerSala(@PathVariable("id") Long id){
			service.apagarSala(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
