package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.TypeMarchandise;

public class CamionFrigorifique extends Camion {

    public CamionFrigorifique(String immatriculation, double capaciteMax) {
        super(immatriculation, capaciteMax, false);
    }

    @Override
    public void charger(Colis colis) {
        // un camion frigo ne prend QUE des colis réfrigérés
        if (!colis.estDeType(TypeMarchandise.REFRIGERE)) {
            throw new IllegalArgumentException("Un camion frigorifique ne transporte que des produits réfrigérés");
        }
        super.charger(colis);
    }

    @Override
    public void activerRefrigeration(double temperature) {
        System.out.println("Réfrigération activée à " + temperature + "°C sur " + immatriculation);
    }
}
