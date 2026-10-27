package fr.efrei.transfret.vehicule;

import java.util.List;

/**
 * Ensemble des véhicules de l'entreprise, dans leur ordre de priorité d'affectation.
 */
public class Flotte {

    private final List<Vehicule> vehicules;

    public Flotte(List<Vehicule> vehicules) {
        this.vehicules = List.copyOf(vehicules);
    }

    public List<Vehicule> vehicules() {
        return vehicules;
    }
}
