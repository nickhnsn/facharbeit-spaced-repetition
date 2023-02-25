package de.nickhansen.spacedrepetition.api.algorithm.fsrs;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Modell der SchedulingCard
 * implementiert nach https://github.com/open-spaced-repetition/py-fsrs/blob/2f178564e805c5bfdf8501d8ef516723187266dd/src/fsrs/models.py
 *                    (MIT LICENSE, Copyright (c) 2022 Open Spaced Repetition).
 */
public class SchedulingCard {

    private long dueTime, lastReview;
    private float stability, difficulty;
    private int elapsedDays, scheduledDays, repetitions;
    private FSRSState state;

    private HashMap<FSRSRating, SchedulingCard> ratingToCard;

    /**
     * Konstruktor für die SchedulingCard
     * @param dueTime die nächste fällige Wiederholung bzw. nächster Wiederholungszeitpunkt
     * @param stability die Stabilität des Lerninhalts
     * @param difficulty die Schwierigkeit des Lerninhalts
     * @param elapsedDays die ausgesetzten Tage zwischen letzter und nächster Wiederholung
     * @param scheduledDays die berechneten Tage zur nächsten Wiederholung
     * @param repetitions die bisherige Anzahl an Wiederholungen
     * @param state der Status des Lerninhalts
     * @param lastReview der letzte Zeitpunkt des Abfragens des Lerninhalts
     */
    public SchedulingCard(long dueTime, float stability, float difficulty, int elapsedDays, int scheduledDays, int repetitions, FSRSState state, long lastReview) {
        this.dueTime = dueTime;
        this.stability = stability;
        this.difficulty = difficulty;
        this.elapsedDays = elapsedDays;
        this.scheduledDays = scheduledDays;
        this.repetitions = repetitions;
        this.state = state;
        this.lastReview = lastReview;

        this.ratingToCard = new HashMap<>();
    }

    /**
     * Aktualisieren der States für die Karten je nach Rating
     */
    public void updateState() {
        if (this.state == FSRSState.NEW) {
            this.ratingToCard.get(FSRSRating.AGAIN).setState(FSRSState.LEARNING);
            this.ratingToCard.get(FSRSRating.HARD).setState(FSRSState.LEARNING);
            this.ratingToCard.get(FSRSRating.GOOD).setState(FSRSState.LEARNING);
            this.ratingToCard.get(FSRSRating.EASY).setState(FSRSState.REVIEW);

        } else if(this.state == FSRSState.LEARNING || this.state == FSRSState.RELEARNING) {
            this.ratingToCard.get(FSRSRating.AGAIN).setState(this.state);
            this.ratingToCard.get(FSRSRating.HARD).setState(FSRSState.REVIEW);
            this.ratingToCard.get(FSRSRating.GOOD).setState(FSRSState.REVIEW);
            this.ratingToCard.get(FSRSRating.EASY).setState(FSRSState.REVIEW);

        } else if(this.state == FSRSState.REVIEW) {
            this.ratingToCard.get(FSRSRating.AGAIN).setState(FSRSState.LEARNING);
            this.ratingToCard.get(FSRSRating.HARD).setState(FSRSState.REVIEW);
            this.ratingToCard.get(FSRSRating.GOOD).setState(FSRSState.REVIEW);
            this.ratingToCard.get(FSRSRating.EASY).setState(FSRSState.REVIEW);
        }
    }

    /**
     * Berechnungen für die nächste Wiederholung des Inhalts
     * @param hardInterval das Intervall für das HARD Rating
     * @param goodInterval das Intervall für das GOOD Rating
     * @param easyInterval das Intervall für das EASY Rating
     */
    public void schedule(float hardInterval, float goodInterval, float easyInterval) {
        this.scheduledDays = 0;

        this.ratingToCard.get(FSRSRating.HARD).setScheduledDays(Math.round(hardInterval));
        this.ratingToCard.get(FSRSRating.GOOD).setScheduledDays(Math.round(goodInterval));
        this.ratingToCard.get(FSRSRating.EASY).setScheduledDays(Math.round(easyInterval));

        this.ratingToCard.get(FSRSRating.AGAIN).setDueTime(this.dueTime + TimeUnit.MINUTES.toMillis(5));
        this.ratingToCard.get(FSRSRating.HARD).setDueTime(this.dueTime + TimeUnit.DAYS.toMillis((long) hardInterval));
        this.ratingToCard.get(FSRSRating.GOOD).setDueTime(this.dueTime + TimeUnit.DAYS.toMillis((long) goodInterval));
        this.ratingToCard.get(FSRSRating.EASY).setDueTime(this.dueTime + TimeUnit.DAYS.toMillis((long) easyInterval));
    }

    /**
     * Erhalten des nächsten Wiederholungszeitpunkts
     * @return der Wiederholungszeitpunkt in Millisekunden
     */
    public long getDueTime() {
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
     * Erhalten der berechneten Tage zur nächsten Wiederholung
     * @return die Anzahl an Tagen
     */
    public int getScheduledDays() {
        return this.scheduledDays;
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
        return this.lastReview;
    }

    /**
     * Erhalten des Status des Lerninhalts
     * @return der Status
     */
    public FSRSState getState() {
        return this.state;
    }

    /**
     * Erhalten der HashMap in der einem Rating je eine Karte zugeordnet ist
     * @return die Hashmap in der einem Rating je eine Karte zugeordnet ist
     */
    public HashMap<FSRSRating, SchedulingCard> getRatingToCard() {
        return this.ratingToCard;
    }

    /**
     * Setzen des nächsten Wiederholungszeitpunkts
     * @param dueTime
     */
    public void setDueTime(long dueTime) {
        this.dueTime = dueTime;
    }

    /**
     * Setzen der Stabilität
     * @param stability die Stabilität des Lerninhalts
     */
    public void setStability(float stability) {
        this.stability = stability;
    }

    /**
     * Setzen der Schwierigkeit
     * @param difficulty die Schwierigkeit des Lerninhalts
     */
    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Setzen der ausgesetzten Tage zwischen letzter und nächster Wiederholung
     * @param elapsedDays die Anzahl an ausgesetzten Tagen
     */
    public void setElapsedDays(int elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    /**
     * Setzen der Tage zur nächsten Wiederholung
     * @param scheduledDays die Anzahl an Tagen
     */
    public void setScheduledDays(int scheduledDays) {
        this.scheduledDays = scheduledDays;
    }

    /**
     * Setzen der bisherigen Anzahl an Wiederholungen
     * @param repetitions die Anzahl an Wiederholungen
     */
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * Setzen des letzten Abfragezeitpunkts des Lerninhalts
     * @param lastReview der Abfragezeitpunkt in Millisekunden
     */
    public void setLastReview(long lastReview) {
        this.lastReview = lastReview;
    }

    /**
     * Setzen des Status des Lerninhalts
     * @param state der Status
     */
    public void setState(FSRSState state) {
        this.state = state;
    }
}
