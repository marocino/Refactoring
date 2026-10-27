package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.Arrays;
import java.util.List;

/** Fabrique de demandes de tarif pour les tests unitaires. */
public final class Demandes {

    /** Composant de base factice : un prix fixe, pour observer l'effet d'un décorateur. */
    public static final ComposantPrix MILLE_EUROS = demande -> 1000.0;

    private Demandes() {
    }

    public static Colis colis(double poids, TypeMarchandise type) {
        return new Colis("colis", poids, 50, 40, 30, type, 100);
    }

    public static DemandeTarif demande(int distance, ModeTransport mode, Colis... colis) {
        return new DemandeTarif(List.of(colis), distance, mode, false, false, TypeClient.PARTICULIER);
    }

    public static DemandeTarif demande(TypeMarchandise... types) {
        List<Colis> colis = Arrays.stream(types).map(type -> colis(10, type)).toList();
        return new DemandeTarif(colis, 100, ModeTransport.ROUTE, false, false, TypeClient.PARTICULIER);
    }

    public static DemandeTarif demande(boolean urgent, boolean assurance, TypeClient typeClient, Colis... colis) {
        return new DemandeTarif(List.of(colis), 100, ModeTransport.ROUTE, urgent, assurance, typeClient);
    }
}
