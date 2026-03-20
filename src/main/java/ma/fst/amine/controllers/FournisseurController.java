package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Fournisseur;
import ma.fst.amine.repositories.FournisseurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurRepository fournisseurRepository;

    public FournisseurController(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fournisseurs", fournisseurRepository.findAll());
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
        return "redirect:/fournisseurs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow();
        model.addAttribute("fournisseur", fournisseur);
        return "fournisseurs/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        fournisseurRepository.deleteById(id);
        return "redirect:/fournisseurs";
    }
}