package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Paiement;
import ma.fst.amine.repositories.ClientRepository;
import ma.fst.amine.repositories.PaiementRepository;
import ma.fst.amine.service.PaiementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    private final PaiementRepository paiementRepository;
    private final ClientRepository clientRepository;
    private final PaiementService paiementService;

    public PaiementController(PaiementRepository paiementRepository,
                              ClientRepository clientRepository,
                              PaiementService paiementService) {
        this.paiementRepository = paiementRepository;
        this.clientRepository = clientRepository;
        this.paiementService = paiementService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("paiements", paiementRepository.findAll());
        return "paiements/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("paiement", new Paiement());
        model.addAttribute("clients", clientRepository.findAll());
        return "paiements/form";
    }

    @PostMapping("/save/{clientId}")
    public String save(@PathVariable Long clientId,
                       @Valid @ModelAttribute("paiement") Paiement paiement,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientRepository.findAll());
            return "paiements/form";
        }

        paiementService.enregistrerPaiement(clientId, paiement);
        return "redirect:/paiements";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Paiement paiement = paiementService.getById(id);
        model.addAttribute("paiement", paiement);
        model.addAttribute("clients", clientRepository.findAll());
        return "paiements/form";
    }

    @PostMapping("/update/{id}/{clientId}")
    public String update(@PathVariable Long id,
                         @PathVariable Long clientId,
                         @Valid @ModelAttribute("paiement") Paiement paiement,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientRepository.findAll());
            return "paiements/form";
        }

        paiementService.updatePaiement(id, clientId, paiement);
        return "redirect:/paiements";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        paiementService.deletePaiement(id);
        return "redirect:/paiements";
    }
}