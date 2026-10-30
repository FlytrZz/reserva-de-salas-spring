package SpringApplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reserva {
	
	// Campo ID para identificador da Reserva
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Campo INICIO para demarcar o inicio da Reserva
	private LocalDateTime inicio;
	
	// Campo FIM para demarcar o final da Reserva
	private LocalDateTime fim;
	
	// Campo USUARIOALUGOU para referenciar o Usuário que reservou
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuarioAlugou;
	
	// Campo SALARESERVADA para referenciar a Sala que foi reservada
	@ManyToOne
	@JoinColumn(name = "sala_id")
	private Sala salaReservada;
	
	// Campo STATUS para verificação do status da reserva
	@Enumerated(EnumType.STRING)
	private Status status;
	
	// Construtor Vazio
	public Reserva() {}
	
	// Construtor da classe Reserva
	public Reserva(LocalDateTime inicio, LocalDateTime fim) {
		this.inicio = inicio;
		this.fim = fim;
	}
	
	
	// Getters e Setters
	public Long getId() {
		return id;
	}
	
	public LocalDateTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalDateTime inicio) {
		this.inicio = inicio;
	}

	public LocalDateTime getFim() {
		return fim;
	}

	public void setFim(LocalDateTime fim) {
		this.fim = fim;
	}

	public Usuario getUsuarioAlugou() {
		return usuarioAlugou;
	}

	public void setUsuarioAlugou(Usuario usuarioAlugou) {
		this.usuarioAlugou = usuarioAlugou;
	}

	public Sala getSalaReservada() {
		return salaReservada;
	}

	public void setSalaReservada(Sala salaReservada) {
		this.salaReservada = salaReservada;
	}

	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}


	public Long setId(Long id) {
		this.id = id;
		return id;
	}
}
