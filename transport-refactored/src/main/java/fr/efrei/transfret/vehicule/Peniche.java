package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Péniche de transport fluvial, sans cale réfrigérée.
 */
public class Peniche extends Vehicule implements Ravitaillable {

    public Peniche(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.FLUVIAL);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return !contient(colis, TypeMarchandise.REFRIGERE);
    }

    @Override
    public void partir(int distanceKm) {
        faireLePlein();
        System.out.println("La péniche " + getImmatriculation() + " descend le fleuve sur " + distanceKm + " km");
    }

    @Override
    public void faireLePlein() {
        System.out.println("Plein effectué pour " + getImmatriculation());
    }
}
