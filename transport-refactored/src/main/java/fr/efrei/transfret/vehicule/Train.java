package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Train électrique : pas de plein à faire, pas de wagons frigorifiques.
 */
public class Train extends Vehicule {

    public Train(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.RAIL);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return !contient(colis, TypeMarchandise.REFRIGERE);
    }

    @Override
    public void partir(int distanceKm) {
        System.out.println("Le train " + getImmatriculation() + " circule sur " + distanceKm + " km de voies ferrées");
    }
}
