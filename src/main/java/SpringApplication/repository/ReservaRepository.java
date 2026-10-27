package SpringApplication.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringApplication.model.Reserva;
import SpringApplication.model.Sala;
import SpringApplication.model.Usuario;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
Optional<Reserva> findByNameOrEmail(String string, String string2);
Optional<Reserva> findBySala(Sala sala);
Optional<Reserva> findByInicioAndByFim(LocalDate inicio, LocalDate fim);
}
