package SpringApplication.DTO;

import java.time.LocalDateTime;

import SpringApplication.model.Status;
import jakarta.validation.constraints.NotNull;

public record ReservaDTO(@NotNull Long idUsuario, @NotNull Long idSala, @NotNull LocalDateTime inicio, @NotNull LocalDateTime fim) {

}
