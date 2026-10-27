package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplementRefrigereTest {

    private final SupplementRefrigere supplement = new SupplementRefrigere(MILLE_EUROS);

    @Test
    void vingt_pourcent_si_un_colis_est_refrigere() {
        assertEquals(1200, supplement.calculer(demande(TypeMarchandise.REFRIGERE)), 1e-9);
    }

    @Test
    void sans_effet_sinon() {
        assertEquals(1000, supplement.calculer(demande(TypeMarchandise.STANDARD)), 1e-9);
    }
}
