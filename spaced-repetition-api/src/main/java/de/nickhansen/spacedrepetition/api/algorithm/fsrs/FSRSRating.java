package de.nickhansen.spacedrepetition.api.algorithm.fsrs;

/**
 * Enum für die einzelnen Antworten (Ratings) auf einen Lerninhalt
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public enum FSRSRating {

    /**
     * vergessen; falsche Antwort
     */
    AGAIN(0),
    /**
     * erinnert; richtige Antwort mit gewisser Schwierigkeit abgerufen
     */
    HARD(1),
    /**
     * richtige Antwort nach Zögerung
     */
    GOOD(2),
    /**
     * perfekte Antwort
     */
    EASY(3);

    /**
     * Konstruktor für die Enum
     * @param id die ID des Ratings (0-3)
     */
     FSRSRating(int id) {
        this.id = id;
    }

    private int id;

    /**
     * Rating als Integer erhalten
     * @return die ID des Ratings
     */
    public int toInt() {
        return this.id;
    }
}
