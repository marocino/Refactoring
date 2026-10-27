package fr.efrei.transfret.tarif;

import fr.efrei.transfret.commun.Montants;

/**
 * Calcule le prix HT d'un transport, arrondi au centime.
 */
public class CalculateurPrix {

    private final ComposantPrix chaine;

    public CalculateurPrix(ComposantPrix chaine) {
        this.chaine = chaine;
    }

    public double calculerPrixHT(DemandeTarif demande) {
        return Montants.arrondir(chaine.calculer(demande));
    }
}
