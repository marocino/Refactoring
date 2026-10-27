package fr.efrei.transfret.notification;

import fr.efrei.transfret.commun.Dates;
import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.TypeClient;

import java.util.List;
import java.util.Map;

/**
 * Prévient le client des événements de son compte et de ses expéditions.
 */
public class Notificateur {

    private final CanalNotification canalBienvenue;
    private final Map<TypeClient, List<CanalNotification>> canauxParType;

    public Notificateur(CanalNotification canalBienvenue, Map<TypeClient, List<CanalNotification>> canauxParType) {
        this.canalBienvenue = canalBienvenue;
        this.canauxParType = Map.copyOf(canauxParType);
    }

    public void bienvenue(Client client) {
        canalBienvenue.envoyer(client, new Message("Bienvenue chez TransFret",
                "Bonjour " + client.getNom() + ", votre compte " + client.getType().name().toLowerCase() + " a été créé.",
                null));
    }

    public void expeditionCreee(Expedition e) {
        notifier(e.getClient(), new Message("Confirmation de votre expédition " + e.getId(),
                "Votre expédition " + e.getVilleDepart() + " -> " + e.getVilleArrivee() + " (" + e.getMode() + ") a été enregistrée. Montant : " + e.getPrixTTC() + " € TTC.",
                "TransFret : expédition " + e.getId() + " créée, livraison prévue le " + Dates.formater(e.getDateLivraisonPrevue())));
    }

    public void expeditionPartie(Expedition e) {
        notifier(e.getClient(), new Message("Votre expédition " + e.getId() + " est en route",
                "Votre expédition a quitté " + e.getVilleDepart() + ".",
                "TransFret : expédition " + e.getId() + " partie de " + e.getVilleDepart()));
    }

    public void expeditionLivree(Expedition e) {
        notifier(e.getClient(), new Message("Votre expédition " + e.getId() + " est livrée",
                "Votre expédition est arrivée à " + e.getVilleArrivee() + ". Merci de votre confiance.",
                "TransFret : expédition " + e.getId() + " livrée à " + e.getVilleArrivee()));
    }

    public void expeditionAnnulee(Expedition e) {
        notifier(e.getClient(), new Message("Annulation de votre expédition " + e.getId(),
                "Votre expédition a été annulée. Frais d'annulation : " + e.getFraisAnnulation() + " €",
                "TransFret : expédition " + e.getId() + " annulée"));
    }

    private void notifier(Client client, Message message) {
        for (CanalNotification canal : canauxParType.getOrDefault(client.getType(), List.of())) {
            canal.envoyer(client, message);
        }
    }
}
