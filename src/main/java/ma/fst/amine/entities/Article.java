package ma.fst.amine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String code;

    @NotBlank
    private String designation;

    private String categorie;
    private String unite;      // pièce, mètre, kg...
    private String matiere;    // inox 201, inox 304...
    private String epaisseur;  // 0.7, 0.9, 1.2...

    @NotNull
    @Min(0)
    private Integer stock = 0;

    @NotNull
    @Min(0)
    private Integer seuilAlerte = 0;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixAchat = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixVente = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "article")
    private List<Mouvement> mouvements = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LigneBonLivraison> lignesBon = new ArrayList<>();

    public Article() {}

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDesignation() { return designation; }
    public String getCategorie() { return categorie; }
    public String getUnite() { return unite; }
    public String getMatiere() { return matiere; }
    public String getEpaisseur() { return epaisseur; }
    public Integer getStock() { return stock; }
    public Integer getSeuilAlerte() { return seuilAlerte; }
    public BigDecimal getPrixAchat() { return prixAchat; }
    public BigDecimal getPrixVente() { return prixVente; }
    public Fournisseur getFournisseur() { return fournisseur; }

    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public void setUnite(String unite) { this.unite = unite; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public void setEpaisseur(String epaisseur) { this.epaisseur = epaisseur; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setSeuilAlerte(Integer seuilAlerte) { this.seuilAlerte = seuilAlerte; }
    public void setPrixAchat(BigDecimal prixAchat) { this.prixAchat = prixAchat; }
    public void setPrixVente(BigDecimal prixVente) { this.prixVente = prixVente; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
}