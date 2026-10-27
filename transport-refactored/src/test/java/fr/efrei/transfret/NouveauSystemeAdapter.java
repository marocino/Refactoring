package fr.efrei.transfret;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.notification.Envoi;
import fr.efrei.transport.contrat.Canal;
import fr.efrei.transport.contrat.ColisDto;
import fr.efrei.transport.contrat.DemandeClient;
import fr.efrei.transport.contrat.DemandeDevis;
import fr.efrei.transport.contrat.DemandeExpedition;
import fr.efrei.transport.contrat.EtatExpedition;
import fr.efrei.transport.contrat.Notification;
import fr.efrei.transport.contrat.Statut;
import fr.efrei.transport.contrat.SystemeTransport;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Branche le nouveau code sur les tests de contrat : ne fait que traduire les types du contrat.
 */
public class NouveauSystemeAdapter implements SystemeTransport {

    private TransFret application;

    @Override
    public void reinitialiser() {
        application = TransFret.avecGrilleHistorique();
    }

    @Override
    public Optional<String> creerClient(DemandeClient d) {
        return application.serviceClient()
                .creerClient(d.nom(), d.email(), d.telephone(), d.adresse(), versTypeClient(d.type()), d.siret())
                .map(client -> client.getEmail());
    }

    @Override
    public Optional<String> creerExpedition(DemandeExpedition d) {
        return application.serviceClient().trouver(d.refClient())
                .flatMap(client -> application.serviceExpedition().creerExpedition(client, versColis(d.colis()),
                        d.villeDepart(), d.villeArrivee(), versMode(d.mode()), d.urgent(), d.assurance()))
                .map(Expedition::getId);
    }

    @Override
    public Optional<EtatExpedition> consulter(String id) {
        return application.serviceExpedition().consulter(id).map(NouveauSystemeAdapter::versEtat);
    }

    @Override
    public OptionalDouble devisTTC(DemandeDevis d) {
        return application.serviceExpedition().calculerDevis(versColis(d.colis()), d.villeDepart(), d.villeArrivee(),
                versMode(d.mode()), d.urgent(), d.assurance(), versTypeClient(d.typeClient()));
    }

    @Override
    public boolean demarrer(String id) {
        return application.serviceExpedition().demarrerExpedition(id);
    }

    @Override
    public boolean livrer(String id) {
        return application.serviceExpedition().livrerExpedition(id);
    }

    @Override
    public boolean annuler(String id) {
        return application.serviceExpedition().annulerExpedition(id);
    }

    @Override
    public Optional<String> facture(String id) {
        return application.serviceExpedition().consulter(id).map(application.generateurFacture()::generer);
    }

    @Override
    public List<Notification> notifications() {
        List<Notification> notifications = new ArrayList<>();
        for (Envoi email : application.emails().envois()) {
            notifications.add(new Notification(Canal.EMAIL, email.destinataire(), email.objet()));
        }
        for (Envoi sms : application.sms().envois()) {
            notifications.add(new Notification(Canal.SMS, sms.destinataire(), sms.objet()));
        }
        return notifications;
    }

    private static EtatExpedition versEtat(Expedition e) {
        int delai = (int) ChronoUnit.DAYS.between(e.getDateCreation(), e.getDateLivraisonPrevue());
        return new EtatExpedition(e.getId(), Statut.valueOf(e.getStatut().name()), e.getPrixHT(), e.getPrixTTC(),
                delai, e.getVehicule().getImmatriculation(), e.getFraisAnnulation());
    }

    private static List<Colis> versColis(List<ColisDto> colis) {
        return colis.stream().map(NouveauSystemeAdapter::versColis).toList();
    }

    private static Colis versColis(ColisDto c) {
        return new Colis(c.description(), c.poidsKg(), c.longueurCm(), c.largeurCm(), c.hauteurCm(),
                TypeMarchandise.valueOf(c.type().name()), c.valeurDeclaree());
    }

    private static ModeTransport versMode(fr.efrei.transport.contrat.ModeTransport mode) {
        return ModeTransport.valueOf(mode.name());
    }

    private static TypeClient versTypeClient(fr.efrei.transport.contrat.TypeClient type) {
        return TypeClient.valueOf(type.name());
    }
}
