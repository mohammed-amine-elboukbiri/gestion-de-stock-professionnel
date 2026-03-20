package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Fournisseur;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.FournisseurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final FournisseurRepository fournisseurRepository;

    public ArticleController(ArticleRepository articleRepository, FournisseurRepository fournisseurRepository) {
        this.articleRepository = articleRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String categorie,
                       @RequestParam(required = false) Boolean critique,
                       Model model) {

        List<Article> articles;

        if (categorie != null && !categorie.isBlank()) {
            articles = articleRepository.findByCategorieContainingIgnoreCase(categorie);
        } else {
            articles = articleRepository.findAll();
        }

        if (Boolean.TRUE.equals(critique)) {
            articles = articles.stream()
                    .filter(a -> a.getStock() <= a.getSeuilAlerte())
                    .toList();
        }

        model.addAttribute("articles", articles);
        model.addAttribute("categorie", categorie);
        model.addAttribute("critique", critique != null && critique);

        return "articles/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "articles/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("article") Article article,
                       BindingResult result,
                       @RequestParam(required = false) Long fournisseurId,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("fournisseurs", fournisseurRepository.findAll());
            return "articles/form";
        }

        if (fournisseurId != null) {
            Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);
            article.setFournisseur(fournisseur);
        } else {
            article.setFournisseur(null);
        }

        articleRepository.save(article);
        return "redirect:/articles";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id).orElseThrow();
        model.addAttribute("article", article);
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        return "articles/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
        return "redirect:/articles";
    }
}