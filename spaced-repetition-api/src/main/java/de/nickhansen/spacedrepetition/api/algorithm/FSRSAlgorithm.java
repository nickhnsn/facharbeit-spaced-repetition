package de.nickhansen.spacedrepetition.api.algorithm;

import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSRating;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSState;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.SchedulingCard;
import de.nickhansen.spacedrepetition.api.algorithm.result.FSRSAlgorithmResult;

import java.util.concurrent.TimeUnit;

/**
 * Implementation des Free Spaced Repetition Scheduler Algorithmus
 * siehe https://github.com/open-spaced-repetition/fsrs4anki/wiki/Free-Spaced-Repetition-Scheduler
 * implementiert nach https://github.com/open-spaced-repetition/py-fsrs/blob/2f178564e805c5bfdf8501d8ef516723187266dd/src/fsrs/fsrs
 *       (MIT LICENSE, Copyright (c) 2022 Open Spaced Repetition)
 */
public class FSRSAlgorithm implements Algorithm {

    // Standardmäßige Werte für den Algorithmus
    private final float REQUEST_RETENTION = 0.9F;
    private final int MAXIMUM_INTERVAL = 36500;
    private final float EASY_BONUS = 1.3F;
    private final float HARD_FACTOR = 1.2F;
    private final float[] WEIGHTS = new float[]{1F, 1F, 5F, -1F, -1F, 0.1F, 1.5F, -0.2F, 0.8F, 2, -0.2F, 0.2F, 1F};

    private FSRSRating rating;
    private SchedulingCard card;
    private long lastReview;
    private float stability, difficulty;
    private int elapsedDays, scheduledDays, repetitions;
    private FSRSState state;

    /**
     * Builder mit Fluent Interface (statische Klasse FSRSAlgorithmBuilder)
     * Objekterzeugung erfolgt mithilfe eines Builders für den FSRSAlgorithm
     * @return Builder für den Free Spaced Repetition Scheduler Algorithmus
     */
    public static FSRSAlgorithmBuilder builder() {
        return new FSRSAlgorithmBuilder();
    }

    /**
     * Konstruktor für den Free Spaced Repetition Scheduler Algorithmus, der den Builder nutzt.
     * Durch den Builder sind alle nötigen Parameter bestimmt worden.
     * @param builder Builder für den Free Spaced Repetition Scheduler Algorithmus
     */
    public FSRSAlgorithm(FSRSAlgorithmBuilder builder) {
        this.rating = builder.rating;
        this.lastReview = builder.lastReview;
        this.stability = builder.stability;
        this.difficulty = builder.difficulty;
        this.elapsedDays = builder.elapsedDays;
        this.scheduledDays = builder.scheduledDays;
        this.repetitions = builder.repetitions;
        this.state = builder.state;
    }

    /**
     * Der Free Spaced Repetition Scheduler Algorithmus.
     * Berechnet aus den Inputs rating, lastReview, stability, difficulty, elapsedDays, scheduledDays, repetitions und state
     * die Outputs dueTime, lastReview, stability, difficulty, elapsedDays, scheduledDays, repetitions und state
     * @return Free Spaced Repetition Scheduler Algorithmus mit den berechneten Rückgabewerten
     */
    @Override
    public FSRSAlgorithmResult calc() {
        this.card = new SchedulingCard(System.currentTimeMillis(), this.stability, this.difficulty, this.elapsedDays, this.scheduledDays, this.repetitions, this.state, this.lastReview);
        // Für jedes Ranking-Enum eine Karte mit Standard-Parametern erstellen
        for (FSRSRating rating : FSRSRating.values()) {
            this.card.getRatingToCard().put(rating, new SchedulingCard(System.currentTimeMillis(), 0, 0, 0, 0, 0,  FSRSState.NEW, 0));
        }

        if (this.card.getState() == FSRSState.NEW) {
            this.card.setElapsedDays(0);
        } else {
            this.card.setElapsedDays((int) (TimeUnit.MILLISECONDS.toDays(this.card.getDueTime() - this.card.getLastReview())));
        }

        this.card.updateState();

        int easyInterval, hardInterval, goodInterval;

        // Für neue Karten
        if (this.card.getState() == FSRSState.NEW) {
            this.init();

            this.card.getRatingToCard().get(FSRSRating.AGAIN).setDueTime(this.card.getDueTime() + TimeUnit.MINUTES.toMillis(1));
            this.card.getRatingToCard().get(FSRSRating.HARD).setDueTime(this.card.getDueTime() + TimeUnit.MINUTES.toMillis(15));
            this.card.getRatingToCard().get(FSRSRating.GOOD).setDueTime(this.card.getDueTime() + TimeUnit.MINUTES.toMillis(10));

            easyInterval = this.nextInterval(this.card.getRatingToCard().get(FSRSRating.EASY).getStability() * EASY_BONUS);
            this.card.getRatingToCard().get(FSRSRating.EASY).setScheduledDays(easyInterval);
            this.card.getRatingToCard().get(FSRSRating.EASY).setDueTime(this.card.getDueTime() + TimeUnit.DAYS.toMillis(easyInterval));

        // Für Karten, die (neu-)gelernt werden
        } else if (this.card.getState()  == FSRSState.LEARNING || this.card.getState()  == FSRSState.RELEARNING) {
            hardInterval = this.nextInterval(this.card.getRatingToCard().get(FSRSRating.HARD).getStability());
            goodInterval = Math.max(this.nextInterval(this.card.getRatingToCard().get(FSRSRating.GOOD).getStability()), hardInterval + 1);
            easyInterval = Math.max(this.nextInterval(this.card.getRatingToCard().get(FSRSRating.EASY).getStability() * EASY_BONUS), goodInterval + 1);
            this.card.schedule(hardInterval, goodInterval, easyInterval);

        // Für zu abrufende Karten
        } else if (this.card.getState()  == FSRSState.REVIEW) {
            int interval = this.card.getElapsedDays();
            float lastDifficulty = this.card.getDifficulty();
            float lastStability = this.card.getStability();

            float retrievability = (float) Math.exp(Math.log(0.9) * interval / lastStability);
            this.next(lastDifficulty, lastStability, retrievability);

            hardInterval = this.nextInterval(lastStability * HARD_FACTOR);
            goodInterval = nextInterval(this.card.getRatingToCard().get(FSRSRating.GOOD).getStability());
            hardInterval = Math.min(hardInterval, goodInterval);
            goodInterval = Math.max(goodInterval, hardInterval + 1);
            easyInterval = Math.max(this.nextInterval(this.card.getRatingToCard().get(FSRSRating.EASY).getStability() * HARD_FACTOR), goodInterval + 1);
            this.card.schedule(hardInterval, goodInterval, easyInterval);
        }

        SchedulingCard newCard = this.card.getRatingToCard().get(this.rating);
        newCard.setLastReview(System.currentTimeMillis());
        newCard.setRepetitions(this.card.getRepetitions() + 1);
        newCard.setElapsedDays(this.card.getElapsedDays());
        newCard.setScheduledDays(this.card.getScheduledDays());
        return new FSRSAlgorithmResult(newCard);
    }

    /**
     * Für alle Karten die standardmäßigen Werte für die Stabilität und Schwierigkeit berechnen
     */
    private void init() {
        for (FSRSRating rating : FSRSRating.values()) {
            this.card.getRatingToCard().get(rating).setDifficulty(this.initDifficulty(rating.toInt()));
            this.card.getRatingToCard().get(rating).setStability(this.initStability(rating.toInt()));
        }
    }

    /**
     * Standardmäßigen Wert für die Schwierigkeit berechnen
     * @param retrievability die Aufrufbarkeit
     * @return die Schwierigkeit
     */
    private float initDifficulty(int retrievability) {
        return Math.min(Math.max(WEIGHTS[2] + WEIGHTS[3] * (retrievability - 2), 1), 10);
    }

    /**
     * Standardmäßigen Wert für die Stabilität berechnen
     * @param retrievability die Aufrufbarkeit
     * @return die Stabilität
     */
    private float initStability(int retrievability) {
        return Math.max(WEIGHTS[0] + WEIGHTS[1] * retrievability, 0.1F);
    }

    /**
     * Berechnen der Stabilität und Schwierigkeit für die nächste Wiederholung
     * @param lastDifficulty die vorherige Schwierigkeit
     * @param lastStability die vorherige Stabilität
     * @param retrievability die Aufrufbarkeit
     */
    private void next(float lastDifficulty, float lastStability, float retrievability) {
        for (FSRSRating rating : FSRSRating.values()) {
            this.card.getRatingToCard().get(rating).setDifficulty(this.nextDifficulty(lastDifficulty, rating.toInt()));
            if (rating == FSRSRating.AGAIN) {
                this.card.getRatingToCard().get(rating).setStability(this.nextForgetStability(this.card.getRatingToCard().get(rating).getDifficulty(), lastStability, retrievability));
            } else {
                this.card.getRatingToCard().get(rating).setStability(this.nextRecallStability(this.card.getRatingToCard().get(rating).getDifficulty(), lastStability, retrievability));
            }
        }
    }

    /**
     * Berechnen des Intervalls für die nächste Wiederholung
     * @param stability die Stabilität
     * @return das Intervall in Tagen
     */
    private int nextInterval(float stability) {
        double interval = stability * Math.log(REQUEST_RETENTION) / Math.log(0.9);
        return (int) Math.min(Math.max(Math.round(interval), 1), MAXIMUM_INTERVAL);
    }

    /**
     * Berechnen der Schwierigkeit für die nächste Wiederholung
     * @param difficulty die alte Schwierigkeit
     * @param retrievability die Aufrufbarkeit
     * @return die neue Schwierigkeit
     */
    private float nextDifficulty(float difficulty, int retrievability) {
        float next = difficulty + WEIGHTS[4] * (retrievability - 2);
        return Math.min(Math.max(this.meanReversion(WEIGHTS[2], next), 1), 100);
    }

    /**
     * Berechnen der Stabilität für die nächste Wiederholung
     * @param difficulty die Schwierigkeit
     * @param stability die vorherige Stabilität
     * @param retrievability die Aufrufbarkeit
     * @return die neue Stabilität
     */
    private float nextRecallStability(float difficulty, float stability, float retrievability) {
        return (float) (stability * (1 + Math.exp(WEIGHTS[6]) *
                                    (11 - difficulty) *
                                    Math.pow(stability, WEIGHTS[7]) *
                                    (Math.exp((1 - retrievability) * WEIGHTS[8]) -1)));
    }

    /**
     * Berechnen der Stabilität für den Fall, dass der Lerninhalt vergessen wurde
     * @param difficulty die Schwierigkeit
     * @param stability die alte Stabilität
     * @param retrievability die Aufrufbarkeit
     * @return die neue Stabilität
     */
    private float nextForgetStability(float difficulty, float stability, float retrievability) {
        return (float) (WEIGHTS[9] * Math.pow(difficulty, WEIGHTS[10]) * Math.pow(stability, WEIGHTS[11]) * Math.exp((1 - retrievability) * WEIGHTS[12]));
    }

    private float meanReversion(float init, float current) {
        return WEIGHTS[5] * init + (1 - WEIGHTS[5]) * current;
    }

    /**
     * Builder mit Fluent Interface zur Objekterzeugung des Free Spaced Repetition Scheduler Algorithmus
     */
    public static class FSRSAlgorithmBuilder {

        private FSRSRating rating = FSRSRating.AGAIN;
        private long lastReview = 0;
        private float stability = 0, difficulty = 0;
        private int elapsedDays = 0, scheduledDays = 0, repetitions = 0;
        private FSRSState state = FSRSState.NEW;

        /**
         * Setzen des Ratings für den Lerninhalt
         * @param rating das Rating
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder rating(FSRSRating rating) {
            this.rating = rating;
            return this;
        }

        /**
         * Setzen des letzten Abfragezeitpunkts des Lerninhalts
         * @param lastReview der Abfragezeitpunkt in Millisekunden
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder lastReview(long lastReview) {
            this.lastReview = lastReview;
            return this;
        }

        /**
         * Setzen der Stabilität
         * @param stability die Stabilität des Lerninhalts
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder stability(float stability) {
            this.stability = stability;
            return this;
        }

        /**
         * Setzen der Schwierigkeit
         * @param difficulty die Schwierigkeit
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder difficulty(float difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        /**
         * Setzen der ausgesetzten Tage zwischen letzter und nächster Wiederholung
         * @param elapsedDays die Anzahl an ausgesetzten Tagen
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder elapsedDays(int elapsedDays) {
            this.elapsedDays = elapsedDays;
            return this;
        }

        /**
         * Setzen der Tage zur nächsten Wiederholung
         * @param scheduledDays die Anzahl an Tagen
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder scheduledDays(int scheduledDays) {
            this.scheduledDays = scheduledDays;
            return this;
        }

        /**
         * Setzen der bisherigen Anzahl an Wiederholungen
         * @param repetitions die Anzahl an Wiederholungen
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder repetitions(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        /**
         * Setzen des Status des Lerninhalts
         * @param state der Status
         * @return neu erzeugte Instanz der statischen Klasse FSRSAlgorithmBuilder
         */
        public FSRSAlgorithmBuilder state(FSRSState state) {
            this.state = state;
            return this;
        }

        /**
         * Baut den Free Spaced Repetition Scheduler Algorithmus mithilfe der zuvor durch den Builder festgelegten Parameter.
         * @return neu gebautes FSRSAlgorithm-Objekt
         */
        public FSRSAlgorithm build() {
            return new FSRSAlgorithm(this);
        }
    }
}