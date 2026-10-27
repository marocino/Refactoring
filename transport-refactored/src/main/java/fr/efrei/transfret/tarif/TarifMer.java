package fr.efrei.transfret.tarif;

public class TarifMer extends TarifAuKilometre {

    private static final double PRIX_KM = 0.5;
    private static final double PRIX_KG = 0.03;
    private static final double MINIMUM = 500;

    public TarifMer() {
        super(PRIX_KM, PRIX_KG, MINIMUM);
    }
}
