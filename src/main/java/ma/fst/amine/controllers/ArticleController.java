package ma.fst.amine.controllers;

import ma.fst.amine.entities.Article;
import ma.fst.amine.repositories.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public List<Article> all() {
        return articleRepository.findAll();
    }

    @GetMapping("/categorie/{categorie}")
    public List<Article> byCategorie(@PathVariable String categorie) {
        return articleRepository.findByCategorieIgnoreCase(categorie);
    }

    @GetMapping("/stock-critique")
    public List<Article> stockCritique() {
        return articleRepository.findAll().stream()
                .filter(a -> a.getStock() <= a.getSeuilAlerte())
                .toList();
    }

    @GetMapping("/valeur-stock")
    public BigDecimal valeurStock() {
        return articleRepository.findAll().stream()
                .map(a -> a.getPrixAchat().multiply(BigDecimal.valueOf(a.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PostMapping
    public Article create(@RequestBody Article article) {
        return articleRepository.save(article);
    }

    @PutMapping("/{id}")
    public Article update(@PathVariable Long id, @RequestBody Article article) {
        Article existing = articleRepository.findById(id).orElseThrow();
        existing.setCode(article.getCode());
        existing.setDesignation(article.getDesignation());
        existing.setCategorie(article.getCategorie());
        existing.setUnite(article.getUnite());
        existing.setMatiere(article.getMatiere());
        existing.setEpaisseur(article.getEpaisseur());
        existing.setStock(article.getStock());
        existing.setSeuilAlerte(article.getSeuilAlerte());
        existing.setPrixAchat(article.getPrixAchat());
        existing.setPrixVente(article.getPrixVente());
        existing.setFournisseur(article.getFournisseur());
        return articleRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
    }
}