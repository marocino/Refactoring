package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TarifRouteTest {

    private final TarifRoute tarif = new TarifRoute();

    @Test
    void un_euro_vingt_par_km_et_dix_centimes_par_kg() {
        assertEquals(225 * 1.2 + 10 * 0.10, tarif.prixDeBase(demande(225, ModeTransport.ROUTE, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void le_poids_de_tous_les_colis_est_additionne() {
        assertEquals(100 * 1.2 + 30 * 0.10, tarif.prixDeBase(demande(100, ModeTransport.ROUTE,
                colis(10, TypeMarchandise.STANDARD), colis(20, TypeMarchandise.FRAGILE))), 1e-9);
    }
}
