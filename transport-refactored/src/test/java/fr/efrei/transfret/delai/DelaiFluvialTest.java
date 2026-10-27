package fr.efrei.transfret.delai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelaiFluvialTest {

    @Test
    void trois_cent_soixante_km_par_jour_plus_un_jour() {
        assertEquals(2, new DelaiFluvial().jours(360, false));
        assertEquals(3, new DelaiFluvial().jours(365, false));
    }
}
