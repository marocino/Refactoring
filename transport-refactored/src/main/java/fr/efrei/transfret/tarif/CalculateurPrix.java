package fr.efrei.transfret.tarif;

import fr.efrei.transfret.commun.Montants;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.remise.PolitiqueRemise;

import java.util.Map;

/**
 * Calcule le prix HT d'un transport.
 */
public class CalculateurPrix {

    private static final double COEF_FRAGILE = 1.15;
    private static final double COEF_DANGEREUX = 1.30;
    private static final double COEF_REFRIGERE = 1.20;
    private static final double COEF_URGENCE = 1.5;

    private static final double TAUX_ASSURANCE = 0.02;
    private static final double MINIMUM_ASSURANCE = 15;

    private final Map<ModeTransport, TarifTransport> tarifs;
    private final Map<TypeClient, PolitiqueRemise> remises;

    public CalculateurPrix(Map<ModeTransport, TarifTransport> tarifs, Map<TypeClient, PolitiqueRemise> remises) {
        this.tarifs = Map.copyOf(tarifs);
        this.remises = Map.copyOf(remises);
    }

    public double calculerPrixHT(DemandeTarif demande) {
        double prix = tarif(demande.mode()).prixDeBase(demande);

        // suppléments
        if (demande.contient(TypeMarchandise.FRAGILE)) prix = prix * COEF_FRAGILE;
        if (demande.contient(TypeMarchandise.DANGEREUX)) prix = prix * COEF_DANGEREUX;
        if (demande.contient(TypeMarchandise.REFRIGERE)) prix = prix * COEF_REFRIGERE;
        if (demande.urgent()) prix = prix * COEF_URGENCE;

        prix = remise(demande.typeClient()).appliquer(prix);

        if (demande.assurance()) {
            prix = prix + Math.max(demande.valeurTotale() * TAUX_ASSURANCE, MINIMUM_ASSURANCE);
        }
        return Montants.arrondir(prix);
    }

    private TarifTransport tarif(ModeTransport mode) {
        TarifTransport tarif = tarifs.get(mode);
        if (tarif == null) {
            throw new IllegalArgumentException("Mode inconnu : " + mode);
        }
        return tarif;
    }

    private PolitiqueRemise remise(TypeClient type) {
        PolitiqueRemise remise = remises.get(type);
        if (remise == null) {
            throw new IllegalArgumentException("Type de client inconnu : " + type);
        }
        return remise;
    }
}
