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

import SpringApplication.DTO.ReservaDTO;
import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Usuario;
import SpringApplication.service.ReservaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {
	
	@Autowired
	private ReservaService service;
	
	@GetMapping
	public List<Reserva> listarTodas(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarPorId(@PathVariable("id") Long id){
			Reserva reservaAchada = service.buscarPorId(id);
			return ResponseEntity.status(HttpStatus.OK).body(reservaAchada);
	}
	
	@PostMapping
	public ResponseEntity<Object> criarReserva(@Valid @RequestBody ReservaDTO dto){
			Reserva reservaNova = service.criarReserva(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> atualizarReserva(@Valid @RequestBody ReservaDTO dto, @PathVariable("id") Long id){
			Reserva reservaAtualizada = service.atualizarReserva(dto,id);
			return ResponseEntity.status(HttpStatus.OK).body(reservaAtualizada);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerReserva(@PathVariable("id") Long id){
			service.removerReserva(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
