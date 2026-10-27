package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.tarif.remise.PolitiqueRemise;
import fr.efrei.transfret.tarif.remise.Remise;
import fr.efrei.transfret.tarif.supplement.Assurance;
import fr.efrei.transfret.tarif.supplement.SupplementDangereux;
import fr.efrei.transfret.tarif.supplement.SupplementFragile;
import fr.efrei.transfret.tarif.supplement.SupplementRefrigere;
import fr.efrei.transfret.tarif.supplement.SupplementUrgence;

import java.util.Map;

/**
 * Assemble la chaîne de calcul du prix, de l'intérieur vers l'extérieur :
 * <pre>Assurance( Remise( Urgence( Réfrigéré( Dangereux( Fragile( tarif du mode ) ) ) ) ) )</pre>
 * L'ordre compte : la remise s'applique au prix après suppléments et urgence, l'assurance après la remise.
 * Chaque supplément ne s'applique que si l'expédition est concernée.
 */
public final class AssemblageTarif {

    private AssemblageTarif() {
    }

    public static ComposantPrix chaine(Map<ModeTransport, TarifTransport> tarifs, Map<TypeClient, PolitiqueRemise> remises) {
        ComposantPrix prix = new TarifParMode(tarifs);
        prix = new SupplementFragile(prix);
        prix = new SupplementDangereux(prix);
        prix = new SupplementRefrigere(prix);
        prix = new SupplementUrgence(prix);
        prix = new Remise(prix, remises);
        prix = new Assurance(prix);
        return prix;
    }
}
