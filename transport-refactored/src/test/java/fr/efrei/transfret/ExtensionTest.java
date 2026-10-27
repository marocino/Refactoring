package fr.efrei.transfret;

import fr.efrei.transfret.expedition.ServiceExpedition;
import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Étape 8 : le mode FLUVIAL et le supplément hors gabarit, de bout en bout, avec la grille tarifaire en vigueur.
 */
class ExtensionTest {

    private final TransFret application = new TransFret();
    private final ServiceExpedition expeditions = application.serviceExpedition();
    private final Client dupont = application.serviceClient()
            .creerClient("Jean Dupont", "jean.dupont@mail.fr", "0601020304", "Paris", TypeClient.PARTICULIER, null)
            .orElseThrow();

    private static List<Colis> colis(double poids, double longueur, TypeMarchandise type) {
        return List.of(new Colis("colis", poids, longueur, 40, 30, type, 100));
    }

    @Test
    void expedition_fluviale_paris_le_havre() {
        Expedition e = expeditions.creerExpedition(dupont, colis(10, 50, TypeMarchandise.STANDARD),
                "Paris", "Le Havre", ModeTransport.FLUVIAL, false, false).orElseThrow();
        assertEquals(300.00, e.getPrixHT(), 0.001);   // 365 × 0,40 + 10 × 0,02 = 146,20 → minimum 300 €
        assertEquals(360.00, e.getPrixTTC(), 0.001);
        assertEquals(3, ChronoUnit.DAYS.between(e.getDateCreation(), e.getDateLivraisonPrevue()));
        assertEquals("PEN-0001", e.getVehicule().getImmatriculation());
    }

    @Test
    void fluvial_au_dela_du_minimum() {
        // 365 × 0,40 + 20 000 × 0,02 = 546 €
        assertEquals(546.00, expeditions.creerExpedition(dupont, colis(20000, 50, TypeMarchandise.STANDARD),
                "Le Havre", "Paris", ModeTransport.FLUVIAL, false, false).orElseThrow().getPrixHT(), 0.001);
    }

    @Test
    void fluvial_sans_refrigere_ni_urgence() {
        assertTrue(expeditions.creerExpedition(dupont, colis(10, 50, TypeMarchandise.REFRIGERE),
                "Paris", "Le Havre", ModeTransport.FLUVIAL, false, false).isEmpty());
        assertTrue(expeditions.creerExpedition(dupont, colis(10, 50, TypeMarchandise.STANDARD),
                "Paris", "Le Havre", ModeTransport.FLUVIAL, true, false).isEmpty());
    }

    @Test
    void supplement_hors_gabarit() {
        // Paris → Lille : 225 × 1,2 + 10 × 0,10 = 271 € ; colis de 250 cm → × 1,10 = 298,10 €
        assertEquals(298.10, expeditions.creerExpedition(dupont, colis(10, 250, TypeMarchandise.STANDARD),
                "Paris", "Lille", ModeTransport.ROUTE, false, false).orElseThrow().getPrixHT(), 0.001);
        assertEquals(271.00, expeditions.creerExpedition(dupont, colis(10, 200, TypeMarchandise.STANDARD),
                "Paris", "Lille", ModeTransport.ROUTE, false, false).orElseThrow().getPrixHT(), 0.001);
    }

    @Test
    void le_hors_gabarit_s_applique_avant_l_urgence_et_la_remise() {
        Client transco = application.serviceClient()
                .creerClient("Transco SA", "contact@transco.fr", "01", "Villeneuve", TypeClient.ENTREPRISE, "12345678900012")
                .orElseThrow();
        List<Colis> futs = List.of(new Colis("Fûts de solvant", 5000, 240, 120, 150, TypeMarchandise.DANGEREUX, 20000));
        // (775 × 1,2 + 5000 × 0,10) × 1,30 × 1,10 × 1,5 = 3067,37 € ≤ 5000 → remise de 10 % : 2760,62 €
        assertEquals(2760.62, expeditions.creerExpedition(transco, futs, "Paris", "Marseille", ModeTransport.ROUTE, true, false)
                .orElseThrow().getPrixHT(), 0.001);
    }
}
