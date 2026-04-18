package ma.fst.amine.repositories;

import ma.fst.amine.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByClientId(Long clientId);
}