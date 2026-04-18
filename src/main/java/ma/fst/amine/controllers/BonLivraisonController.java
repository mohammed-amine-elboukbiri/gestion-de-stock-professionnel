package ma.fst.amine.controllers;

import ma.fst.amine.dto.BonLivraisonRequest;
import ma.fst.amine.dto.LigneBonRequest;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.BonLivraisonRepository;
import ma.fst.amine.repositories.ClientRepository;
import ma.fst.amine.service.BonLivraisonService;
import ma.fst.amine.service.PdfService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/bons")
public class BonLivraisonController {

    private final BonLivraisonRepository bonRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final BonLivraisonService bonLivraisonService;
    private final PdfService pdfService;

    public BonLivraisonController(BonLivraisonRepository bonRepository,
                                  ClientRepository clientRepository,
                                  ArticleRepository articleRepository,
                                  BonLivraisonService bonLivraisonService,
                                  PdfService pdfService) {
        this.bonRepository = bonRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.bonLivraisonService = bonLivraisonService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bons", bonRepository.findAll());
        return "bons/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        BonLivraisonRequest request = new BonLivraisonRequest();
        ArrayList<LigneBonRequest> lignes = new ArrayList<>();

        lignes.add(new LigneBonRequest());
        request.setLignes(lignes);

        model.addAttribute("bonRequest", request);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("categories", articleRepository.findDistinctCategories());

        return "bons/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("bonRequest") BonLivraisonRequest request) {
        bonLivraisonService.creerBon(request);
        return "redirect:/bons";
    }

    @GetMapping("/view/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("bon", bonRepository.findById(id).orElseThrow());
        return "bons/view";
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<InputStreamResource> exportPdf(@PathVariable Long id) {
        var bon = bonRepository.findById(id).orElseThrow();
        InputStreamResource file = new InputStreamResource(pdfService.genererBonLivraisonPdf(bon));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bon_" + bon.getNumero() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bonRepository.deleteById(id);
        return "redirect:/bons";
    }
}