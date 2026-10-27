package fr.efrei.transfret.notification;

/**
 * Trace d'une notification envoyée.
 *
 * @param destinataire adresse email ou numéro de téléphone
 * @param objet        sujet de l'email, ou texte du SMS
 */
public record Envoi(String destinataire, String objet) {
}
