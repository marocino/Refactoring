package fr.efrei.transfret.expedition;

import java.util.Random;

/**
 * Attribue un identifiant unique à chaque expédition : EXP-{numéro d'ordre}-{suffixe aléatoire}.
 */
public class GenerateurIdentifiant {

    private static final int PREMIER_SUFFIXE = 1000;
    private static final int NOMBRE_SUFFIXES = 9000;

    private final Random aleatoire;
    private int compteur;

    public GenerateurIdentifiant(Random aleatoire) {
        this.aleatoire = aleatoire;
    }

    public String prochain() {
        compteur++;
        return "EXP-" + compteur + "-" + (PREMIER_SUFFIXE + aleatoire.nextInt(NOMBRE_SUFFIXES));
    }
}
