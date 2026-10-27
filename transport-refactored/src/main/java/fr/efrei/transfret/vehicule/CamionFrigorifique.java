package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Camion frigorifique : il ne transporte que des produits réfrigérés.
 * <p>
 * Il le dit au moment de l'affectation ({@link #accepte}) au lieu de lever une exception au chargement.
 */
public class CamionFrigorifique extends Vehicule implements Ravitaillable, Refrigerant {

    public CamionFrigorifique(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, ModeTransport.ROUTE);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return colis.stream().allMatch(c -> c.estDeType(TypeMarchandise.REFRIGERE));
    }

    @Override
    public void partir(int distanceKm) {
        faireLePlein();
        activerRefrigeration(TEMPERATURE_REFRIGERATION);
        System.out.println("Le camion " + getImmatriculation() + " roule sur " + distanceKm + " km");
    }

    @Override
    public void faireLePlein() {
        System.out.println("Plein effectué pour " + getImmatriculation());
    }

    @Override
    public void activerRefrigeration(double temperature) {
        System.out.println("Réfrigération activée à " + temperature + "°C sur " + getImmatriculation());
    }
}
