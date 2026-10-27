package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.tarif.ComposantPrix;
import fr.efrei.transfret.tarif.DecorateurPrix;
import fr.efrei.transfret.tarif.DemandeTarif;

/**
 * +50 % pour une expédition urgente.
 */
public class SupplementUrgence extends DecorateurPrix {

    private static final double COEFFICIENT = 1.5;

    public SupplementUrgence(ComposantPrix enveloppe) {
        super(enveloppe);
    }

    @Override
    protected double ajuster(double prix, DemandeTarif demande) {
        return demande.urgent() ? prix * COEFFICIENT : prix;
    }
}
