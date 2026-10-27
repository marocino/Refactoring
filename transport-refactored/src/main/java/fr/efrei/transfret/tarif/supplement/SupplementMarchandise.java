package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.ComposantPrix;
import fr.efrei.transfret.tarif.DecorateurPrix;
import fr.efrei.transfret.tarif.DemandeTarif;

/**
 * Majoration appliquée une fois dès qu'au moins un colis est d'un type donné.
 */
public abstract class SupplementMarchandise extends DecorateurPrix {

    private final TypeMarchandise type;
    private final double coefficient;

    protected SupplementMarchandise(ComposantPrix enveloppe, TypeMarchandise type, double coefficient) {
        super(enveloppe);
        this.type = type;
        this.coefficient = coefficient;
    }

    @Override
    protected double ajuster(double prix, DemandeTarif demande) {
        return demande.contient(type) ? prix * coefficient : prix;
    }
}
