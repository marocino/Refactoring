package fr.efrei.transfret.delai;

/**
 * 70 km/h, 9 h de conduite par jour, 18 h en urgence (deux chauffeurs).
 */
public class DelaiRoute implements DelaiTransport {

    private static final double VITESSE_KMH = 70.0;
    private static final int HEURES_CONDUITE_PAR_JOUR = 9;
    private static final int HEURES_CONDUITE_PAR_JOUR_URGENT = 18;

    @Override
    public int jours(int distance, boolean urgent) {
        double heures = distance / VITESSE_KMH;
        return (int) Math.ceil(heures / (urgent ? HEURES_CONDUITE_PAR_JOUR_URGENT : HEURES_CONDUITE_PAR_JOUR));
    }
}
