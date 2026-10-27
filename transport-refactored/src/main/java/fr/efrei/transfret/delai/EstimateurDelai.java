package fr.efrei.transfret.delai;

import fr.efrei.transfret.modele.ModeTransport;

import java.util.Map;

/**
 * Estime le délai de livraison, en jours, d'un trajet.
 */
public class EstimateurDelai {

    private static final int DELAI_MINIMUM = 1;

    private final Map<ModeTransport, DelaiTransport> delais;

    public EstimateurDelai(Map<ModeTransport, DelaiTransport> delais) {
        this.delais = Map.copyOf(delais);
    }

    public int estimerJours(int distance, ModeTransport mode, boolean urgent) {
        DelaiTransport delai = delais.get(mode);
        if (delai == null) {
            throw new IllegalArgumentException("Mode inconnu : " + mode);
        }
        return Math.max(delai.jours(distance, urgent), DELAI_MINIMUM);
    }
}
