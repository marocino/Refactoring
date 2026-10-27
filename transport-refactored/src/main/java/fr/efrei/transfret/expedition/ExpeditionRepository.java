package fr.efrei.transfret.expedition;

import fr.efrei.transfret.modele.Expedition;

import java.util.Optional;

public interface ExpeditionRepository {

    void sauvegarder(Expedition expedition);

    void mettreAJour(Expedition expedition);

    Optional<Expedition> parId(String id);
}
