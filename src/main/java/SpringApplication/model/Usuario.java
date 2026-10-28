package SpringApplication.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {
	
	// Campo ID para identificador do Usuário
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Campo NOME para nomear o Usuário
	private String nome;
	
	// Campo EMAIL para garantir a comunicação e rastreabilidade da reserva do Usuário
	private String email;
	
	// Lista de reservas, pois um usuário pode ter várias reservas
	@OneToMany(mappedBy = "usuarioAlugou",cascade = CascadeType.ALL)
	private List<Reserva> reservas;
	
	// Construtor vazio
	public Usuario () {}

	// Construtor da classe Usuario
	public Usuario(String nome, String email) {
		this.nome = nome;
		this.email = email;
	}
	
	
	// Método para Reservar sala
	public void reservarSala(Usuario usuario,Sala sala,Reserva reserva) {
		if(reserva.getStatus() == Status.CANCELADA) {
			System.out.println("Essa reserva está cancelada!");
			return;
		}
		if(sala.validadorCondicionalSala(reserva)) {
			System.out.println("Não é possivel alugar a sala!");
			return;
			}
		if(sala.validadorReservaSobreposta(reserva)) {
			System.out.println("Conflito de horário!");
			return;
		}
		sala.adicionarReservas(reserva);
		reserva.setStatus(Status.ATIVA);
		}
	

	// Getters e Setters
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long setId(Long id) {
		this.id = id;
		return id;
	}
}
