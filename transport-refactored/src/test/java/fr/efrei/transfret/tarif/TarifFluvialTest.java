package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifFluvialTest {

    private final TarifFluvial tarif = new TarifFluvial();

    @Test
    void quarante_centimes_par_km_et_deux_centimes_par_kg() {
        assertEquals(365 * 0.40 + 50000 * 0.02, tarif.prixDeBase(demande(365, ModeTransport.FLUVIAL, colis(50000, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void minimum_de_300_euros() {
        assertEquals(300, tarif.prixDeBase(demande(365, ModeTransport.FLUVIAL, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }
}
