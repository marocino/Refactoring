package fr.efrei.transfret.delai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiAirTest {

    private final DelaiAir delai = new DelaiAir();

    @Test
    void un_jour_jusqu_a_3000_km() {
        assertEquals(1, delai.jours(465, false));
        assertEquals(1, delai.jours(3000, false));
    }

    @Test
    void deux_jours_au_dela_de_3000_km() {
        assertEquals(2, delai.jours(5840, false));
    }

    @Test
    void un_jour_en_urgence() {
        assertEquals(1, delai.jours(5840, true));
    }
}
