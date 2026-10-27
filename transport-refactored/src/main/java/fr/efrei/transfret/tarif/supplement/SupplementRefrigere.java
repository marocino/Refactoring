package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.ComposantPrix;

/**
 * +20 % si au moins un colis est réfrigéré.
 */
public class SupplementRefrigere extends SupplementMarchandise {

    private static final double COEFFICIENT = 1.20;

    public SupplementRefrigere(ComposantPrix enveloppe) {
        super(enveloppe, TypeMarchandise.REFRIGERE, COEFFICIENT);
    }
}
