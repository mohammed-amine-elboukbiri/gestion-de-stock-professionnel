package ma.fst.amine.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mouvement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeMouvement type;

    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private int quantite;

    @NotNull(message = "La date est obligatoire")
    private LocalDate dateMvt;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}