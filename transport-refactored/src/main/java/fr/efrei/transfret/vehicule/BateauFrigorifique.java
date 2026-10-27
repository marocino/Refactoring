package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Navire équipé de conteneurs frigorifiques : il accepte aussi les produits réfrigérés.
 */
public class BateauFrigorifique extends Bateau implements Refrigerant {

    public BateauFrigorifique(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax);
    }

    @Override
    protected boolean accepte(List<Colis> colis) {
        return true;
    }

    @Override
    public void partir(int distanceKm) {
        if (transporte(TypeMarchandise.REFRIGERE)) {
            activerRefrigeration(TEMPERATURE_REFRIGERATION);
        }
        super.partir(distanceKm);
    }

    @Override
    public void activerRefrigeration(double temperature) {
        System.out.println("Conteneurs frigorifiques réglés à " + temperature + "°C");
    }
}
