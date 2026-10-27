package fr.efrei.transfret.client;

import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.notification.Notificateur;

import java.util.Optional;

/**
 * Inscrit les nouveaux clients.
 */
public class ServiceClient {

    private final ValidateurClient validateur;
    private final ClientRepository clients;
    private final Notificateur notificateur;

    public ServiceClient(ValidateurClient validateur, ClientRepository clients, Notificateur notificateur) {
        this.validateur = validateur;
        this.clients = clients;
        this.notificateur = notificateur;
    }

    /** @return le client créé, ou vide si les données sont invalides */
    public Optional<Client> creerClient(String nom, String email, String telephone, String adresse, TypeClient type, String siret) {
        Optional<String> refus = validateur.verifier(nom, email, type, siret);
        if (refus.isPresent()) {
            System.out.println("ERREUR : " + refus.get());
            return Optional.empty();
        }
        Client client = new Client(nom, email, telephone, adresse, type, siret);
        clients.sauvegarder(client);
        notificateur.bienvenue(client);
        return Optional.of(client);
    }

    public Optional<Client> trouver(String email) {
        return clients.parEmail(email);
    }
}
