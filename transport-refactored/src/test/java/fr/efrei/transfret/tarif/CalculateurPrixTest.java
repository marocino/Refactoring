package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.TypeMarchandise;
import org.junit.jupiter.api.Test;

import static fr.efrei.transfret.tarif.Demandes.demande;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateurPrixTest {

    @Test
    void arrondit_le_resultat_de_la_chaine_au_centime() {
        CalculateurPrix calculateur = new CalculateurPrix(demande -> 669.105001);
        assertEquals(669.11, calculateur.calculerPrixHT(demande(TypeMarchandise.STANDARD)), 1e-9);
    }
}
