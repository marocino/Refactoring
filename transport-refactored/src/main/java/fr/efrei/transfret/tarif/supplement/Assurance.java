package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.tarif.ComposantPrix;
import fr.efrei.transfret.tarif.DecorateurPrix;
import fr.efrei.transfret.tarif.DemandeTarif;

/**
 * Assurance optionnelle : 2 % de la valeur déclarée totale, au minimum 15 €.
 */
public class Assurance extends DecorateurPrix {

    private static final double TAUX = 0.02;
    private static final double MINIMUM = 15;

    public Assurance(ComposantPrix enveloppe) {
        super(enveloppe);
    }

    @Override
    protected double ajuster(double prix, DemandeTarif demande) {
        if (!demande.assurance()) {
            return prix;
        }
        return prix + Math.max(demande.valeurTotale() * TAUX, MINIMUM);
    }
}
