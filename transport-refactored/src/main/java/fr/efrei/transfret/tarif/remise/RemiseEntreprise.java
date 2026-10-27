package fr.efrei.transfret.tarif.remise;

/**
 * 10 % de remise, 15 % au-delà de 5 000 €.
 */
public class RemiseEntreprise implements PolitiqueRemise {

    private static final double SEUIL = 5000;
    private static final double COEF = 0.90;
    private static final double COEF_AU_DELA_DU_SEUIL = 0.85;

    @Override
    public double appliquer(double prix) {
        return prix * (prix > SEUIL ? COEF_AU_DELA_DU_SEUIL : COEF);
    }
}
