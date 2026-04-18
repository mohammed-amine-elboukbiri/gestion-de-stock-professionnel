package ma.fst.amine.service;

import ma.fst.amine.dto.ArticleStockValueDto;
import ma.fst.amine.dto.DashboardStatsDto;
import ma.fst.amine.entities.Article;
import ma.fst.amine.enums.TypeMouvement;
import ma.fst.amine.repositories.ArticleRepository;
import ma.fst.amine.repositories.BonLivraisonRepository;
import ma.fst.amine.repositories.ClientRepository;
import ma.fst.amine.repositories.FournisseurRepository;
import ma.fst.amine.repositories.MouvementRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final ArticleRepository articleRepository;
    private final ClientRepository clientRepository;
    private final FournisseurRepository fournisseurRepository;
    private final BonLivraisonRepository bonLivraisonRepository;
    private final MouvementRepository mouvementRepository;

    public DashboardService(ArticleRepository articleRepository,
                            ClientRepository clientRepository,
                            FournisseurRepository fournisseurRepository,
                            BonLivraisonRepository bonLivraisonRepository,
                            MouvementRepository mouvementRepository) {
        this.articleRepository = articleRepository;
        this.clientRepository = clientRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.bonLivraisonRepository = bonLivraisonRepository;
        this.mouvementRepository = mouvementRepository;
    }

    public DashboardStatsDto getStats() {
        DashboardStatsDto dto = new DashboardStatsDto();

        List<Article> articles = articleRepository.findAll();

        dto.setTotalArticles(articleRepository.count());
        dto.setTotalClients(clientRepository.count());
        dto.setTotalFournisseurs(fournisseurRepository.count());
        dto.setTotalBons(bonLivraisonRepository.count());

        dto.setStockCritique(
                articles.stream()
                        .filter(a -> a.getStock() <= a.getSeuilAlerte())
                        .count()
        );

        BigDecimal valeurStock = articles.stream()
                .map(a -> a.getPrixAchat().multiply(BigDecimal.valueOf(a.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setValeurStock(valeurStock);

        BigDecimal creances = clientRepository.findAll().stream()
                .map(c -> c.getSolde() == null ? BigDecimal.ZERO : c.getSolde())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalCreancesClients(creances);

        dto.setTotalMouvementsEntree(
                mouvementRepository.findAll().stream().filter(m -> m.getType() == TypeMouvement.ENTREE).count()
        );

        dto.setTotalMouvementsSortie(
                mouvementRepository.findAll().stream().filter(m -> m.getType() == TypeMouvement.SORTIE).count()
        );

        return dto;
    }

    public List<Article> getArticlesCritiques() {
        return articleRepository.findAll().stream()
                .filter(a -> a.getStock() <= a.getSeuilAlerte())
                .sorted(Comparator.comparing(Article::getStock))
                .toList();
    }

    public List<ArticleStockValueDto> getTopValeurStock() {
        return articleRepository.findAll().stream()
                .map(a -> new ArticleStockValueDto(
                        a.getCode(),
                        a.getDesignation(),
                        a.getStock(),
                        a.getPrixAchat(),
                        a.getPrixAchat().multiply(BigDecimal.valueOf(a.getStock()))
                ))
                .sorted((a, b) -> b.getValeur().compareTo(a.getValeur()))
                .limit(10)
                .toList();
    }
}