package fr.efrei.transfret.notification;

/**
 * Contenu d'une notification, décliné pour chaque canal.
 *
 * @param sujet      objet de l'email
 * @param corps      corps de l'email
 * @param texteCourt texte du SMS
 */
public record Message(String sujet, String corps, String texteCourt) {
}
