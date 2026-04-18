package ma.fst.amine.repositories;

import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.enums.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MouvementRepository extends JpaRepository<Mouvement, Long> {

    List<Mouvement> findByType(TypeMouvement type);

    List<Mouvement> findByDateMvtBetween(LocalDateTime debut, LocalDateTime fin);

    boolean existsByArticleId(Long articleId);

    long countByArticleId(Long articleId);
}