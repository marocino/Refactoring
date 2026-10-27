package fr.efrei.transfret.tarif;

public class TarifRoute extends TarifAuKilometre {

    private static final double PRIX_KM = 1.2;
    private static final double PRIX_KG = 0.10;

    public TarifRoute() {
        super(PRIX_KM, PRIX_KG, SANS_MINIMUM);
    }
}
