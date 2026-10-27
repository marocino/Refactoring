package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifAirTest {

    private final TarifAir tarif = new TarifAir();

    @Test
    void poids_volumetrique_quand_il_depasse_le_poids_reel() {
        // 1 m × 0,8 m × 0,6 m = 0,48 m³ → 80,16 kg taxables > 50 kg réels
        Colis volumineux = new Colis("volumineux", 50, 100, 80, 60, TypeMarchandise.STANDARD, 0);
        assertEquals(5840 * 0.6 + 0.48 * 167 * 4.5, tarif.prixDeBase(demande(5840, ModeTransport.AIR, volumineux)), 1e-9);
    }

    @Test
    void poids_reel_quand_il_depasse_le_poids_volumetrique() {
        Colis dense = new Colis("dense", 100, 10, 10, 10, TypeMarchandise.STANDARD, 0);
        assertEquals(5840 * 0.6 + 100 * 4.5, tarif.prixDeBase(demande(5840, ModeTransport.AIR, dense)), 1e-9);
    }
}
