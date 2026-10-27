package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplementHorsGabaritTest {

    private final SupplementHorsGabarit supplement = new SupplementHorsGabarit(MILLE_EUROS);

    private static Colis colis(double longueur, double largeur, double hauteur) {
        return new Colis("colis", 10, longueur, largeur, hauteur, TypeMarchandise.STANDARD, 0);
    }

    private double prix(Colis... colis) {
        return supplement.calculer(demande(false, false, TypeClient.PARTICULIER, colis));
    }

    @Test
    void dix_pourcent_si_un_colis_depasse_2_m_sur_une_dimension() {
        assertEquals(1100, prix(colis(201, 50, 50)), 1e-9);
        assertEquals(1100, prix(colis(50, 201, 50)), 1e-9);
        assertEquals(1100, prix(colis(50, 50, 201)), 1e-9);
    }

    @Test
    void une_seule_fois_meme_avec_plusieurs_colis_hors_gabarit() {
        assertEquals(1100, prix(colis(300, 50, 50), colis(250, 250, 250)), 1e-9);
    }

    @Test
    void sans_effet_jusqu_a_2_m_inclus() {
        assertEquals(1000, prix(colis(200, 200, 200), colis(10, 10, 10)), 1e-9);
    }
}
