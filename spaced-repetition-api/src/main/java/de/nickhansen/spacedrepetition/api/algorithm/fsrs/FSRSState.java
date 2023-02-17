package de.nickhansen.spacedrepetition.api.algorithm.fsrs;

/**
 * Enum für den Status eines Lerninhalts
 */
public enum FSRSState {

    NEW(0), // noch nie gelernt
    LEARNING(1), // vor kurzem zum ersten Mal gelernt
    REVIEW(2), // beim Abschließen des LEARNING States
    RELEARNING(3); // vergessen im REVIEW State

    /**
     * Konstruktor für die Enum
     * @param id die ID des States (0-3)
     */
    FSRSState(int id) {
        this.id = id;
    }

    private int id;

    /**
     * State als Integer erhalten
     * @return die ID des States
     */
    public int toInt() {
        return this.id;
    }
}
