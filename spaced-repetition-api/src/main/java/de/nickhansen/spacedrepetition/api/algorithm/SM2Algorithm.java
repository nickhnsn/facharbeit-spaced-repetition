package de.nickhansen.spacedrepetition.api.algorithm;

import de.nickhansen.spacedrepetition.api.algorithm.result.SM2AlgorithmResult;

/**
 * Implementation des SuperMemo-Algorithmus SM-2 nach:
 * https://super-memory.com/english/ol/sm2.htm
 */
public class SM2Algorithm implements Algorithm {

    private int repetitions;
    private int quality;
    private float easinessFactor;
    private float interval;

    /**
     * Builder mit Fluent Interface (statische Klasse SM2AlgorithmBuilder)
     * Objekterzeugung erfolgt mithilfe eines Builders für den SM2Algorithm
     * @return Builder für den SM-2-Algorithmus
     */
    public static SM2AlgorithmBuilder builder() {
        return new SM2AlgorithmBuilder();
    }

    /**
     * Konstruktor für den SM-2-Algorithmus, der den Builder nutzt.
     * Durch den Builder sind alle nötigen Parameter bestimmt worden.
     * @param builder Builder für den SM-2-Algorithmus
     */
    public SM2Algorithm(SM2AlgorithmBuilder builder) {
        this.repetitions = builder.repetitions;
        this.easinessFactor = builder.easinessFactor;
        this.quality = builder.quality;
        this.interval = builder.interval;
    }

    /**
     * Der SuperMemo-Algorithmus SM-2.
     * Berechnet aus den Inputs repetitions, easinessFactor, quality und interval
     * die Outputs newRepetitions, newEasinessFactor und newInterval.
     * @return SM2AlgorithmResult mit den berechneten Rückgabewerten
     */
    @Override
    public SM2AlgorithmResult calc() {
        int newRepetitions = repetitions;
        int newInterval;
        float newEasinessFactor;

        // Durch Math.max(...) wird die Begrenzung von EF berücksichtigt: 2,5 ≥ EF ≥ 1,3
        newEasinessFactor = (float) Math.max(1.3, Math.max(easinessFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)), 2.5));

        // Berücksichtigung der Begrenzung von der Qualität 0 ≤ q ≤ 5
        if (quality > 5) {
            quality = 5;
        }

        if (quality < 3) {
            newRepetitions = 0;
            newEasinessFactor = easinessFactor;
        } else {
            newRepetitions++;
        }

        switch (newRepetitions) {
            case 0:
            case 1:
                newInterval = 1;
                break;
            case 2:
                newInterval = 6;
                break;
            default:
                newInterval = Math.round(interval * easinessFactor);
        }

        return new SM2AlgorithmResult(newRepetitions, newEasinessFactor, newInterval);
    }

    /**
     * Builder mit Fluent Interface zur Objekterzeugung des SM-2-Algorithmus
     */
    public static class SM2AlgorithmBuilder {

        private int repetitions = 1;
        private float easinessFactor = 2.5F;
        private int interval = 0;
        private int quality = 0;

        /**
         * @param repetitions bisherige Wiederholungen
         * @return neu erzeugte Instanz der statischen Klasse SM2AlgorithmBuilder
         */
        public SM2AlgorithmBuilder repetitions(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        /**
         * @param easinessFactor bisheriger Leichtigkeitsfaktor EF
         * @return neu erzeugte Instanz der statischen Klasse SM2AlgorithmBuilder
         */
        public SM2AlgorithmBuilder easinessFactor(float easinessFactor) {
            this.easinessFactor = easinessFactor;
            return this;
        }

        /**
         * @param interval zuvor genutztes Intervall
         * @return neu erzeugte Instanz der statischen Klasse SM2AlgorithmBuilder
         */
        public SM2AlgorithmBuilder interval(int interval) {
            this.interval = interval;
            return this;
        }

        /**
         * @param quality Qualität mit welcher der Lerninhalt bewertet wurde
         * @return neu erzeugte Instanz der statischen Klasse SM2AlgorithmBuilder
         */
        public SM2AlgorithmBuilder quality(int quality) {
            this.quality = quality;
            return this;
        }

        /**
         * Baut den SM-2-Algorithmus mithilfe der zuvor durch den Builder festgelegten Parameter.
         * @return neu gebautes SM2Algorithm-Objekt
         */
        public SM2Algorithm build() {
            return new SM2Algorithm(this);
        }
    }
}
