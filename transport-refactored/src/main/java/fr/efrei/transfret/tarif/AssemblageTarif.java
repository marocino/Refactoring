package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.tarif.remise.PolitiqueRemise;
import fr.efrei.transfret.tarif.remise.Remise;
import fr.efrei.transfret.tarif.supplement.Assurance;
import fr.efrei.transfret.tarif.supplement.SupplementUrgence;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * Assemble la chaîne de calcul du prix, de l'intérieur vers l'extérieur :
 * <pre>Assurance( Remise( Urgence( supplément n( … supplément 1( tarif du mode ) ) ) ) )</pre>
 * L'ordre compte : la remise s'applique au prix après suppléments et urgence, l'assurance après la remise.
 * Chaque supplément ne s'applique que si l'expédition est concernée.
 */
public final class AssemblageTarif {

    private AssemblageTarif() {
    }

    /**
     * @param supplements les suppléments marchandise, dans leur ordre d'application
     *                    (par exemple {@code SupplementFragile::new})
     */
    public static ComposantPrix chaine(Map<ModeTransport, TarifTransport> tarifs,
                                       List<UnaryOperator<ComposantPrix>> supplements,
                                       Map<TypeClient, PolitiqueRemise> remises) {
        ComposantPrix prix = new TarifParMode(tarifs);
        for (UnaryOperator<ComposantPrix> supplement : supplements) {
            prix = supplement.apply(prix);
        }
        prix = new SupplementUrgence(prix);
        prix = new Remise(prix, remises);
        prix = new Assurance(prix);
        return prix;
    }
}
