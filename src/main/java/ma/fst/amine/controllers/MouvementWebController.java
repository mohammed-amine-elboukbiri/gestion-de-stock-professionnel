package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.enums.SourceMouvement;
import ma.fst.amine.enums.TypeMouvement;
import ma.fst.amine.exception.ResourceNotFoundException;
import ma.fst.amine.exception.StockInsuffisantException;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.MouvementRepository;
import ma.fst.amine.service.MouvementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mouvements")
public class MouvementWebController {

    private final MouvementRepository mouvementRepository;
    private final ArticleRepository articleRepository;
    private final MouvementService mouvementService;

    public MouvementWebController(MouvementRepository mouvementRepository,
                                  ArticleRepository articleRepository,
                                  MouvementService mouvementService) {
        this.mouvementRepository = mouvementRepository;
        this.articleRepository = articleRepository;
        this.mouvementService = mouvementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("mouvements", mouvementRepository.findAll());
        return "mouvements/list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("mouvement", new Mouvement());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("types", TypeMouvement.values());
        model.addAttribute("sources", SourceMouvement.values());
        return "mouvements/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("mouvement") Mouvement mouvement,
                       BindingResult bindingResult,
                       Model model) {

        if (mouvement.getArticle() == null || mouvement.getArticle().getId() == null) {
            bindingResult.rejectValue("article", "error.mouvement", "Article obligatoire");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("articles", articleRepository.findAll());
            model.addAttribute("types", TypeMouvement.values());
            model.addAttribute("sources", SourceMouvement.values());
            return "mouvements/form";
        }

        try {
            mouvementService.enregistrer(mouvement, mouvement.getArticle().getId());
            return "redirect:/mouvements";
        } catch (StockInsuffisantException e) {
            bindingResult.rejectValue("quantite", "error.mouvement", e.getMessage());
        } catch (ResourceNotFoundException e) {
            bindingResult.rejectValue("article", "error.mouvement", e.getMessage());
        }

        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("types", TypeMouvement.values());
        model.addAttribute("sources", SourceMouvement.values());
        return "mouvements/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        mouvementRepository.deleteById(id);
        return "redirect:/mouvements";
    }
}