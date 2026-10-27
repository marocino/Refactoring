package fr.efrei.transfret.tarif.remise;

/**
 * 20 % de remise.
 */
public class RemisePremium implements PolitiqueRemise {

    private static final double COEF = 0.80;

    @Override
    public double appliquer(double prix) {
        return prix * COEF;
    }
}
