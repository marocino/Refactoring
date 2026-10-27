package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MarchandiseInterditeTest {

    private final MarchandiseInterdite regle = new MarchandiseInterdite(TypeMarchandise.DANGEREUX, Set.of(ModeTransport.AIR));

    private static Colis colis(TypeMarchandise type) {
        return new Colis("colis", 10, 10, 10, 10, type, 0);
    }

    @Test
    void refuse_la_marchandise_sur_un_mode_interdit() {
        assertTrue(regle.verifier(List.of(colis(TypeMarchandise.STANDARD), colis(TypeMarchandise.DANGEREUX)), ModeTransport.AIR, false).isPresent());
    }

    @Test
    void accepte_la_marchandise_sur_les_autres_modes() {
        assertTrue(regle.verifier(List.of(colis(TypeMarchandise.DANGEREUX)), ModeTransport.ROUTE, false).isEmpty());
    }

    @Test
    void accepte_les_autres_marchandises() {
        assertTrue(regle.verifier(List.of(colis(TypeMarchandise.STANDARD)), ModeTransport.AIR, false).isEmpty());
    }
}
