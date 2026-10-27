package fr.efrei.transfret.delai;

/**
 * 720 km par jour, plus deux jours d'opérations portuaires.
 */
public class DelaiMer implements DelaiTransport {

    private static final double KM_PAR_JOUR = 720.0;
    private static final int JOURS_PORTUAIRES = 2;

    @Override
    public int jours(int distance, boolean urgent) {
        return (int) Math.ceil(distance / KM_PAR_JOUR) + JOURS_PORTUAIRES;
    }
}
