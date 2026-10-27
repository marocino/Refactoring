package fr.efrei.transfret.notification;

import fr.efrei.transfret.modele.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Envoie les SMS sur la console et garde une trace des envois (facturés par l'opérateur).
 */
public class SmsConsole implements CanalNotification {

    private static final int LONGUEUR_MAX = 160;
    private static final String POINTS_DE_SUSPENSION = "...";

    private final List<Envoi> envois = new ArrayList<>();

    @Override
    public void envoyer(Client destinataire, Message message) {
        String texte = tronquer(message.texteCourt());
        System.out.println("[SMS] -> " + destinataire.getTelephone() + " : " + texte);
        envois.add(new Envoi(destinataire.getTelephone(), texte));
    }

    public List<Envoi> envois() {
        return List.copyOf(envois);
    }

    private static String tronquer(String texte) {
        if (texte.length() <= LONGUEUR_MAX) {
            return texte;
        }
        return texte.substring(0, LONGUEUR_MAX - POINTS_DE_SUSPENSION.length()) + POINTS_DE_SUSPENSION;
    }
}
