package ma.fst.amine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    private String telephone;
    private String adresse;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    @OneToMany(mappedBy = "client")
    private List<BonLivraison> bons = new ArrayList<>();

    @OneToMany(mappedBy = "client")
    private List<Paiement> paiements = new ArrayList<>();

    public Client() {}

    public Client(String nom, String telephone, String adresse) {
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.solde = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public BigDecimal getSolde() { return solde; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
}