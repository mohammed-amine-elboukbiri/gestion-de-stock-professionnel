package ma.fst.amine.dto;

import java.util.List;

public class BonLivraisonRequest {
    private Long clientId;
    private List<LigneBonRequest> lignes;

    public Long getClientId() { return clientId; }
    public List<LigneBonRequest> getLignes() { return lignes; }

    public void setClientId(Long clientId) { this.clientId = clientId; }
    public void setLignes(List<LigneBonRequest> lignes) { this.lignes = lignes; }
}