package fr.efrei.transfret.tarif.remise;

public class AucuneRemise implements PolitiqueRemise {

    @Override
    public double appliquer(double prix) {
        return prix;
    }
}
