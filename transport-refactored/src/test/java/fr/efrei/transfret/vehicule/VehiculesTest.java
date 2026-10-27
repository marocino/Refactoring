package fr.efrei.transfret.vehicule;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehiculesTest {

    private static List<Colis> colis(TypeMarchandise... types) {
        return java.util.Arrays.stream(types).map(type -> new Colis("colis", 100, 10, 10, 10, type, 0)).toList();
    }

    @Nested
    class Communs {

        private final Camion camion = new Camion("AB-123-CD", 1000, false);

        @Test
        void refuse_un_autre_mode() {
            assertFalse(camion.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.RAIL));
        }

        @Test
        void refuse_au_dela_de_la_capacite_restante() {
            camion.charger(new Colis("lourd", 950, 10, 10, 10, TypeMarchandise.STANDARD, 0));
            assertFalse(camion.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.ROUTE));
        }

        @Test
        void refuse_s_il_est_immobilise_puis_accepte_une_fois_libere() {
            camion.immobiliser();
            assertFalse(camion.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.ROUTE));
            camion.liberer();
            assertTrue(camion.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.ROUTE));
        }

        @Test
        void le_dechargement_libere_la_capacite() {
            Colis c = new Colis("colis", 400, 10, 10, 10, TypeMarchandise.STANDARD, 0);
            camion.charger(c);
            assertEquals(400, camion.getChargeActuelle(), 1e-9);
            camion.decharger(c);
            assertEquals(0, camion.getChargeActuelle(), 1e-9);
        }
    }

    @Nested
    class Routier {

        @Test
        void camion_standard_sans_dangereux_ni_refrigere() {
            Camion camion = new Camion("AB-123-CD", 19000, false);
            assertTrue(camion.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD, TypeMarchandise.FRAGILE), ModeTransport.ROUTE));
            assertFalse(camion.peutPrendreEnCharge(colis(TypeMarchandise.DANGEREUX), ModeTransport.ROUTE));
            assertFalse(camion.peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE), ModeTransport.ROUTE));
        }

        @Test
        void camion_certifie_adr_accepte_le_dangereux() {
            assertTrue(new Camion("EF-456-GH", 12000, true).peutPrendreEnCharge(colis(TypeMarchandise.DANGEREUX), ModeTransport.ROUTE));
        }

        @Test
        void camion_frigorifique_uniquement_du_refrigere() {
            CamionFrigorifique frigo = new CamionFrigorifique("IJ-789-KL", 8000);
            assertTrue(frigo.peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE), ModeTransport.ROUTE));
            assertFalse(frigo.peutPrendreEnCharge(colis(TypeMarchandise.STANDARD), ModeTransport.ROUTE));
            assertFalse(frigo.peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE, TypeMarchandise.STANDARD), ModeTransport.ROUTE));
        }

        @Test
        void camion_frigorifique_peut_charger_n_importe_quel_colis_sans_exception() {
            // Liskov : le chargement ne refuse plus rien, le tri se fait à l'affectation
            assertDoesNotThrow(() -> new CamionFrigorifique("IJ-789-KL", 8000).charger(colis(TypeMarchandise.STANDARD).getFirst()));
        }
    }

    @Nested
    class AutresModes {

        @Test
        void train_sans_refrigere() {
            Train train = new Train("TR-0001", 600000);
            assertTrue(train.peutPrendreEnCharge(colis(TypeMarchandise.DANGEREUX), ModeTransport.RAIL));
            assertFalse(train.peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE), ModeTransport.RAIL));
        }

        @Test
        void bateau_frigorifique_accepte_le_refrigere_pas_le_bateau_simple() {
            assertTrue(new BateauFrigorifique("CMA-CGM-01", 20000000).peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE), ModeTransport.MER));
            assertFalse(new Bateau("NAV-01", 20000000).peutPrendreEnCharge(colis(TypeMarchandise.REFRIGERE), ModeTransport.MER));
        }

        @Test
        void avion_sans_dangereux() {
            Avion avion = new Avion("F-GTXA", 100000);
            assertTrue(avion.peutPrendreEnCharge(colis(TypeMarchandise.FRAGILE), ModeTransport.AIR));
            assertFalse(avion.peutPrendreEnCharge(colis(TypeMarchandise.DANGEREUX), ModeTransport.AIR));
        }

        @Test
        void tous_les_vehicules_peuvent_partir_sans_exception() {
            List<Vehicule> flotte = List.of(new Camion("C", 1, false), new CamionFrigorifique("F", 1), new Train("T", 1),
                    new Bateau("B", 1), new BateauFrigorifique("BF", 1), new Avion("A", 1));
            flotte.forEach(v -> assertDoesNotThrow(() -> v.partir(100)));
        }
    }
}
