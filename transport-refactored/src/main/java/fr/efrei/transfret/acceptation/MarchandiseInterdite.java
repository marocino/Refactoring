package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Un type de marchandise est interdit sur certains modes de transport.
 */
public class MarchandiseInterdite implements RegleAcceptation {

    private final TypeMarchandise type;
    private final Set<ModeTransport> modesInterdits;

    public MarchandiseInterdite(TypeMarchandise type, Set<ModeTransport> modesInterdits) {
        this.type = type;
        this.modesInterdits = Set.copyOf(modesInterdits);
    }

    @Override
    public Optional<String> verifier(List<Colis> colis, ModeTransport mode, boolean urgent) {
        if (modesInterdits.contains(mode) && colis.stream().anyMatch(c -> c.estDeType(type))) {
            return Optional.of("marchandise " + type.getLibelle() + " interdite en " + mode);
        }
        return Optional.empty();
    }
}
