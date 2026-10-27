package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.ArrayList;
import java.util.List;

/**
 * Véhicule de la flotte : il transporte des colis sur un mode de transport donné.
 * <p>
 * Chaque véhicule dit lui-même quelles marchandises il accepte ({@link #accepte}) et comment il part
 * ({@link #partir}) : aucune sous-classe ne refuse ce que la classe de base promet.
 */
public abstract class Vehicule {

    protected static final double TEMPERATURE_REFRIGERATION = 4;

    private final String immatriculation;
    private final double capaciteMax;
    private final ModeTransport mode;
    private final List<Colis> chargement = new ArrayList<>();
    private double chargeActuelle;
    private boolean disponible = true;

    protected Vehicule(String immatriculation, double capaciteMax, ModeTransport mode) {
        this.immatriculation = immatriculation;
        this.capaciteMax = capaciteMax;
        this.mode = mode;
    }

    /** Le véhicule est libre, dessert ce mode, a la place pour ces colis et sait les transporter. */
    public boolean peutPrendreEnCharge(List<Colis> colis, ModeTransport modeDemande) {
        return disponible
                && capaciteMax - chargeActuelle >= poids(colis)
                && mode == modeDemande
                && accepte(colis);
    }

    /** Le véhicule est équipé pour ces marchandises (réfrigération, certification…). */
    protected abstract boolean accepte(List<Colis> colis);

    /** Prépare le véhicule et le fait partir pour un trajet de la distance donnée. */
    public abstract void partir(int distanceKm);

    public void charger(Colis colis) {
        if (chargeActuelle + colis.getPoids() > capaciteMax) {
            System.out.println("ERREUR : capacité dépassée pour " + immatriculation);
            return;
        }
        chargement.add(colis);
        chargeActuelle = chargeActuelle + colis.getPoids();
    }

    public void decharger(Colis colis) {
        if (chargement.remove(colis)) {
            chargeActuelle = chargeActuelle - colis.getPoids();
        }
    }

    public void immobiliser() {
        disponible = false;
    }

    public void liberer() {
        disponible = true;
    }

    public boolean estDisponible() {
        return disponible;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public double getCapaciteMax() {
        return capaciteMax;
    }

    public double getChargeActuelle() {
        return chargeActuelle;
    }

    protected boolean transporte(TypeMarchandise type) {
        return contient(chargement, type);
    }

    protected static boolean contient(List<Colis> colis, TypeMarchandise type) {
        return colis.stream().anyMatch(c -> c.estDeType(type));
    }

    private static double poids(List<Colis> colis) {
        double total = 0;
        for (Colis c : colis) {
            total = total + c.getPoids();
        }
        return total;
    }
}
