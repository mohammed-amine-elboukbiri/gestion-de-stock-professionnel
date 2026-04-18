package ma.fst.amine.entities;

import ma.fst.amine.enums.SourceMouvement;
import ma.fst.amine.enums.TypeMouvement;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Mouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeMouvement type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SourceMouvement source;

    @NotNull
    @Min(1)
    private Integer quantite;

    private LocalDateTime dateMvt;

    private String referenceDocument;
    private String observation;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Mouvement() {
        this.dateMvt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public TypeMouvement getType() { return type; }
    public SourceMouvement getSource() { return source; }
    public Integer getQuantite() { return quantite; }
    public LocalDateTime getDateMvt() { return dateMvt; }
    public String getReferenceDocument() { return referenceDocument; }
    public String getObservation() { return observation; }
    public Article getArticle() { return article; }

    public void setId(Long id) { this.id = id; }
    public void setType(TypeMouvement type) { this.type = type; }
    public void setSource(SourceMouvement source) { this.source = source; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public void setDateMvt(LocalDateTime dateMvt) { this.dateMvt = dateMvt; }
    public void setReferenceDocument(String referenceDocument) { this.referenceDocument = referenceDocument; }
    public void setObservation(String observation) { this.observation = observation; }
    public void setArticle(Article article) { this.article = article; }
}