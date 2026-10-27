package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Navire de commerce, sans conteneurs frigorifiques.
 */
public class Bateau extends Vehicule implements Ravitaillable {

    private static final double KM_PAR_MILLE_NAUTIQUE = 1.852;

    public Bateau(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.MER);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return !contient(colis, TypeMarchandise.REFRIGERE);
    }

    @Override
    public void partir(int distanceKm) {
        faireLePlein();
        int milles = (int) (distanceKm / KM_PAR_MILLE_NAUTIQUE);
        System.out.println("Le navire " + getImmatriculation() + " navigue sur " + milles + " milles nautiques");
    }

    @Override
    public void faireLePlein() {
        System.out.println("Plein effectué pour " + getImmatriculation());
    }
}
