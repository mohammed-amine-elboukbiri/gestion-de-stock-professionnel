package ma.fst.amine.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class LigneBonLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantite;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "bon_id")
    private BonLivraison bonLivraison;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Long getId() { return id; }
    public Integer getQuantite() { return quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public BigDecimal getTotal() { return total; }
    public BonLivraison getBonLivraison() { return bonLivraison; }
    public Article getArticle() { return article; }

    public void setId(Long id) { this.id = id; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setBonLivraison(BonLivraison bonLivraison) { this.bonLivraison = bonLivraison; }
    public void setArticle(Article article) { this.article = article; }
}