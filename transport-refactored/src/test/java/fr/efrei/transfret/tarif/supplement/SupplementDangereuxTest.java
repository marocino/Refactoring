package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplementDangereuxTest {

    private final SupplementDangereux supplement = new SupplementDangereux(MILLE_EUROS);

    @Test
    void trente_pourcent_si_un_colis_est_dangereux() {
        assertEquals(1300, supplement.calculer(demande(TypeMarchandise.STANDARD, TypeMarchandise.DANGEREUX)), 1e-9);
    }

    @Test
    void sans_effet_sinon() {
        assertEquals(1000, supplement.calculer(demande(TypeMarchandise.FRAGILE)), 1e-9);
    }
}
