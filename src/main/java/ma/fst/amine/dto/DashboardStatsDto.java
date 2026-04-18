package ma.fst.amine.dto;

import java.math.BigDecimal;

public class DashboardStatsDto {
    private long totalArticles;
    private long totalClients;
    private long totalFournisseurs;
    private long totalBons;
    private long stockCritique;
    private BigDecimal valeurStock;
    private BigDecimal totalCreancesClients;
    private long totalMouvementsEntree;
    private long totalMouvementsSortie;

    public long getTotalArticles() { return totalArticles; }
    public void setTotalArticles(long totalArticles) { this.totalArticles = totalArticles; }

    public long getTotalClients() { return totalClients; }
    public void setTotalClients(long totalClients) { this.totalClients = totalClients; }

    public long getTotalFournisseurs() { return totalFournisseurs; }
    public void setTotalFournisseurs(long totalFournisseurs) { this.totalFournisseurs = totalFournisseurs; }

    public long getTotalBons() { return totalBons; }
    public void setTotalBons(long totalBons) { this.totalBons = totalBons; }

    public long getStockCritique() { return stockCritique; }
    public void setStockCritique(long stockCritique) { this.stockCritique = stockCritique; }

    public BigDecimal getValeurStock() { return valeurStock; }
    public void setValeurStock(BigDecimal valeurStock) { this.valeurStock = valeurStock; }

    public BigDecimal getTotalCreancesClients() { return totalCreancesClients; }
    public void setTotalCreancesClients(BigDecimal totalCreancesClients) { this.totalCreancesClients = totalCreancesClients; }

    public long getTotalMouvementsEntree() { return totalMouvementsEntree; }
    public void setTotalMouvementsEntree(long totalMouvementsEntree) { this.totalMouvementsEntree = totalMouvementsEntree; }

    public long getTotalMouvementsSortie() { return totalMouvementsSortie; }
    public void setTotalMouvementsSortie(long totalMouvementsSortie) { this.totalMouvementsSortie = totalMouvementsSortie; }
}