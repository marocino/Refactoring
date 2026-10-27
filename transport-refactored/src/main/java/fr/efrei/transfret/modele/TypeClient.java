package fr.efrei.transfret.modele;

public enum TypeClient {
    PARTICULIER(false),
    ENTREPRISE(true),
    PREMIUM(false);

    private final boolean avecSiret;

    TypeClient(boolean avecSiret) {
        this.avecSiret = avecSiret;
    }

    /** Le client est identifié par un numéro SIRET, obligatoire à l'inscription et imprimé sur les factures. */
    public boolean avecSiret() {
        return avecSiret;
    }
}
