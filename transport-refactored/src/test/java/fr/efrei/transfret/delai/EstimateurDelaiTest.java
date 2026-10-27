package fr.efrei.transfret.delai;

import fr.efrei.transfret.modele.ModeTransport;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstimateurDelaiTest {

    @Test
    void delegue_a_la_strategie_du_mode() {
        EstimateurDelai estimateur = new EstimateurDelai(Map.of(ModeTransport.ROUTE, (distance, urgent) -> 7));
        assertEquals(7, estimateur.estimerJours(100, ModeTransport.ROUTE, false));
    }

    @Test
    void au_minimum_un_jour() {
        EstimateurDelai estimateur = new EstimateurDelai(Map.of(ModeTransport.ROUTE, (distance, urgent) -> 0));
        assertEquals(1, estimateur.estimerJours(0, ModeTransport.ROUTE, false));
    }

    @Test
    void mode_sans_strategie_refuse() {
        EstimateurDelai estimateur = new EstimateurDelai(Map.of());
        assertThrows(IllegalArgumentException.class, () -> estimateur.estimerJours(100, ModeTransport.AIR, false));
    }
}
