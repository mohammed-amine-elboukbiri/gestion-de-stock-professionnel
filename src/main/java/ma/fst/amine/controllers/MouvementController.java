package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.entities.TypeMouvement;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/mouvements")
public class MouvementController {

    private final MouvementRepository mouvementRepository;
    private final ArticleRepository articleRepository;

    public MouvementController(MouvementRepository mouvementRepository, ArticleRepository articleRepository) {
        this.mouvementRepository = mouvementRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) TypeMouvement type,
                       @RequestParam(required = false) String dateDebut,
                       @RequestParam(required = false) String dateFin,
                       Model model) {

        List<Mouvement> mouvements = mouvementRepository.findAll();

        if (type != null) {
            mouvements = mouvements.stream()
                    .filter(m -> m.getType() == type)
                    .toList();
        }

        if (dateDebut != null && !dateDebut.isBlank() && dateFin != null && !dateFin.isBlank()) {
            LocalDate debut = LocalDate.parse(dateDebut);
            LocalDate fin = LocalDate.parse(dateFin);

            mouvements = mouvements.stream()
                    .filter(m -> !m.getDateMvt().isBefore(debut) && !m.getDateMvt().isAfter(fin))
                    .toList();
        }

        model.addAttribute("mouvements", mouvements);
        model.addAttribute("types", TypeMouvement.values());
        model.addAttribute("selectedType", type);

        return "mouvements/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Mouvement mouvement = new Mouvement();
        mouvement.setDateMvt(LocalDate.now());

        model.addAttribute("mouvement", mouvement);
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("types", TypeMouvement.values());

        return "mouvements/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("mouvement") Mouvement mouvement,
                       BindingResult result,
                       @RequestParam Long articleId,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("articles", articleRepository.findAll());
            model.addAttribute("types", TypeMouvement.values());
            return "mouvements/form";
        }

        Article article = articleRepository.findById(articleId).orElseThrow();
        mouvement.setArticle(article);

        if (mouvement.getType() == TypeMouvement.ENTREE) {
            article.setStock(article.getStock() + mouvement.getQuantite());
        } else {
            if (article.getStock() < mouvement.getQuantite()) {
                result.rejectValue("quantite", "error.mouvement", "Stock insuffisant pour cette sortie");
                model.addAttribute("articles", articleRepository.findAll());
                model.addAttribute("types", TypeMouvement.values());
                return "mouvements/form";
            }

            article.setStock(article.getStock() - mouvement.getQuantite());
        }

        articleRepository.save(article);
        mouvementRepository.save(mouvement);

        return "redirect:/mouvements";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        mouvementRepository.deleteById(id);
        return "redirect:/mouvements";
    }
}