package fr.efrei.transfret.tarif;

import fr.efrei.transfret.modele.Colis;
import fr.efrei.transfret.modele.ModeTransport;
import fr.efrei.transfret.modele.TypeClient;
import fr.efrei.transfret.modele.TypeMarchandise;

import java.util.List;

/**
 * Tout ce qui influe sur le prix d'un transport, pour une expédition comme pour un devis.
 */
public record DemandeTarif(List<Colis> colis, int distance, ModeTransport mode, boolean urgent, boolean assurance,
                           TypeClient typeClient) {

    public DemandeTarif {
        colis = List.copyOf(colis);
    }

    public double poidsTotal() {
        double total = 0;
        for (Colis c : colis) {
            total = total + c.getPoids();
        }
        return total;
    }

    /** en m³ */
    public double volumeTotal() {
        double total = 0;
        for (Colis c : colis) {
            total = total + c.getVolume();
        }
        return total;
    }

    public double valeurTotale() {
        double total = 0;
        for (Colis c : colis) {
            total = total + c.getValeurDeclaree();
        }
        return total;
    }

    public boolean contient(TypeMarchandise type) {
        return colis.stream().anyMatch(c -> c.estDeType(type));
    }
}
