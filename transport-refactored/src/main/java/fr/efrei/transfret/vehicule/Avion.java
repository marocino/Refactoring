package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Avion cargo : ni matières dangereuses, ni soute réfrigérée.
 */
public class Avion extends Vehicule implements Ravitaillable {

    private static final int ROULAGE_PISTE_KM = 2;
    private static final int ALTITUDE_CROISIERE_PIEDS = 35000;

    public Avion(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.AIR);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return !contient(colis, TypeMarchandise.DANGEREUX) && !contient(colis, TypeMarchandise.REFRIGERE);
    }

    @Override
    public void partir(int distanceKm) {
        faireLePlein();
        System.out.println("L'avion " + getImmatriculation() + " roule sur " + ROULAGE_PISTE_KM + " km de piste");
        System.out.println("L'avion " + getImmatriculation() + " vole à " + ALTITUDE_CROISIERE_PIEDS + " pieds");
    }

    @Override
    public void faireLePlein() {
        System.out.println("Plein effectué pour " + getImmatriculation());
    }
}
