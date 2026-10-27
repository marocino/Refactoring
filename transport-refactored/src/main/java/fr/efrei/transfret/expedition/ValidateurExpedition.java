package fr.efrei.transfret.expedition;

import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;
import java.util.Optional;

/**
 * Vérifie qu'une demande d'expédition respecte les règles d'acceptation.
 */
public class ValidateurExpedition {

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
        boolean dangereux = contient(colis, TypeMarchandise.DANGEREUX);
        boolean refrigere = contient(colis, TypeMarchandise.REFRIGERE);
        if (dangereux && mode == ModeTransport.AIR) {
            return Optional.of("les matières dangereuses sont interdites en transport aérien");
        }
        if (refrigere && !(mode == ModeTransport.ROUTE || mode == ModeTransport.MER)) {
            return Optional.of("le transport réfrigéré n'est possible que par route ou par mer");
        }
        if (urgent && mode == ModeTransport.MER) {
            return Optional.of("pas d'option urgente pour le transport maritime");
        }
        return Optional.empty();
    }

    private static boolean contient(List<Colis> colis, TypeMarchandise type) {
        return colis.stream().anyMatch(c -> c.estDeType(type));
    }
}
