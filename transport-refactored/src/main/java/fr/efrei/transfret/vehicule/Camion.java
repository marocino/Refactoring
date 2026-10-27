package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Camion classique : pas de réfrigéré ; matières dangereuses seulement s'il est certifié ADR.
 */
public class Camion extends Vehicule implements Ravitaillable {

    private final boolean certifieAdr;

    public Camion(String immatriculation, double capaciteMax, boolean certifieAdr) {
        super(immatriculation, capaciteMax, ModeTransport.ROUTE);
        this.certifieAdr = certifieAdr;
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return !contient(colis, TypeMarchandise.REFRIGERE)
                && (certifieAdr || !contient(colis, TypeMarchandise.DANGEREUX));
    }

    @Override
    public void partir(int distanceKm) {
        faireLePlein();
        System.out.println("Le camion " + getImmatriculation() + " roule sur " + distanceKm + " km");
    }

    @Override
    public void faireLePlein() {
        System.out.println("Plein effectué pour " + getImmatriculation());
    }
}
