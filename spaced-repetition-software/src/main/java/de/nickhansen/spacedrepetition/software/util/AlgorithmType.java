package de.nickhansen.spacedrepetition.software.util;

/**
 * Enum für die verschiedenen Typen von Algorithmen
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public enum AlgorithmType {

    LEITNER_SYSTEM("leitner", 2),

    SUPERMEMO_2("sm2", 6),

    FREE_SPACED_REPETITION_SCHEDULER("fsrs", 4);

    /**
     * Konstruktor für das Enum
     * @param databaseTable der Name der Datenbanktabelle
     * @param ratingButtons die Anzahl an Bewertungsbuttons
     */
    AlgorithmType(String databaseTable, int ratingButtons) {
        this.databaseTable = databaseTable;
        this.ratingButtons = ratingButtons;
    }

    private String databaseTable;
    private int ratingButtons;

    /**
     * Erhalten des Datenbanktabellennamens für den Algorithmus
     * @return der Datenbanktabellenname
     */
    public String getDatabaseTable() {
        return this.databaseTable;
    }

    /**
     * Erhalten der Anzahl an Bewertungsbuttons für den Algorithmus
     * @return die Anzahl an Bewertungsbuttons
     */
    public int getRatingButtons() {
        return this.ratingButtons;
    }
}
