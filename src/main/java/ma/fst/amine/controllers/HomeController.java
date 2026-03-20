package ma.fst.amine.controllers;

import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.entities.TypeMouvement;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ArticleRepository articleRepository;
    private final MouvementRepository mouvementRepository;

    public HomeController(ArticleRepository articleRepository, MouvementRepository mouvementRepository) {
        this.articleRepository = articleRepository;
        this.mouvementRepository = mouvementRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        List<Mouvement> mouvements = mouvementRepository.findAll();

        long stockCritique = articles.stream()
                .filter(a -> a.getStock() <= a.getSeuilAlerte())
                .count();

        int totalStock = articles.stream()
                .mapToInt(Article::getStock)
                .sum();

        long totalEntrees = mouvements.stream()
                .filter(m -> m.getType() == TypeMouvement.ENTREE)
                .count();

        long totalSorties = mouvements.stream()
                .filter(m -> m.getType() == TypeMouvement.SORTIE)
                .count();

        model.addAttribute("totalArticles", articles.size());
        model.addAttribute("stockCritique", stockCritique);
        model.addAttribute("totalStock", totalStock);
        model.addAttribute("totalEntrees", totalEntrees);
        model.addAttribute("totalSorties", totalSorties);

        return "index";
    }
}