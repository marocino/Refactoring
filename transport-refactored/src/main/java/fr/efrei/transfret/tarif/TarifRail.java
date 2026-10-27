package fr.efrei.transfret.tarif;

public class TarifRail extends TarifAuKilometre {

    private static final double PRIX_KM = 0.8;
    private static final double PRIX_KG = 0.07;
    private static final double MINIMUM = 150;

    public TarifRail() {
        super(PRIX_KM, PRIX_KG, MINIMUM);
    }
}
