package ma.fst.amine.service;

import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.enums.TypeMouvement;
import ma.fst.amine.exception.ResourceNotFoundException;
import ma.fst.amine.exception.StockInsuffisantException;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MouvementService {

    private final MouvementRepository mouvementRepository;
    private final ArticleRepository articleRepository;

    public MouvementService(MouvementRepository mouvementRepository, ArticleRepository articleRepository) {
        this.mouvementRepository = mouvementRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Mouvement enregistrer(Mouvement mouvement, Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable"));

        if (mouvement.getType() == TypeMouvement.ENTREE) {
            article.setStock(article.getStock() + mouvement.getQuantite());
        } else {
            if (article.getStock() < mouvement.getQuantite()) {
                throw new StockInsuffisantException("Stock insuffisant pour l'article : " + article.getDesignation());
            }
            article.setStock(article.getStock() - mouvement.getQuantite());
        }

        mouvement.setArticle(article);
        articleRepository.save(article);
        return mouvementRepository.save(mouvement);
    }
}