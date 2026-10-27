package fr.efrei.transfret.persistance;

import fr.efrei.transfret.client.ClientRepository;
import fr.efrei.transfret.modele.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientRepositoryEnMemoire implements ClientRepository {

    private final Map<String, Client> clients = new HashMap<>();

    @Override
    public void sauvegarder(Client client) {
        clients.put(client.getEmail(), client);
    }

    @Override
    public Optional<Client> parEmail(String email) {
        return Optional.ofNullable(clients.get(email));
    }
}
