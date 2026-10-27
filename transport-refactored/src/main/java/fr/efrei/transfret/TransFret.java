package fr.efrei.transfret;

import fr.efrei.transfret.acceptation.MarchandiseInterdite;
import fr.efrei.transfret.acceptation.MarchandiseReservee;
import fr.efrei.transfret.acceptation.RegleAcceptation;
import fr.efrei.transfret.acceptation.UrgenceInterdite;
import fr.efrei.transfret.acceptation.ValidateurExpedition;
import fr.efrei.transfret.client.ServiceClient;
import fr.efrei.transfret.client.ValidateurClient;
import fr.efrei.transfret.delai.DelaiAir;
import fr.efrei.transfret.delai.DelaiMer;
import fr.efrei.transfret.delai.DelaiRail;
import fr.efrei.transfret.delai.DelaiRoute;
import fr.efrei.transfret.delai.DelaiTransport;
import fr.efrei.transfret.delai.EstimateurDelai;
import fr.efrei.transfret.expedition.GenerateurFacture;
import fr.efrei.transfret.expedition.GenerateurIdentifiant;
import fr.efrei.transfret.expedition.ServiceExpedition;
import fr.efrei.transfret.expedition.TableDistances;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.notification.CanalNotification;
import fr.efrei.transfret.notification.EmailConsole;
import fr.efrei.transfret.notification.Notificateur;
import fr.efrei.transfret.notification.SmsConsole;
import fr.efrei.transfret.persistance.ClientRepositoryEnMemoire;
import fr.efrei.transfret.persistance.ExpeditionRepositoryEnMemoire;
import fr.efrei.transfret.tarif.AssemblageTarif;
import fr.efrei.transfret.tarif.CalculateurPrix;
import fr.efrei.transfret.tarif.TarifAir;
import fr.efrei.transfret.tarif.TarifMer;
import fr.efrei.transfret.tarif.TarifRail;
import fr.efrei.transfret.tarif.TarifRoute;
import fr.efrei.transfret.tarif.TarifTransport;
import fr.efrei.transfret.tarif.remise.AucuneRemise;
import fr.efrei.transfret.tarif.remise.PolitiqueRemise;
import fr.efrei.transfret.tarif.remise.RemiseEntreprise;
import fr.efrei.transfret.tarif.remise.RemisePremium;
import fr.efrei.transfret.vehicule.AffectationVehicule;
import fr.efrei.transfret.vehicule.Avion;
import fr.efrei.transfret.vehicule.Bateau;
import fr.efrei.transfret.vehicule.Camion;
import fr.efrei.transfret.vehicule.CamionFrigorifique;
import fr.efrei.transfret.vehicule.Flotte;
import fr.efrei.transfret.vehicule.Train;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
        Notificateur notificateur = new Notificateur(emails, canauxParTypeClient());
        serviceClient = new ServiceClient(new ValidateurClient(), new ClientRepositoryEnMemoire(), notificateur);
        serviceExpedition = new ServiceExpedition(
                new ValidateurExpedition(reglesAcceptation()),
                tableDistances(),
                new AffectationVehicule(flotte()),
                new CalculateurPrix(AssemblageTarif.chaine(tarifs(), remises())),
                new EstimateurDelai(delais()),
                new GenerateurIdentifiant(new Random()),
                new ExpeditionRepositoryEnMemoire(),
                notificateur,
                horloge);
        generateurFacture = new GenerateurFacture(horloge);
    }

    private Map<TypeClient, List<CanalNotification>> canauxParTypeClient() {
        return Map.of(
                TypeClient.PARTICULIER, List.of(emails),
                TypeClient.ENTREPRISE, List.of(emails),
                TypeClient.PREMIUM, List.of(emails, sms));
    }

    private static List<RegleAcceptation> reglesAcceptation() {
        return List.of(
                new MarchandiseInterdite(TypeMarchandise.DANGEREUX, Set.of(ModeTransport.AIR)),
                new MarchandiseReservee(TypeMarchandise.REFRIGERE, Set.of(ModeTransport.ROUTE, ModeTransport.MER)),
                new UrgenceInterdite(Set.of(ModeTransport.MER)));
    }

    private static Map<ModeTransport, TarifTransport> tarifs() {
        return Map.of(
                ModeTransport.ROUTE, new TarifRoute(),
                ModeTransport.RAIL, new TarifRail(),
                ModeTransport.MER, new TarifMer(),
                ModeTransport.AIR, new TarifAir());
    }

    private static Map<TypeClient, PolitiqueRemise> remises() {
        return Map.of(
                TypeClient.PARTICULIER, new AucuneRemise(),
                TypeClient.ENTREPRISE, new RemiseEntreprise(),
                TypeClient.PREMIUM, new RemisePremium());
    }

    private static Map<ModeTransport, DelaiTransport> delais() {
        return Map.of(
                ModeTransport.ROUTE, new DelaiRoute(),
                ModeTransport.RAIL, new DelaiRail(),
                ModeTransport.MER, new DelaiMer(),
                ModeTransport.AIR, new DelaiAir());
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
