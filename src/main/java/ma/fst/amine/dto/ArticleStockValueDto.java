package ma.fst.amine.dto;

import java.math.BigDecimal;

public class ArticleStockValueDto {
    private String code;
    private String designation;
    private Integer stock;
    private BigDecimal prixAchat;
    private BigDecimal valeur;

    public ArticleStockValueDto(String code, String designation, Integer stock, BigDecimal prixAchat, BigDecimal valeur) {
        this.code = code;
        this.designation = designation;
        this.stock = stock;
        this.prixAchat = prixAchat;
        this.valeur = valeur;
    }

    public String getCode() { return code; }
    public String getDesignation() { return designation; }
    public Integer getStock() { return stock; }
    public BigDecimal getPrixAchat() { return prixAchat; }
    public BigDecimal getValeur() { return valeur; }
}