package SpringApplication.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
Optional<Reserva> findBySalaReservadaNomeOrUsuarioAlugouEmail(String string, String string2);
Optional<Reserva> findBySalaReservada(Sala sala);
Optional<Reserva> findByInicioAndFim(LocalDate inicio, LocalDate fim);
boolean existsBySalaReservadaAndInicioBeforeAndFimAfter(Sala sala, LocalDateTime fim, LocalDateTime inicio);
boolean existsBySalaReservadaAndInicioBeforeAndFimAfterAndIdNot(Sala sala, LocalDateTime fim, LocalDateTime inicio, Long id);
}
