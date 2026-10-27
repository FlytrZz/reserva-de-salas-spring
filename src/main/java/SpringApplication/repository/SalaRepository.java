package SpringApplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringApplication.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long>{
	Optional<Sala> findByNome(String nome);
}
