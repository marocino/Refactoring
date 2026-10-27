package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * L'option urgente n'existe pas pour certains modes de transport.
 */
public class UrgenceInterdite implements RegleAcceptation {

    private final Set<ModeTransport> modesSansUrgence;

    public UrgenceInterdite(Set<ModeTransport> modesSansUrgence) {
        this.modesSansUrgence = Set.copyOf(modesSansUrgence);
    }

    @Override
    public Optional<String> verifier(List<Colis> colis, ModeTransport mode, boolean urgent) {
        if (urgent && modesSansUrgence.contains(mode)) {
            return Optional.of("pas d'option urgente en " + mode);
        }
        return Optional.empty();
    }
}
