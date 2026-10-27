package fr.efrei.transfret.commun;

/**
 * Règles d'arrondi et de TVA appliquées à tous les montants en euros.
 */
public final class Montants {

    private static final double CENTIMES_PAR_EURO = 100.0;
    private static final double TAUX_TTC = 1.2;

    private Montants() {
    }

    public static double arrondir(double valeur) {
        return Math.round(valeur * CENTIMES_PAR_EURO) / CENTIMES_PAR_EURO;
    }

    public static double ttc(double montantHT) {
        return arrondir(montantHT * TAUX_TTC);
    }
}
