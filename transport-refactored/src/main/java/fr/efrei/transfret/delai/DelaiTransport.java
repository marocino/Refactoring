package fr.efrei.transfret.delai;

/**
 * Stratégie : durée d'acheminement d'un mode de transport.
 */
public interface DelaiTransport {

    /** @return le nombre de jours de transport, éventuellement inférieur au minimum de livraison */
    int jours(int distance, boolean urgent);
}
