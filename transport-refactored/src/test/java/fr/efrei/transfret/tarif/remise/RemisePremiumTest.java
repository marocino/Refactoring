package fr.efrei.transfret.tarif.remise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemisePremiumTest {

    @Test
    void vingt_pourcent() {
        assertEquals(800, new RemisePremium().appliquer(1000), 1e-9);
        assertEquals(8000, new RemisePremium().appliquer(10000), 1e-9);
    }
}
