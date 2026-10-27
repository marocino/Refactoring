package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.MILLE_EUROS;
import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssuranceTest {

    private final Assurance assurance = new Assurance(MILLE_EUROS);

    private static Colis colisDeValeur(double valeur) {
        return new Colis("colis", 10, 10, 10, 10, TypeMarchandise.STANDARD, valeur);
    }

    @Test
    void deux_pourcent_de_la_valeur_declaree_totale() {
        assertEquals(1060, assurance.calculer(demande(false, true, TypeClient.PARTICULIER,
                colisDeValeur(1000), colisDeValeur(2000))), 1e-9);
    }

    @Test
    void au_minimum_15_euros() {
        assertEquals(1015, assurance.calculer(demande(false, true, TypeClient.PARTICULIER, colisDeValeur(100))), 1e-9);
    }

    @Test
    void sans_effet_si_non_souscrite() {
        assertEquals(1000, assurance.calculer(demande(false, false, TypeClient.PARTICULIER, colisDeValeur(100000))), 1e-9);
    }
}
