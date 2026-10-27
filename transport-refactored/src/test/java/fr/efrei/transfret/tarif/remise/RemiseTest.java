package fr.efrei.transfret.tarif.remise;

import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemiseTest {

    private final Remise remise = new Remise(MILLE_EUROS, Map.of(
            TypeClient.PARTICULIER, new AucuneRemise(),
            TypeClient.PREMIUM, prix -> prix / 2));

    @Test
    void delegue_a_la_politique_du_type_de_client() {
        assertEquals(500, remise.calculer(demande(false, false, TypeClient.PREMIUM, colis(10, TypeMarchandise.STANDARD))), 1e-9);
        assertEquals(1000, remise.calculer(demande(false, false, TypeClient.PARTICULIER, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void type_de_client_sans_politique_refuse() {
        assertThrows(IllegalArgumentException.class,
                () -> remise.calculer(demande(false, false, TypeClient.ENTREPRISE, colis(10, TypeMarchandise.STANDARD))));
    }
}
