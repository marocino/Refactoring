package fr.efrei.transfret.tarif;

/**
 * En aérien, on facture le plus grand du poids réel et du poids volumétrique.
 */
public class TarifAir extends TarifAuKilometre {

    private static final double PRIX_KM = 0.6;
    private static final double PRIX_KG_TAXABLE = 4.5;
    private static final double KG_PAR_M3 = 167;

    public TarifAir() {
        super(PRIX_KM, PRIX_KG_TAXABLE, SANS_MINIMUM);
    }

    @Override
    protected double poidsTaxable(DemandeTarif demande) {
        return Math.max(demande.poidsTotal(), demande.volumeTotal() * KG_PAR_M3);
    }
}
