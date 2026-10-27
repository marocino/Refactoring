package fr.efrei.transfret.delai;

/**
 * 1 440 km par jour, plus un jour de manutention, sauf en urgence.
 */
public class DelaiRail implements DelaiTransport {

    private static final double KM_PAR_JOUR = 1440.0;
    private static final int JOURS_MANUTENTION = 1;
    private static final int JOURS_GAGNES_EN_URGENCE = 1;

    @Override
    public int jours(int distance, boolean urgent) {
        int jours = (int) Math.ceil(distance / KM_PAR_JOUR) + JOURS_MANUTENTION;
        return urgent ? jours - JOURS_GAGNES_EN_URGENCE : jours;
    }
}
