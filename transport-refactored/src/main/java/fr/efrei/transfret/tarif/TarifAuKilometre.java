package fr.efrei.transfret.tarif;

/**
 * Tarif « prix au km + prix au kg taxable », avec un minimum de facturation éventuel.
 */
public abstract class TarifAuKilometre implements TarifTransport {

    protected static final double SANS_MINIMUM = 0;

    private final double prixKm;
    private final double prixKg;
    private final double minimum;

    protected TarifAuKilometre(double prixKm, double prixKg, double minimum) {
        this.prixKm = prixKm;
        this.prixKg = prixKg;
        this.minimum = minimum;
    }

    @Override
    public double prixDeBase(DemandeTarif demande) {
        return Math.max(demande.distance() * prixKm + poidsTaxable(demande) * prixKg, minimum);
    }

    /** Poids facturé, en kg : le poids réel par défaut. */
    protected double poidsTaxable(DemandeTarif demande) {
        return demande.poidsTotal();
    }
}
