package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AffectationVehiculeTest {

    private final AffectationVehicule affectation = new AffectationVehicule(new Flotte(List.of(
            new Camion("STANDARD", 19000, false),
            new Camion("ADR", 12000, true),
            new CamionFrigorifique("FRIGO", 8000),
            new Train("TRAIN", 600000))));

    private static List<Colis> colis(double poids, TypeMarchandise type) {
        return List.of(new Colis("colis", poids, 10, 10, 10, type, 0));
    }

    private Optional<String> affecter(List<Colis> colis, ModeTransport mode) {
        return affectation.trouverVehicule(colis, mode).map(Vehicule::getImmatriculation);
    }

    @Test
    void premier_vehicule_compatible_dans_l_ordre_de_la_flotte() {
        assertEquals(Optional.of("STANDARD"), affecter(colis(100, TypeMarchandise.STANDARD), ModeTransport.ROUTE));
        assertEquals(Optional.of("ADR"), affecter(colis(100, TypeMarchandise.DANGEREUX), ModeTransport.ROUTE));
        assertEquals(Optional.of("FRIGO"), affecter(colis(100, TypeMarchandise.REFRIGERE), ModeTransport.ROUTE));
        assertEquals(Optional.of("TRAIN"), affecter(colis(100, TypeMarchandise.STANDARD), ModeTransport.RAIL));
    }

    private void charger(List<Colis> colis, ModeTransport mode) {
        Vehicule vehicule = affectation.trouverVehicule(colis, mode).orElseThrow();
        colis.forEach(vehicule::charger);
    }

    @Test
    void passe_au_suivant_si_la_capacite_restante_ne_suffit_pas() {
        charger(colis(15000, TypeMarchandise.STANDARD), ModeTransport.ROUTE);
        assertEquals(Optional.of("ADR"), affecter(colis(10000, TypeMarchandise.STANDARD), ModeTransport.ROUTE));
    }

    @Test
    void le_camion_frigorifique_ne_sert_pas_pour_le_reste() {
        charger(colis(15000, TypeMarchandise.STANDARD), ModeTransport.ROUTE);
        charger(colis(11000, TypeMarchandise.STANDARD), ModeTransport.ROUTE);
        assertTrue(affecter(colis(5000, TypeMarchandise.STANDARD), ModeTransport.ROUTE).isEmpty());
    }

    @Test
    void aucun_vehicule_pour_un_mode_absent_de_la_flotte() {
        assertTrue(affecter(colis(100, TypeMarchandise.STANDARD), ModeTransport.AIR).isEmpty());
    }
}
