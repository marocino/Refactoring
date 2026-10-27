package fr.efrei.transfret.delai;

/**
 * 360 km par jour, plus un jour d'écluses et de manutention.
 */
public class DelaiFluvial implements DelaiTransport {

    private static final double KM_PAR_JOUR = 360.0;
    private static final int JOURS_MANUTENTION = 1;

    @Override
    public int jours(int distance, boolean urgent) {
        return (int) Math.ceil(distance / KM_PAR_JOUR) + JOURS_MANUTENTION;
    }
}
