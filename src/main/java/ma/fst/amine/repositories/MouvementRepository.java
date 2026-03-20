package ma.fst.amine.repositories;

import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.entities.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long> {
    List<Mouvement> findByType(TypeMouvement type);
    List<Mouvement> findByDateMvtBetween(LocalDate debut, LocalDate fin);
}