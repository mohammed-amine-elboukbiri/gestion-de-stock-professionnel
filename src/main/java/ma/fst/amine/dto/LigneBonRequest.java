package ma.fst.amine.dto;

public class LigneBonRequest {

    private String categorie;
    private Long articleId;
    private Integer quantite;

    public String getCategorie() {
        return categorie;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}