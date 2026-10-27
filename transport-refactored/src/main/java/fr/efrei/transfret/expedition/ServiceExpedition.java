package fr.efrei.transfret.expedition;

import fr.efrei.transfret.commun.Montants;
import fr.efrei.transfret.modele.Client;
import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.Expedition;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.StatutExpedition;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;
import fr.efrei.transfret.notification.Notificateur;
import fr.efrei.transfret.persistance.Database;
import fr.efrei.transfret.tarif.CalculateurPrix;
import fr.efrei.transfret.vehicule.AffectationVehicule;
import fr.efrei.transfret.vehicule.Vehicule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Random;

/**
 * Pilote le cycle de vie des expéditions : création, départ, livraison, annulation.
 */
public class ServiceExpedition {

    private static final double TAUX_FRAIS_ANNULATION = 0.5;

    private static final double KM_PAR_MILLE_NAUTIQUE = 1.852;
    private static final int ROULAGE_PISTE_KM = 2;
    private static final int ALTITUDE_CROISIERE_PIEDS = 35000;
    private static final double TEMPERATURE_REFRIGERATION = 4;

    private static final int PREMIER_SUFFIXE_ID = 1000;
    private static final int NOMBRE_SUFFIXES_ID = 9000;

    public static int compteur = 0;

    private Database db = Database.getInstance();
    private CalculateurPrix calculateur = new CalculateurPrix();
    private AffectationVehicule affectation = new AffectationVehicule();
    private ValidateurExpedition validateur = new ValidateurExpedition();
    private EstimateurDelai estimateurDelai = new EstimateurDelai();
    private TableDistances distances = new TableDistances();
    private Notificateur notificateur = new Notificateur();

    public Expedition creerExpedition(Client client, List<Colis> colis, String villeDepart, String villeArrivee, ModeTransport mode, boolean urgent, boolean assurance) {
        Optional<String> refus = validateur.verifier(client, colis, mode, urgent);
        if (refus.isPresent()) {
            System.out.println("ERREUR : " + refus.get());
            return null;
        }
        OptionalInt distance = distances.distance(villeDepart, villeArrivee);
        if (distance.isEmpty()) {
            System.out.println("ERREUR : trajet inconnu " + villeDepart + " -> " + villeArrivee);
            return null;
        }

        Vehicule v = affectation.trouverVehicule(colis, mode);
        if (v == null) {
            System.out.println("ERREUR : aucun véhicule disponible pour cette expédition");
            return null;
        }
        for (Colis c : colis) {
            v.charger(c);
        }

        Expedition e = new Expedition();
        compteur++;
        e.setId("EXP-" + compteur + "-" + (PREMIER_SUFFIXE_ID + new Random().nextInt(NOMBRE_SUFFIXES_ID)));
        e.setClient(client);
        e.setColis(colis);
        e.setVilleDepart(villeDepart);
        e.setVilleArrivee(villeArrivee);
        e.setDistance(distance.getAsInt());
        e.setMode(mode);
        e.setUrgent(urgent);
        e.setAssurance(assurance);
        e.setStatut(StatutExpedition.CREEE);
        e.setVehicule(v);
        e.setPrixHT(calculateur.calculerPrixHT(colis, distance.getAsInt(), mode, urgent, assurance, client.getType()));
        e.setPrixTTC(Montants.ttc(e.getPrixHT()));
        e.setDateCreation(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(e.getDateCreation());
        cal.add(Calendar.DAY_OF_MONTH, estimateurDelai.estimerJours(distance.getAsInt(), mode, urgent));
        e.setDateLivraisonPrevue(cal.getTime());

        db.sauvegarderExpedition(e);
        notificateur.expeditionCreee(e);
        return e;
    }

    /** @return le prix TTC, ou vide si le trajet est inconnu */
    public OptionalDouble calculerDevis(List<Colis> colis, String villeDepart, String villeArrivee, ModeTransport mode, boolean urgent, boolean assurance, TypeClient typeClient) {
        OptionalInt distance = distances.distance(villeDepart, villeArrivee);
        if (distance.isEmpty()) {
            return OptionalDouble.empty();
        }
        double ht = calculateur.calculerPrixHT(colis, distance.getAsInt(), mode, urgent, assurance, typeClient);
        return OptionalDouble.of(Montants.ttc(ht));
    }

    public boolean demarrerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() != StatutExpedition.CREEE) {
            System.out.println("ERREUR : impossible de démarrer l'expédition " + id);
            return false;
        }
        e.setStatut(StatutExpedition.EN_COURS);
        Vehicule v = e.getVehicule();
        v.setDisponible(false);
        try {
            v.faireLePlein();
        } catch (Exception ex) {
        }
        switch (e.getMode()) {
            case ROUTE, RAIL -> v.rouler(e.getDistance());
            case MER -> v.naviguer((int) (e.getDistance() / KM_PAR_MILLE_NAUTIQUE));
            case AIR -> {
                v.rouler(ROULAGE_PISTE_KM);
                v.voler(ALTITUDE_CROISIERE_PIEDS);
            }
        }
        for (Colis c : e.getColis()) {
            if (c.estDeType(TypeMarchandise.REFRIGERE)) {
                v.activerRefrigeration(TEMPERATURE_REFRIGERATION);
                break;
            }
        }
        db.mettreAJourStatut(e);
        notificateur.expeditionPartie(e);
        return true;
    }

    public boolean livrerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() != StatutExpedition.EN_COURS) {
            System.out.println("ERREUR : impossible de livrer l'expédition " + id);
            return false;
        }
        e.setStatut(StatutExpedition.LIVREE);
        libererVehicule(e);
        db.mettreAJourStatut(e);
        notificateur.expeditionLivree(e);
        return true;
    }

    public boolean annulerExpedition(String id) {
        Expedition e = db.trouverExpedition(id);
        if (e == null || e.getStatut() == StatutExpedition.LIVREE || e.getStatut() == StatutExpedition.ANNULEE) {
            System.out.println("ERREUR : impossible d'annuler l'expédition " + id);
            return false;
        }
        if (e.getStatut() == StatutExpedition.EN_COURS) {
            // frais si déjà partie
            e.setFraisAnnulation(Montants.arrondir(e.getPrixTTC() * TAUX_FRAIS_ANNULATION));
        }
        e.setStatut(StatutExpedition.ANNULEE);
        libererVehicule(e);
        db.mettreAJourStatut(e);
        notificateur.expeditionAnnulee(e);
        return true;
    }

    private void libererVehicule(Expedition e) {
        for (Colis c : e.getColis()) {
            e.getVehicule().decharger(c);
        }
        e.getVehicule().setDisponible(true);
    }
}
