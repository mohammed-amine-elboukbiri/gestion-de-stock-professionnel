package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Fournisseur;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.FournisseurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurRepository fournisseurRepository;
    private final ArticleRepository articleRepository;

    public FournisseurController(FournisseurRepository fournisseurRepository,
                                 ArticleRepository articleRepository) {
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "success", required = false) String success,
                       @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "fournisseurs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("fournisseur", new Fournisseur());
        return "fournisseurs/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("fournisseur") Fournisseur fournisseur,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "fournisseurs/form";
        }

        fournisseurRepository.save(fournisseur);
        return "redirect:/fournisseurs?success=Fournisseur enregistre avec succes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur introuvable : " + id));
        model.addAttribute("fournisseur", fournisseur);
        return "fournisseurs/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fournisseur introuvable : " + id));

        if (articleRepository.existsByFournisseur(fournisseur)) {
            return "redirect:/fournisseurs?error=Impossible de supprimer ce fournisseur car il est lie a un ou plusieurs articles";
        }

        fournisseurRepository.delete(fournisseur);
        return "redirect:/fournisseurs?success=Fournisseur supprime avec succes";
    }
}