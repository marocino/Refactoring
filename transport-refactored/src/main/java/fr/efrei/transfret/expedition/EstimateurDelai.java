package fr.efrei.transfret.expedition;

import fr.efrei.transfret.modele.ModeTransport;

/**
 * Estime le délai de livraison, en jours, d'un trajet.
 */
public class EstimateurDelai {

    private static final double VITESSE_ROUTE_KMH = 70.0;
    private static final int HEURES_CONDUITE_PAR_JOUR = 9;
    private static final int HEURES_CONDUITE_PAR_JOUR_URGENT = 18;
    private static final double KM_PAR_JOUR_RAIL = 1440.0;
    private static final int JOURS_MANUTENTION_RAIL = 1;
    private static final int JOURS_GAGNES_RAIL_URGENT = 1;
    private static final double KM_PAR_JOUR_MER = 720.0;
    private static final int JOURS_PORTUAIRES_MER = 2;
    private static final int SEUIL_LONG_COURRIER_KM = 3000;
    private static final int JOURS_AIR = 1;
    private static final int JOURS_AIR_LONG_COURRIER = 2;
    private static final int DELAI_MINIMUM = 1;

    public int estimerJours(int distance, ModeTransport mode, boolean urgent) {
        int jours = switch (mode) {
            case ROUTE -> {
                double heures = distance / VITESSE_ROUTE_KMH;
                yield (int) Math.ceil(heures / (urgent ? HEURES_CONDUITE_PAR_JOUR_URGENT : HEURES_CONDUITE_PAR_JOUR));
            }
            case RAIL -> (int) Math.ceil(distance / KM_PAR_JOUR_RAIL) + JOURS_MANUTENTION_RAIL
                    - (urgent ? JOURS_GAGNES_RAIL_URGENT : 0);
            case MER -> (int) Math.ceil(distance / KM_PAR_JOUR_MER) + JOURS_PORTUAIRES_MER;
            case AIR -> urgent || distance <= SEUIL_LONG_COURRIER_KM ? JOURS_AIR : JOURS_AIR_LONG_COURRIER;
        };
        return Math.max(jours, DELAI_MINIMUM);
    }
}
