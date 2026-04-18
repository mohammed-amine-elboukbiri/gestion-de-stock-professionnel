package ma.fst.amine.entities;

import ma.fst.amine.enums.ModePaiement;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModePaiement mode;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    private LocalDateTime datePaiement;

    private String reference;
    private String observation;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Paiement() {
        this.datePaiement = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public ModePaiement getMode() { return mode; }
    public BigDecimal getMontant() { return montant; }
    public LocalDateTime getDatePaiement() { return datePaiement; }
    public String getReference() { return reference; }
    public String getObservation() { return observation; }
    public Client getClient() { return client; }

    public void setId(Long id) { this.id = id; }
    public void setMode(ModePaiement mode) { this.mode = mode; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }
    public void setReference(String reference) { this.reference = reference; }
    public void setObservation(String observation) { this.observation = observation; }
    public void setClient(Client client) { this.client = client; }
}