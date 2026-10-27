package fr.efrei.transfret.persistance;

import fr.efrei.transfret.expedition.ExpeditionRepository;
import fr.efrei.transfret.modele.Expedition;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExpeditionRepositoryEnMemoire implements ExpeditionRepository {

    private final Map<String, Expedition> expeditions = new HashMap<>();

    @Override
    public void sauvegarder(Expedition expedition) {
        expeditions.put(expedition.getId(), expedition);
    }

    @Override
    public void mettreAJour(Expedition expedition) {
        expeditions.put(expedition.getId(), expedition);
    }

    @Override
    public Optional<Expedition> parId(String id) {
        return Optional.ofNullable(expeditions.get(id));
    }
}
