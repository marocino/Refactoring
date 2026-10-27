package fr.efrei.transfret.client;

import fr.efrei.transfret.modele.Client;

import java.util.Optional;

public interface ClientRepository {

    void sauvegarder(Client client);

    Optional<Client> parEmail(String email);
}
