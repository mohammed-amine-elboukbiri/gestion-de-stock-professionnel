package ma.fst.amine.repositories;

import ma.fst.amine.entities.BonLivraison;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BonLivraisonRepository extends JpaRepository<BonLivraison, Long> {

    Optional<BonLivraison> findTopByOrderByIdDesc();

    List<BonLivraison> findByClientIdOrderByDateCreationAsc(Long clientId);
}