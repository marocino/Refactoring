package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.ModeTransport;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UrgenceInterditeTest {

    private final UrgenceInterdite regle = new UrgenceInterdite(Set.of(ModeTransport.MER));

    @Test
    void refuse_l_urgence_sur_un_mode_sans_urgence() {
        assertTrue(regle.verifier(List.of(), ModeTransport.MER, true).isPresent());
    }

    @Test
    void accepte_sans_urgence() {
        assertTrue(regle.verifier(List.of(), ModeTransport.MER, false).isEmpty());
    }

    @Test
    void accepte_l_urgence_sur_les_autres_modes() {
        assertTrue(regle.verifier(List.of(), ModeTransport.ROUTE, true).isEmpty());
    }
}
