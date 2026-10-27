package fr.efrei.transfret.tarif;

/**
 * Composant du patron Decorator : un calcul de prix HT, éventuellement enveloppé par d'autres.
 */
public interface ComposantPrix {

    double calculer(DemandeTarif demande);
}
