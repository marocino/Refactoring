package fr.efrei.transfret.notification;

import fr.efrei.transfret.modele.Client;

public interface CanalNotification {

    void envoyer(Client destinataire, Message message);
}
