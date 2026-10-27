package fr.efrei.transfret.delai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiRailTest {

    private final DelaiRail delai = new DelaiRail();

    @Test
    void mille_quatre_cent_quarante_km_par_jour_plus_un_jour() {
        assertEquals(2, delai.jours(465, false));
        assertEquals(3, delai.jours(1441, false));
    }

    @Test
    void un_jour_de_moins_en_urgence() {
        assertEquals(1, delai.jours(465, true));
    }
}
