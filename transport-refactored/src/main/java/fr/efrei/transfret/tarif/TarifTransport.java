package fr.efrei.transfret.tarif;

/**
 * Stratégie : tarif de base HT d'un mode de transport, avant suppléments et remises.
 */
public interface TarifTransport {

    double prixDeBase(DemandeTarif demande);
}
