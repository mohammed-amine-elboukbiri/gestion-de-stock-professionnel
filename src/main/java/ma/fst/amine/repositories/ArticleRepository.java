package ma.fst.amine.repositories;

import ma.fst.amine.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCategorieContainingIgnoreCase(String categorie);
}