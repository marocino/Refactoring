package fr.efrei.transfret.delai;

/**
 * Un jour, deux au-delà de 3 000 km, sauf en urgence.
 */
public class DelaiAir implements DelaiTransport {

    private static final int SEUIL_LONG_COURRIER_KM = 3000;
    private static final int JOURS = 1;
    private static final int JOURS_LONG_COURRIER = 2;

    @Override
    public int jours(int distance, boolean urgent) {
        return urgent || distance <= SEUIL_LONG_COURRIER_KM ? JOURS : JOURS_LONG_COURRIER;
    }
}
