package SpringApplication.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SalaDTO(Long id, @NotBlank String nome, @Positive int capacidade, boolean disponibilidade) {

}
