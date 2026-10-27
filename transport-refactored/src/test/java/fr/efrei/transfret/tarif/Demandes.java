package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/** Fabrique de demandes de tarif pour les tests unitaires. */
final class Demandes {

    private Demandes() {
    }

    static Colis colis(double poids, TypeMarchandise type) {
        return new Colis("colis", poids, 50, 40, 30, type, 100);
    }

    static DemandeTarif demande(int distance, ModeTransport mode, Colis... colis) {
        return new DemandeTarif(List.of(colis), distance, mode, false, false, TypeClient.PARTICULIER);
    }
}
