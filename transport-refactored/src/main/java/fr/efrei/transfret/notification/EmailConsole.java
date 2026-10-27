package fr.efrei.transfret.notification;

import fr.efrei.transfret.modele.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Envoie les emails sur la console et garde une trace des envois.
 */
public class EmailConsole implements CanalNotification {

    private static final String EXPEDITEUR = "noreply@transfret.fr";

    private final List<Envoi> envois = new ArrayList<>();

    @Override
    public void envoyer(Client destinataire, Message message) {
        String adresse = destinataire.getEmail();
        if (adresse == null || !adresse.contains("@")) {
            return;
        }
        System.out.println("[EMAIL] " + EXPEDITEUR + " -> " + adresse + " | " + message.sujet());
        System.out.println("        " + message.corps());
        envois.add(new Envoi(adresse, message.sujet()));
    }

    public List<Envoi> envois() {
        return List.copyOf(envois);
    }
}
