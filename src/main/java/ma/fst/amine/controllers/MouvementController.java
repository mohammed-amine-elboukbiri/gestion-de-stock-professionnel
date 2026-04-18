package ma.fst.amine.controllers;

import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.service.MouvementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementController {

    private final MouvementService mouvementService;

    public MouvementController(MouvementService mouvementService) {
        this.mouvementService = mouvementService;
    }

    @PostMapping("/{articleId}")
    public Mouvement create(@PathVariable Long articleId, @RequestBody Mouvement mouvement) {
        return mouvementService.enregistrer(mouvement, articleId);
    }
}