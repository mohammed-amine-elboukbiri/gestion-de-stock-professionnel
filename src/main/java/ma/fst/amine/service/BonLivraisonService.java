package ma.fst.amine.service;

import ma.fst.amine.dto.BonLivraisonRequest;
import ma.fst.amine.dto.LigneBonRequest;
import ma.fst.amine.entities.Article;
import ma.fst.amine.entities.BonLivraison;
import ma.fst.amine.entities.Client;
import ma.fst.amine.entities.LigneBonLivraison;
import ma.fst.amine.entities.Mouvement;
import ma.fst.amine.enums.SourceMouvement;
import ma.fst.amine.enums.StatutBon;
import ma.fst.amine.enums.TypeMouvement;
import ma.fst.amine.exception.ResourceNotFoundException;
import ma.fst.amine.exception.StockInsuffisantException;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.BonLivraisonRepository;
import ma.fst.amine.repositories.ClientRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BonLivraisonService {

    private final BonLivraisonRepository bonRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final MouvementRepository mouvementRepository;

    public BonLivraisonService(BonLivraisonRepository bonRepository,
                               ClientRepository clientRepository,
                               ArticleRepository articleRepository,
                               MouvementRepository mouvementRepository) {
        this.bonRepository = bonRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.mouvementRepository = mouvementRepository;
    }

    @Transactional
    public BonLivraison creerBon(BonLivraisonRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("La requête du bon de livraison est invalide.");
        }

        if (request.getClientId() == null) {
            throw new IllegalArgumentException("Le client est obligatoire.");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        BonLivraison bon = new BonLivraison();
        bon.setNumero(genererNumeroBon());
        bon.setClient(client);
        bon.setStatut(StatutBon.NON_PAYE);

        List<LigneBonLivraison> lignes = new ArrayList<>();
        BigDecimal totalBon = BigDecimal.ZERO;

        if (request.getLignes() != null) {
            for (LigneBonRequest ligneRequest : request.getLignes()) {

                if (ligneRequest == null) {
                    continue;
                }

                if (ligneRequest.getArticleId() == null ||
                        ligneRequest.getQuantite() == null ||
                        ligneRequest.getQuantite() <= 0) {
                    continue;
                }

                Article article = articleRepository.findById(ligneRequest.getArticleId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Article introuvable : " + ligneRequest.getArticleId()
                        ));

                if (article.getStock() < ligneRequest.getQuantite()) {
                    throw new StockInsuffisantException(
                            "Stock insuffisant pour : " + article.getDesignation()
                    );
                }

                BigDecimal prix = article.getPrixVente();
                BigDecimal totalLigne = prix.multiply(BigDecimal.valueOf(ligneRequest.getQuantite()));

                LigneBonLivraison ligne = new LigneBonLivraison();
                ligne.setArticle(article);
                ligne.setQuantite(ligneRequest.getQuantite());
                ligne.setPrixUnitaire(prix);
                ligne.setTotal(totalLigne);
                ligne.setBonLivraison(bon);

                lignes.add(ligne);
                totalBon = totalBon.add(totalLigne);

                article.setStock(article.getStock() - ligneRequest.getQuantite());
                articleRepository.save(article);

                Mouvement mouvement = new Mouvement();
                mouvement.setArticle(article);
                mouvement.setType(TypeMouvement.SORTIE);
                mouvement.setSource(SourceMouvement.VENTE);
                mouvement.setQuantite(ligneRequest.getQuantite());
                mouvement.setReferenceDocument(bon.getNumero());
                mouvement.setObservation("Sortie via bon de livraison");
                mouvementRepository.save(mouvement);
            }
        }

        if (lignes.isEmpty()) {
            throw new IllegalArgumentException("Veuillez ajouter au moins un article valide.");
        }

        bon.setLignes(lignes);
        bon.setTotal(totalBon);
        bon.setMontantPaye(BigDecimal.ZERO);
        bon.setResteAPayer(totalBon);
        bon.setStatut(StatutBon.NON_PAYE);

        if (client.getSolde() == null) {
            client.setSolde(BigDecimal.ZERO);
        }

        client.setSolde(client.getSolde().add(totalBon));
        clientRepository.save(client);

        return bonRepository.save(bon);
    }

    private String genererNumeroBon() {
        Long lastId = bonRepository.findTopByOrderByIdDesc()
                .map(BonLivraison::getId)
                .orElse(0L);
        return "BL-" + String.format("%05d", lastId + 1);
    }
}