package ma.fst.amine.repositories;

import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCategorieIgnoreCase(String categorie);

    List<Article> findByStockLessThanEqual(Integer seuil);

    Optional<Article> findByCode(String code);

    boolean existsByFournisseur(Fournisseur fournisseur);

    @Query("select distinct a.categorie from Article a where a.categorie is not null and a.categorie <> '' order by a.categorie")
    List<String> findDistinctCategories();
}