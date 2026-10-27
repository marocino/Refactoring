package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Un type de marchandise n'est accepté que sur certains modes de transport.
 */
public class MarchandiseReservee implements RegleAcceptation {

    private final TypeMarchandise type;
    private final Set<ModeTransport> modesAutorises;

    public MarchandiseReservee(TypeMarchandise type, Set<ModeTransport> modesAutorises) {
        this.type = type;
        this.modesAutorises = Set.copyOf(modesAutorises);
    }

    @Override
    public Optional<String> verifier(List<Colis> colis, ModeTransport mode, boolean urgent) {
        if (!modesAutorises.contains(mode) && colis.stream().anyMatch(c -> c.estDeType(type))) {
            return Optional.of("marchandise " + type.getLibelle() + " uniquement en " + modesAutorises);
        }
        return Optional.empty();
    }
}
