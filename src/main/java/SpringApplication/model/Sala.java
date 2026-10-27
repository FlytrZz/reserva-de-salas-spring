package SpringApplication.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Sala {
	
	// Campo ID para identificador da Sala
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Campo NOME para nomear a Sala
	private String nome;
	
	// Campo CAPACIDADE para disponibilizar a capacidade total da Sala
	private int capacidade;
	
	// Campo DISPONIBILIDADE para averiguar se é possivel fazer reserva da Sala
	private boolean disponibilidade;
	
	// Lista de reservas, pois uma sala pode ter várias reservas
	@OneToMany(mappedBy = "salaReservada", cascade = CascadeType.ALL)
	private List<Reserva> reservas;

	// Construtor vazio
	public Sala() {}
	
	// Construtor da classe Sala
	public Sala(String nome, int capacidade, boolean disponibilidade) {
		this.nome = nome;
		this.capacidade = capacidade;
		this.disponibilidade = disponibilidade;
	}
	
	
	// Métodos validadores
	
	//1:
	public boolean validadorCondicionalSala(Reserva reserva) {
		if(this.getDisponibilidade() == false || this.getCapacidade() <= 0 || reserva.getFim().getHour() < reserva.getInicio().getHour()) {
			System.out.println("Não é possivel alugar a sala");
			return true;
		}
		return false;
	}
	
	//2:
	public boolean validadorReservaSobreposta(Reserva novaReserva) {
		for(Reserva existente : reservas) {
			boolean condicao = novaReserva.getInicio().isBefore(existente.getFim()) && novaReserva.getFim().isAfter(existente.getInicio());
			if(condicao) {
				return true;
			}
		}
		return false;
	}
	//*caso o fim seja igual ao inicio, a nova reserva será permitida
	
	
	// Metodo de adição
	public void adicionarReservas(Reserva reserva) {
		reservas.add(reserva);
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

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public boolean getDisponibilidade() {
		return disponibilidade;
	}

	public void setDisponibilidade(boolean disponibilidade) {
		this.disponibilidade = disponibilidade;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Long setId(Long id) {
		this.id = id;
		return id;
	}
	
}
