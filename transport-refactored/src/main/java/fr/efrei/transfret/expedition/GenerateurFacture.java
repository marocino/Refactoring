package fr.efrei.transfret.expedition;

import fr.efrei.transfret.commun.Dates;
import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.StatutExpedition;
import fr.efrei.transfret.modele.TypeClient;

import java.util.Date;

/**
 * Met en forme la facture texte d'une expédition.
 */
public class GenerateurFacture {

    private static final String LIGNE_DOUBLE = "==============================================\n";
    private static final String LIGNE_SIMPLE = "----------------------------------------------\n";

    public String generer(Expedition e) {
        Client client = e.getClient();
        StringBuilder sb = new StringBuilder();
        sb.append(LIGNE_DOUBLE);
        sb.append("              FACTURE TRANSFRET\n");
        sb.append(LIGNE_DOUBLE);
        sb.append("Facture n° F-" + e.getId() + "\n");
        sb.append("Date : " + Dates.formater(new Date()) + "\n");
        sb.append("Client : " + client.getNom() + "\n");
        if (client.getType() == TypeClient.ENTREPRISE) {
            sb.append("SIRET : " + client.getSiret() + "\n");
        }
        sb.append("Adresse : " + client.getAdresse() + "\n");
        sb.append(LIGNE_SIMPLE);
        sb.append("Trajet : " + e.getVilleDepart() + " -> " + e.getVilleArrivee() + " (" + e.getDistance() + " km)\n");
        sb.append("Mode : " + e.getMode() + (e.isUrgent() ? " (URGENT)" : "") + "\n");
        sb.append("Colis :\n");
        for (Colis c : e.getColis()) {
            sb.append("  - " + c.getDescription() + " : " + c.getPoids() + " kg [" + c.getLibelleType() + "]\n");
        }
        sb.append(LIGNE_SIMPLE);
        if (e.getStatut() == StatutExpedition.ANNULEE) {
            sb.append("EXPEDITION ANNULEE\n");
            sb.append("Frais d'annulation TTC : " + montant(e.getFraisAnnulation()) + " €\n");
        } else {
            sb.append("Total HT  : " + montant(e.getPrixHT()) + " €\n");
            sb.append("TVA 20%   : " + montant(e.getPrixTTC() - e.getPrixHT()) + " €\n");
            sb.append("Total TTC : " + montant(e.getPrixTTC()) + " €\n");
        }
        sb.append(LIGNE_DOUBLE);
        return sb.toString();
    }

    private static String montant(double valeur) {
        return String.format("%.2f", valeur);
    }
}
