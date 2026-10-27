package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.tarif.remise.AucuneRemise;
import fr.efrei.transfret.tarif.remise.RemiseEntreprise;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static fr.efrei.transfret.tarif.Demandes.colis;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblageTarifTest {

    private final ComposantPrix chaine = AssemblageTarif.chaine(
            Map.of(ModeTransport.ROUTE, demande -> 4000.0),
            Map.of(TypeClient.PARTICULIER, new AucuneRemise(), TypeClient.ENTREPRISE, new RemiseEntreprise()));

    @Test
    void les_supplements_se_cumulent() {
        // 4000 × 1,15 × 1,30 = 5980
        assertEquals(5980, chaine.calculer(demande(TypeMarchandise.FRAGILE, TypeMarchandise.DANGEREUX)), 1e-9);
    }

    @Test
    void la_remise_porte_sur_le_prix_apres_urgence() {
        // 4000 × 1,5 = 6000 > 5000 → 15 % de remise : 5100 (et non 4000 × 0,90 × 1,5 = 5400)
        assertEquals(5100, chaine.calculer(demande(true, false, TypeClient.ENTREPRISE, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }

    @Test
    void l_assurance_s_ajoute_apres_la_remise() {
        // 4000 × 0,90 + 15 = 3615 (et non (4000 + 15) × 0,90)
        assertEquals(3615, chaine.calculer(demande(false, true, TypeClient.ENTREPRISE, colis(10, TypeMarchandise.STANDARD))), 1e-9);
    }
}
