package fr.efrei.transfret.acceptation;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;

import java.util.List;
import java.util.Optional;

/**
 * Stratégie : une règle métier qui peut refuser une expédition selon ses colis et son mode de transport.
 */
public interface RegleAcceptation {

    /** @return le motif du refus, ou vide si la règle est respectée */
    Optional<String> verifier(List<Colis> colis, ModeTransport mode, boolean urgent);
}
