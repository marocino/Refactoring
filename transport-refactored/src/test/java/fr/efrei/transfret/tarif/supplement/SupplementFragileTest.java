package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplementFragileTest {

    private final SupplementFragile supplement = new SupplementFragile(MILLE_EUROS);

    @Test
    void quinze_pourcent_si_un_colis_est_fragile() {
        assertEquals(1150, supplement.calculer(demande(TypeMarchandise.STANDARD, TypeMarchandise.FRAGILE)), 1e-9);
    }

    @Test
    void une_seule_fois_meme_avec_plusieurs_colis_fragiles() {
        assertEquals(1150, supplement.calculer(demande(TypeMarchandise.FRAGILE, TypeMarchandise.FRAGILE)), 1e-9);
    }

    @Test
    void sans_effet_sinon() {
        assertEquals(1000, supplement.calculer(demande(TypeMarchandise.STANDARD, TypeMarchandise.DANGEREUX)), 1e-9);
    }
}
