package de.nickhansen.spacedrepetition.api.algorithm.result;

import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSState;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.SchedulingCard;

/**
 * Berechnete Rückgaben für den Free Spaced Repetition Scheduler Algorithmus
 */
public class FSRSAlgorithmResult extends AlgorithmResult {

    private long dueTime, lastReview;
    private float stability, difficulty;
    private int elapsedDays, repetitions;
    private FSRSState state;

    /**
     * Konstruktor für das Ergebnis des Free Spaced Repetition Scheduler Algorithmus
     * @param card die Karte mit den neu berechneten Attributen
     */
    public FSRSAlgorithmResult(SchedulingCard card) {
        super(card.getScheduledDays());
        this.dueTime = card.getDueTime();
        this.lastReview = card.getLastReview();
        this.stability = card.getStability();
        this.difficulty = card.getDifficulty();
        this.elapsedDays = card.getElapsedDays();
        this.repetitions = card.getRepetitions();
        this.state = card.getState();
    }

    /**
     * Berechnung des Zeitpunkts der nächsten notwendigen Wiederholung in Millisekunden
     * @return Zeitpunkt in Millisekunden, an welchem die nächste Wiederholung stattfinden soll
     */
    @Override
    public long getNextRepetitionTime() {
        return this.dueTime;
    }

    /**
     * Erhalten der Stabilität
     * @return die Stabilität des Lerninhalts
     */
    public float getStability() {
        return this.stability;
    }

    /**
     * Erhalten der Schwierigkeit
     * @return die Schwierigkeit des Lerninhalts
     */
    public float getDifficulty() {
        return this.difficulty;
    }

    /**
     * Erhalten der ausgesetzten Tage zwischen letzter und nächster Wiederholung
     * @return die Anzahl an ausgesetzten Tagen
     */
    public int getElapsedDays() {
        return this.elapsedDays;
    }

    /**
     * Erhalten der bisherigen Anzahl an Wiederholungen
     * @return die Anzahl an Wiederholungen
     */
    public int getRepetitions() {
        return this.repetitions;
    }

    /**
     * Erhalten des letzten Abfragezeitpunkts des Lerninhalts
     * @return der Abfragezeitpunkt in Millisekunden
     */
    public long getLastReview() {
        return lastReview;
    }

    /**
     * Erhalten des Status des Lerninhalts
     * @return der Status
     */
    public FSRSState getState() {
        return this.state;
    }
}
