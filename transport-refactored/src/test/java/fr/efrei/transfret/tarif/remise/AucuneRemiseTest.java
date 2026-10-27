package fr.efrei.transfret.tarif.remise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AucuneRemiseTest {

    @Test
    void le_prix_est_inchange() {
        assertEquals(1234.56, new AucuneRemise().appliquer(1234.56), 1e-9);
    }
}
