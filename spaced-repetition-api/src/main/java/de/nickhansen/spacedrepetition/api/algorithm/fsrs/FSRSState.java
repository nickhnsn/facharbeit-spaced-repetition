package de.nickhansen.spacedrepetition.api.algorithm.fsrs;

/**
 * Enum für den Status eines Lerninhalts
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public enum FSRSState {

    /**
     * noch nie gelernt
     */
    NEW(0),
    /**
     * vor kurzem zum ersten Mal gelernt
     */
    LEARNING(1),
    /**
     * beim Abschließen des LEARNING States
     */
    REVIEW(2),
    /**
     * vergessen im REVIEW State
     */
    RELEARNING(3);

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
