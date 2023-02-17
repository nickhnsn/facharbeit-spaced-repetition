package de.nickhansen.spacedrepetition.api.algorithm.fsrs;

/**
 * Enum für die einzelnen Antworten (Ratings) auf einen Lerninhalt
 */
public enum FSRSRating {

    AGAIN(0), // vergessen; falsche Antwort
    HARD(1), // erinnert; richtige Antwort mit gewisser Schwierigkeit abgerufen
    GOOD(2), // richtige Antwort nach Zögerung
    EASY(3); // perfekte Antwort

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
