package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifMerTest {

    private final TarifMer tarif = new TarifMer();

    @Test
    void cinquante_centimes_par_km_et_trois_centimes_par_kg() {
        assertEquals(5850 * 0.5 + 15000 * 0.03, tarif.prixDeBase(demande(5850, ModeTransport.MER, colis(15000, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void minimum_de_500_euros() {
        assertEquals(500, tarif.prixDeBase(demande(760, ModeTransport.MER, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }
}
