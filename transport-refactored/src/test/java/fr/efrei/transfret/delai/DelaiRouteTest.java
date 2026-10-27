package fr.efrei.transfret.delai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiRouteTest {

    private final DelaiRoute delai = new DelaiRoute();

    @Test
    void neuf_heures_de_conduite_a_70_km_h_par_jour() {
        assertEquals(1, delai.jours(465, false));
        assertEquals(2, delai.jours(775, false));
    }

    @Test
    void dix_huit_heures_de_conduite_par_jour_en_urgence() {
        assertEquals(1, delai.jours(775, true));
    }
}
