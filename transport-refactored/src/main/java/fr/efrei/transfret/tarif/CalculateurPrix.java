package fr.efrei.transfret.tarif;

import fr.efrei.transfret.commun.Montants;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

public class CalculateurPrix {

    private static final double ROUTE_PRIX_KM = 1.2;
    private static final double ROUTE_PRIX_KG = 0.10;
    private static final double RAIL_PRIX_KM = 0.8;
    private static final double RAIL_PRIX_KG = 0.07;
    private static final double RAIL_MINIMUM = 150;
    private static final double MER_PRIX_KM = 0.5;
    private static final double MER_PRIX_KG = 0.03;
    private static final double MER_MINIMUM = 500;
    private static final double AIR_PRIX_KM = 0.6;
    private static final double AIR_PRIX_KG_TAXABLE = 4.5;
    private static final double AIR_KG_PAR_M3 = 167;

    private static final double COEF_FRAGILE = 1.15;
    private static final double COEF_DANGEREUX = 1.30;
    private static final double COEF_REFRIGERE = 1.20;
    private static final double COEF_URGENCE = 1.5;

    private static final double SEUIL_REMISE_ENTREPRISE = 5000;
    private static final double COEF_ENTREPRISE_AU_DELA_DU_SEUIL = 0.85;
    private static final double COEF_ENTREPRISE = 0.90;
    private static final double COEF_PREMIUM = 0.80;

    private static final double TAUX_ASSURANCE = 0.02;
    private static final double MINIMUM_ASSURANCE = 15;

    public double calculerPrixHT(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        double poids = 0;
        double volume = 0;
        double valeur = 0;
        boolean fragile = false;
        boolean dangereux = false;
        boolean refrigere = false;
        for (Colis c : colis) {
            poids = poids + c.getPoids();
            volume = volume + c.getVolume();
            valeur = valeur + c.getValeurDeclaree();
            if (c.estDeType(TypeMarchandise.FRAGILE)) fragile = true;
            if (c.estDeType(TypeMarchandise.DANGEREUX)) dangereux = true;
            if (c.estDeType(TypeMarchandise.REFRIGERE)) refrigere = true;
        }

        // tarif de base selon le mode
        double prix;
        switch (mode) {
            case ROUTE -> prix = distance * ROUTE_PRIX_KM + poids * ROUTE_PRIX_KG;
            case RAIL -> prix = Math.max(distance * RAIL_PRIX_KM + poids * RAIL_PRIX_KG, RAIL_MINIMUM);
            case MER -> prix = Math.max(distance * MER_PRIX_KM + poids * MER_PRIX_KG, MER_MINIMUM);
            case AIR -> {
                double poidsTaxable = Math.max(poids, volume * AIR_KG_PAR_M3);
                prix = distance * AIR_PRIX_KM + poidsTaxable * AIR_PRIX_KG_TAXABLE;
            }
            default -> throw new IllegalArgumentException("Mode inconnu : " + mode);
        }

        // suppléments
        if (fragile) prix = prix * COEF_FRAGILE;
        if (dangereux) prix = prix * COEF_DANGEREUX;
        if (refrigere) prix = prix * COEF_REFRIGERE;
        if (urgent) prix = prix * COEF_URGENCE;

        // remise client
        if (typeClient == TypeClient.ENTREPRISE) {
            if (prix > SEUIL_REMISE_ENTREPRISE) {
                prix = prix * COEF_ENTREPRISE_AU_DELA_DU_SEUIL;
            } else {
                prix = prix * COEF_ENTREPRISE;
            }
        } else if (typeClient == TypeClient.PREMIUM) {
            prix = prix * COEF_PREMIUM;
        }

        // assurance
        if (assurance) {
            prix = prix + Math.max(valeur * TAUX_ASSURANCE, MINIMUM_ASSURANCE);
        }
        return Montants.arrondir(prix);
    }
}
