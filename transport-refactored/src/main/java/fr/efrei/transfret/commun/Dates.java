package fr.efrei.transfret.commun;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Format d'affichage des dates (jj/mm/aaaa).
 */
public final class Dates {

    private static final DateTimeFormatter JJ_MM_AAAA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Dates() {
    }

    public static String formater(Date date) {
        if (date == null) {
            return "";
        }
        return JJ_MM_AAAA.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
}
