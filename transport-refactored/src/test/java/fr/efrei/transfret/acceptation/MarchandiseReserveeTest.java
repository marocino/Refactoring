package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MarchandiseReserveeTest {

    private final MarchandiseReservee regle = new MarchandiseReservee(TypeMarchandise.REFRIGERE,
            Set.of(ModeTransport.ROUTE, ModeTransport.MER));

    private static List<Colis> refrigere() {
        return List.of(new Colis("yaourts", 10, 10, 10, 10, TypeMarchandise.REFRIGERE, 0));
    }

    @Test
    void accepte_sur_les_modes_autorises() {
        assertTrue(regle.verifier(refrigere(), ModeTransport.ROUTE, false).isEmpty());
        assertTrue(regle.verifier(refrigere(), ModeTransport.MER, false).isEmpty());
    }

    @Test
    void refuse_sur_les_autres_modes() {
        assertTrue(regle.verifier(refrigere(), ModeTransport.RAIL, false).isPresent());
        assertTrue(regle.verifier(refrigere(), ModeTransport.AIR, false).isPresent());
    }

    @Test
    void ne_concerne_pas_les_autres_marchandises() {
        List<Colis> standard = List.of(new Colis("livres", 10, 10, 10, 10, TypeMarchandise.STANDARD, 0));
        assertTrue(regle.verifier(standard, ModeTransport.AIR, false).isEmpty());
    }
}
