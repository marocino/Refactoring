package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;

import java.util.List;
import java.util.Optional;

/**
 * Choisit le véhicule d'une expédition : le premier de la flotte qui peut la prendre en charge.
 */
public class AffectationVehicule {

    private final Flotte flotte;

    public AffectationVehicule(Flotte flotte) {
        this.flotte = flotte;
    }

    public Optional<Vehicule> trouverVehicule(List<Colis> colis, ModeTransport mode) {
        return flotte.vehicules().stream()
                .filter(v -> v.peutPrendreEnCharge(colis, mode))
                .findFirst();
    }
}
