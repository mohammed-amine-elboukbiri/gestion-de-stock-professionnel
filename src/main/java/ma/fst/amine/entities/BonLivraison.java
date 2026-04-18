package ma.fst.amine.entities;

import ma.fst.amine.enums.StatutBon;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BonLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numero;

    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    private StatutBon statut = StatutBon.NON_PAYE;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montantPaye = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal resteAPayer = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "bonLivraison", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneBonLivraison> lignes = new ArrayList<>();

    public BonLivraison() {
        this.dateCreation = LocalDateTime.now();
        this.total = BigDecimal.ZERO;
        this.montantPaye = BigDecimal.ZERO;
        this.resteAPayer = BigDecimal.ZERO;
        this.statut = StatutBon.NON_PAYE;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public StatutBon getStatut() {
        return statut;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public BigDecimal getResteAPayer() {
        return resteAPayer;
    }

    public Client getClient() {
        return client;
    }

    public List<LigneBonLivraison> getLignes() {
        return lignes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setStatut(StatutBon statut) {
        this.statut = statut;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public void setResteAPayer(BigDecimal resteAPayer) {
        this.resteAPayer = resteAPayer;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setLignes(List<LigneBonLivraison> lignes) {
        this.lignes = lignes;
    }
}