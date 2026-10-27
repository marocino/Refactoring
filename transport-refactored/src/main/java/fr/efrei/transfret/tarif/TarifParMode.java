package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;

import java.util.Map;

/**
 * Composant de base de la chaîne : délègue le tarif à la stratégie du mode de transport.
 */
public class TarifParMode implements ComposantPrix {

    private final Map<ModeTransport, TarifTransport> tarifs;

    public TarifParMode(Map<ModeTransport, TarifTransport> tarifs) {
        this.tarifs = Map.copyOf(tarifs);
    }

    @Override
    public double calculer(DemandeTarif demande) {
        TarifTransport tarif = tarifs.get(demande.mode());
        if (tarif == null) {
            throw new IllegalArgumentException("Mode inconnu : " + demande.mode());
        }
        return tarif.prixDeBase(demande);
    }
}
