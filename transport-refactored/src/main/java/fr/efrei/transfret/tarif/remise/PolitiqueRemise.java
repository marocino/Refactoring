package fr.efrei.transfret.tarif.remise;

/**
 * Stratégie : remise accordée à un type de client.
 */
public interface PolitiqueRemise {

    /** @return le prix après remise */
    double appliquer(double prix);
}
