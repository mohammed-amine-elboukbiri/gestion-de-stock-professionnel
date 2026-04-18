package ma.fst.amine.controllers;

import jakarta.validation.Valid;
import ma.fst.amine.entities.Client;
import ma.fst.amine.repositories.ClientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "clients/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("client") Client client,
                       BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clients/form";
        }

        clientRepository.save(client);
        redirectAttributes.addFlashAttribute("success", "Client enregistré avec succès.");
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientRepository.findById(id).orElseThrow());
        return "clients/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Client supprimé avec succès.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Impossible de supprimer ce client car il est lié à des paiements ou à d'autres données."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Une erreur est survenue lors de la suppression du client."
            );
        }

        return "redirect:/clients";
    }
}