package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplementUrgenceTest {

    private final SupplementUrgence urgence = new SupplementUrgence(MILLE_EUROS);

    @Test
    void cinquante_pourcent_si_urgent() {
        assertEquals(1500, urgence.calculer(demande(true, false, TypeClient.PARTICULIER, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void sans_effet_sinon() {
        assertEquals(1000, urgence.calculer(demande(false, false, TypeClient.PARTICULIER, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }
}
