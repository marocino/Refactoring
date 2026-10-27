package fr.efrei.transfret;

import fr.efrei.transfret.client.ServiceClient;
import fr.efrei.transfret.client.ValidateurClient;
import fr.efrei.transfret.expedition.EstimateurDelai;
import fr.efrei.transfret.expedition.GenerateurFacture;
import fr.efrei.transfret.expedition.GenerateurIdentifiant;
import fr.efrei.transfret.expedition.ServiceExpedition;
import fr.efrei.transfret.expedition.TableDistances;
import fr.efrei.transfret.expedition.ValidateurExpedition;
import fr.efrei.transfret.notification.EmailConsole;
import fr.efrei.transfret.notification.Notificateur;
import fr.efrei.transfret.notification.SmsConsole;
import fr.efrei.transfret.persistance.ClientRepositoryEnMemoire;
import fr.efrei.transfret.persistance.ExpeditionRepositoryEnMemoire;
import fr.efrei.transfret.tarif.CalculateurPrix;
import fr.efrei.transfret.vehicule.AffectationVehicule;
import fr.efrei.transfret.vehicule.Avion;
import fr.efrei.transfret.vehicule.Bateau;
import fr.efrei.transfret.vehicule.Camion;
import fr.efrei.transfret.vehicule.CamionFrigorifique;
import fr.efrei.transfret.vehicule.Flotte;
import fr.efrei.transfret.vehicule.Train;

import java.time.Clock;
import java.util.List;
import java.util.Random;

/**
 * Racine de composition : le seul endroit qui construit le graphe d'objets de l'application.
 * Chaque instance est une application neuve (flotte, clients, expéditions et journaux vides).
 */
public class TransFret {

    private final EmailConsole emails = new EmailConsole();
    private final SmsConsole sms = new SmsConsole();
    private final ServiceClient serviceClient;
    private final ServiceExpedition serviceExpedition;
    private final GenerateurFacture generateurFacture;

    public TransFret() {
        this(Clock.systemDefaultZone());
    }

    public TransFret(Clock horloge) {
        Notificateur notificateur = new Notificateur(emails, sms);
        serviceClient = new ServiceClient(new ValidateurClient(), new ClientRepositoryEnMemoire(), notificateur);
        serviceExpedition = new ServiceExpedition(
                new ValidateurExpedition(),
                tableDistances(),
                new AffectationVehicule(flotte()),
                new CalculateurPrix(),
                new EstimateurDelai(),
                new GenerateurIdentifiant(new Random()),
                new ExpeditionRepositoryEnMemoire(),
                notificateur,
                horloge);
        generateurFacture = new GenerateurFacture(horloge);
    }

    private static Flotte flotte() {
        return new Flotte(List.of(
                new Camion("AB-123-CD", 19000, false),
                new Camion("EF-456-GH", 12000, true),
                new CamionFrigorifique("IJ-789-KL", 8000),
                new Train("TR-0001", 600000),
                new Bateau("CMA-CGM-01", 20000000, true),
                new Avion("F-GTXA", 100000)));
    }

    private static TableDistances tableDistances() {
        return new TableDistances()
                .ajouter("Paris", "Lyon", 465)
                .ajouter("Paris", "Marseille", 775)
                .ajouter("Paris", "Lille", 225)
                .ajouter("Paris", "Bordeaux", 585)
                .ajouter("Lyon", "Marseille", 315)
                .ajouter("Le Havre", "New York", 5850)
                .ajouter("Marseille", "Alger", 760)
                .ajouter("Paris", "New York", 5840);
    }

    public ServiceClient serviceClient() {
        return serviceClient;
    }

    public ServiceExpedition serviceExpedition() {
        return serviceExpedition;
    }

    public GenerateurFacture generateurFacture() {
        return generateurFacture;
    }

    public EmailConsole emails() {
        return emails;
    }

    public SmsConsole sms() {
        return sms;
    }
}
