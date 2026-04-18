package ma.fst.amine.controllers;

import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Fournisseur;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.FournisseurRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/articles")
public class ArticleWebController {

    private final ArticleRepository articleRepository;
    private final FournisseurRepository fournisseurRepository;
    private final MouvementRepository mouvementRepository;

    public ArticleWebController(ArticleRepository articleRepository,
                                FournisseurRepository fournisseurRepository,
                                MouvementRepository mouvementRepository) {
        this.articleRepository = articleRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.mouvementRepository = mouvementRepository;
    }

    @GetMapping
    public String listArticles(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false, defaultValue = "false") boolean critique,
            Model model
    ) {
        List<Article> articles = articleRepository.findAll();

        if (categorie != null && !categorie.trim().isEmpty()) {
            String recherche = categorie.trim().toLowerCase();
            articles = articles.stream()
                    .filter(a -> a.getCategorie() != null &&
                            a.getCategorie().toLowerCase().contains(recherche))
                    .toList();
        }

        if (critique) {
            articles = articles.stream()
                    .filter(a -> a.getStock() != null &&
                            a.getSeuilAlerte() != null &&
                            a.getStock() <= a.getSeuilAlerte())
                    .toList();
        }

        List<String> categories = articleRepository.findAll().stream()
                .map(Article::getCategorie)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();

        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
        model.addAttribute("categorie", categorie);
        model.addAttribute("critique", critique);

        return "articles/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "articles/form";
    }

    @PostMapping("/save")
    public String saveArticle(@ModelAttribute Article article,
                              @RequestParam(required = false) Long fournisseurId,
                              RedirectAttributes redirectAttributes) {

        if (fournisseurId != null) {
            Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);
            article.setFournisseur(fournisseur);
        } else {
            article.setFournisseur(null);
        }

        articleRepository.save(article);
        redirectAttributes.addFlashAttribute("successMessage", "Article enregistré avec succès.");
        return "redirect:/articles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Article article = articleRepository.findById(id).orElse(null);

        if (article == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Article introuvable.");
            return "redirect:/articles";
        }

        model.addAttribute("article", article);
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "articles/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Article article = articleRepository.findById(id).orElse(null);

        if (article == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Article introuvable.");
            return "redirect:/articles";
        }

        if (mouvementRepository.existsByArticleId(id)) {
            long nbMouvements = mouvementRepository.countByArticleId(id);
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Impossible de supprimer l'article \"" + article.getDesignation()
                            + "\" car il est lié à " + nbMouvements + " mouvement(s)."
            );
            return "redirect:/articles";
        }

        articleRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Article supprimé avec succès.");
        return "redirect:/articles";
    }
}