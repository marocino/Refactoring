package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PenicheTest {

    private final Peniche peniche = new Peniche("PEN-0001", 2_500_000);

    private static List<Colis> colis(double poids, TypeMarchandise type) {
        return List.of(new Colis("colis", poids, 10, 10, 10, type, 0));
    }

    @Test
    void transport_fluvial_jusqu_a_2500_tonnes() {
        assertTrue(peniche.peutPrendreEnCharge(colis(2_500_000, TypeMarchandise.STANDARD), ModeTransport.FLUVIAL));
        assertFalse(peniche.peutPrendreEnCharge(colis(2_500_001, TypeMarchandise.STANDARD), ModeTransport.FLUVIAL));
        assertFalse(peniche.peutPrendreEnCharge(colis(100, TypeMarchandise.STANDARD), ModeTransport.MER));
    }

    @Test
    void pas_de_refrigere() {
        assertFalse(peniche.peutPrendreEnCharge(colis(100, TypeMarchandise.REFRIGERE), ModeTransport.FLUVIAL));
    }

    @Test
    void part_sans_exception() {
        assertDoesNotThrow(() -> peniche.partir(365));
    }
}
