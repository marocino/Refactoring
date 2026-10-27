package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.ComposantPrix;

/**
 * +30 % si au moins un colis est dangereux.
 */
public class SupplementDangereux extends SupplementMarchandise {

    private static final double COEFFICIENT = 1.30;

    public SupplementDangereux(ComposantPrix enveloppe) {
        super(enveloppe, TypeMarchandise.DANGEREUX, COEFFICIENT);
    }
}
