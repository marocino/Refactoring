package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifRailTest {

    private final TarifRail tarif = new TarifRail();

    @Test
    void quatre_vingts_centimes_par_km_et_sept_centimes_par_kg() {
        assertEquals(465 * 0.8 + 200000 * 0.07, tarif.prixDeBase(demande(465, ModeTransport.RAIL, colis(200000, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void minimum_de_150_euros() {
        assertEquals(150, tarif.prixDeBase(demande(10, ModeTransport.RAIL, colis(1, TypeMarchandise.STANDARD))), 1e-9);
    }
}
