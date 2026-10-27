package fr.efrei.transfret.tarif.remise;

import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.tarif.ComposantPrix;
import fr.efrei.transfret.tarif.DecorateurPrix;
import fr.efrei.transfret.tarif.DemandeTarif;

import java.util.Map;

/**
 * Décorateur qui applique la remise du type de client, en déléguant à sa {@link PolitiqueRemise}.
 */
public class Remise extends DecorateurPrix {

    private final Map<TypeClient, PolitiqueRemise> politiques;

    public Remise(ComposantPrix enveloppe, Map<TypeClient, PolitiqueRemise> politiques) {
        super(enveloppe);
        this.politiques = Map.copyOf(politiques);
    }

    @Override
    protected double ajuster(double prix, DemandeTarif demande) {
        PolitiqueRemise politique = politiques.get(demande.typeClient());
        if (politique == null) {
            throw new IllegalArgumentException("Type de client inconnu : " + demande.typeClient());
        }
        return politique.appliquer(prix);
    }
}
