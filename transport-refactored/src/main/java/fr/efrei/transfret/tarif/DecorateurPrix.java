package fr.efrei.transfret.tarif;

/**
 * Décorateur abstrait : calcule le prix du composant enveloppé, puis l'ajuste.
 */
public abstract class DecorateurPrix implements ComposantPrix {

    private final ComposantPrix enveloppe;

    protected DecorateurPrix(ComposantPrix enveloppe) {
        this.enveloppe = enveloppe;
    }

    @Override
    public final double calculer(DemandeTarif demande) {
        return ajuster(enveloppe.calculer(demande), demande);
    }

    protected abstract double ajuster(double prix, DemandeTarif demande);
}
