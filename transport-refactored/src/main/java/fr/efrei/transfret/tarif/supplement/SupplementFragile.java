package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.ComposantPrix;

/**
 * +15 % si au moins un colis est fragile.
 */
public class SupplementFragile extends SupplementMarchandise {

    private static final double COEFFICIENT = 1.15;

    public SupplementFragile(ComposantPrix enveloppe) {
        super(enveloppe, TypeMarchandise.FRAGILE, COEFFICIENT);
    }
}
