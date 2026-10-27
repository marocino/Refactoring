package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;

import java.util.List;
import java.util.Optional;

/**
 * Vérifie qu'une demande d'expédition est complète et respecte les règles d'acceptation.
 */
public class ValidateurExpedition {

    private final List<RegleAcceptation> regles;

    public ValidateurExpedition(List<RegleAcceptation> regles) {
        this.regles = List.copyOf(regles);
    }

    /** @return le motif du refus, ou vide si la demande est acceptable */
    public Optional<String> verifier(Client client, List<Colis> colis, ModeTransport mode, boolean urgent) {
        if (client == null) {
            return Optional.of("client obligatoire");
        }
        if (colis == null || colis.isEmpty()) {
            return Optional.of("au moins un colis est nécessaire");
        }
        for (Colis c : colis) {
            if (c.getPoids() <= 0) {
                return Optional.of("poids invalide pour " + c.getDescription());
            }
        }
        return regles.stream()
                .map(regle -> regle.verifier(colis, mode, urgent))
                .flatMap(Optional::stream)
                .findFirst();
    }
}
