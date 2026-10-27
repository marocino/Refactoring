package fr.efrei.transfret.tarif.supplement;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.tarif.ComposantPrix;
import fr.efrei.transfret.tarif.DecorateurPrix;
import fr.efrei.transfret.tarif.DemandeTarif;

/**
 * +10 % si au moins un colis dépasse 2 m sur l'une de ses dimensions.
 */
public class SupplementHorsGabarit extends DecorateurPrix {

    private static final double COEFFICIENT = 1.10;
    private static final double DIMENSION_MAX_CM = 200;

    public SupplementHorsGabarit(ComposantPrix enveloppe) {
        super(enveloppe);
    }

    @Override
    protected double ajuster(double prix, DemandeTarif demande) {
        return demande.colis().stream().anyMatch(SupplementHorsGabarit::horsGabarit) ? prix * COEFFICIENT : prix;
    }

    private static boolean horsGabarit(Colis colis) {
        return Math.max(colis.getLongueur(), Math.max(colis.getLargeur(), colis.getHauteur())) > DIMENSION_MAX_CM;
    }
}
