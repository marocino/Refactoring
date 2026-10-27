package fr.efrei.transfret.tarif.remise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemiseEntrepriseTest {

    private final RemiseEntreprise remise = new RemiseEntreprise();

    @Test
    void dix_pourcent_jusqu_a_5000_euros() {
        assertEquals(900, remise.appliquer(1000), 1e-9);
        assertEquals(4500, remise.appliquer(5000), 1e-9);
    }

    @Test
    void quinze_pourcent_au_dela_de_5000_euros() {
        assertEquals(5100, remise.appliquer(6000), 1e-9);
    }
}
