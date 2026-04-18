package ma.fst.amine.service;

import ma.fst.amine.entities.BonLivraison;
import ma.fst.amine.entities.Client;
import ma.fst.amine.entities.Paiement;
import ma.fst.amine.enums.StatutBon;
import ma.fst.amine.exception.ResourceNotFoundException;
import ma.fst.amine.repositories.BonLivraisonRepository;
import ma.fst.amine.repositories.ClientRepository;
import ma.fst.amine.repositories.PaiementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final ClientRepository clientRepository;
    private final BonLivraisonRepository bonRepository;

    public PaiementService(PaiementRepository paiementRepository,
                           ClientRepository clientRepository,
                           BonLivraisonRepository bonRepository) {
        this.paiementRepository = paiementRepository;
        this.clientRepository = clientRepository;
        this.bonRepository = bonRepository;
    }

    @Transactional
    public Paiement enregistrerPaiement(Long clientId, Paiement paiement) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        if (client.getSolde() == null) {
            client.setSolde(BigDecimal.ZERO);
        }

        BigDecimal montantPaiement = paiement.getMontant() != null ? paiement.getMontant() : BigDecimal.ZERO;

        BigDecimal nouveauSolde = client.getSolde().subtract(montantPaiement);
        if (nouveauSolde.compareTo(BigDecimal.ZERO) < 0) {
            nouveauSolde = BigDecimal.ZERO;
        }

        paiement.setClient(client);
        client.setSolde(nouveauSolde);

        clientRepository.save(client);
        Paiement savedPaiement = paiementRepository.save(paiement);

        recalculerBonsClient(client.getId());

        return savedPaiement;
    }

    @Transactional(readOnly = true)
    public Paiement getById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable"));
    }

    @Transactional
    public Paiement updatePaiement(Long id, Long clientId, Paiement nouveauPaiement) {
        Paiement ancienPaiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable"));

        Client ancienClient = ancienPaiement.getClient();
        if (ancienClient.getSolde() == null) {
            ancienClient.setSolde(BigDecimal.ZERO);
        }

        BigDecimal ancienMontant = ancienPaiement.getMontant() != null ? ancienPaiement.getMontant() : BigDecimal.ZERO;
        ancienClient.setSolde(ancienClient.getSolde().add(ancienMontant));
        clientRepository.save(ancienClient);

        Client nouveauClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        if (nouveauClient.getSolde() == null) {
            nouveauClient.setSolde(BigDecimal.ZERO);
        }

        BigDecimal nouveauMontant = nouveauPaiement.getMontant() != null ? nouveauPaiement.getMontant() : BigDecimal.ZERO;
        BigDecimal nouveauSolde = nouveauClient.getSolde().subtract(nouveauMontant);
        if (nouveauSolde.compareTo(BigDecimal.ZERO) < 0) {
            nouveauSolde = BigDecimal.ZERO;
        }

        nouveauClient.setSolde(nouveauSolde);

        ancienPaiement.setClient(nouveauClient);
        ancienPaiement.setMode(nouveauPaiement.getMode());
        ancienPaiement.setMontant(nouveauMontant);
        ancienPaiement.setReference(nouveauPaiement.getReference());
        ancienPaiement.setObservation(nouveauPaiement.getObservation());

        clientRepository.save(nouveauClient);
        Paiement savedPaiement = paiementRepository.save(ancienPaiement);

        recalculerBonsClient(ancienClient.getId());
        recalculerBonsClient(nouveauClient.getId());

        return savedPaiement;
    }

    @Transactional
    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable"));

        Client client = paiement.getClient();
        if (client.getSolde() == null) {
            client.setSolde(BigDecimal.ZERO);
        }

        BigDecimal montantPaiement = paiement.getMontant() != null ? paiement.getMontant() : BigDecimal.ZERO;
        client.setSolde(client.getSolde().add(montantPaiement));
        clientRepository.save(client);

        paiementRepository.delete(paiement);

        recalculerBonsClient(client.getId());
    }

    private void recalculerBonsClient(Long clientId) {
        List<BonLivraison> bons = bonRepository.findByClientIdOrderByDateCreationAsc(clientId);
        List<Paiement> paiements = paiementRepository.findByClientId(clientId);

        BigDecimal totalPayeDisponible = BigDecimal.ZERO;

        for (Paiement paiement : paiements) {
            if (paiement.getMontant() != null) {
                totalPayeDisponible = totalPayeDisponible.add(paiement.getMontant());
            }
        }

        for (BonLivraison bon : bons) {
            BigDecimal totalBon = bon.getTotal() != null ? bon.getTotal() : BigDecimal.ZERO;

            if (totalPayeDisponible.compareTo(BigDecimal.ZERO) <= 0) {
                bon.setMontantPaye(BigDecimal.ZERO);
                bon.setResteAPayer(totalBon);
                bon.setStatut(StatutBon.NON_PAYE);
            } else if (totalPayeDisponible.compareTo(totalBon) >= 0) {
                bon.setMontantPaye(totalBon);
                bon.setResteAPayer(BigDecimal.ZERO);
                bon.setStatut(StatutBon.PAYE);
                totalPayeDisponible = totalPayeDisponible.subtract(totalBon);
            } else {
                bon.setMontantPaye(totalPayeDisponible);
                bon.setResteAPayer(totalBon.subtract(totalPayeDisponible));
                bon.setStatut(StatutBon.PARTIELLEMENT_PAYE);
                totalPayeDisponible = BigDecimal.ZERO;
            }

            bonRepository.save(bon);
        }
    }
}