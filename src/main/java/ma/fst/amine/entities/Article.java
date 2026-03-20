package ma.fst.amine.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code est obligatoire")
    @Column(unique = true)
    private String code;

    @NotBlank(message = "La désignation est obligatoire")
    private String designation;

    @NotBlank(message = "La catégorie est obligatoire")
    private String categorie;

    @Min(value = 0, message = "Le stock doit être supérieur ou égal à 0")
    private int stock;

    @Min(value = 0, message = "Le seuil d'alerte doit être supérieur ou égal à 0")
    private int seuilAlerte;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;
}