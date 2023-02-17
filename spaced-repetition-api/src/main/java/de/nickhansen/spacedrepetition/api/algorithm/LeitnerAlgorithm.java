package de.nickhansen.spacedrepetition.api.algorithm;

import de.nickhansen.spacedrepetition.api.algorithm.result.LeitnerAlgorithmResult;

/**
 * Implementation eines Algorithmus nach Leitners Lernkarteisystem
 */
public class LeitnerAlgorithm implements Algorithm {

    private int boxId;
    private boolean retrievalSuccessful;

    /**
     * Builder mit Fluent Interface (statische Klasse LeitnerAlgorithmBuilder)
     * Objekterzeugung erfolgt mithilfe eines Builders für den LeitnerAlgorithm
     * @return Builder für den Algorithmus nach Leitners Lernkarteisystem
     */
    public static LeitnerAlgorithmBuilder builder() {
        return new LeitnerAlgorithmBuilder();
    }

    /**
     * Konstruktor für den Algorithmus nach Leitners Lernkarteisystem, der den Builder nutzt.
     * Durch den Builder sind alle nötigen Parameter bestimmt worden.
     * @param builder Builder für den Algorithmus nach Leitners Lernkarteisystem
     */
    public LeitnerAlgorithm(LeitnerAlgorithmBuilder builder) {
        this.boxId = builder.boxId;
        this.retrievalSuccessful = builder.retrievalSuccessful;
    }

    /**
     * Der Algorithmus nach Leitners Lernkarteisystem.
     * Berechnet aus den Inputs boxId und retrievalSuccessful
     * die Outputs newBoxId und newInterval
     * @return LeitnerAlgorithmResult mit den berechneten Rückgabewerten
     */
    @Override
    public LeitnerAlgorithmResult calc() {
        int boxId = 1;
        int interval = 0;

        if (this.retrievalSuccessful) {
            boxId = this.boxId + 1;
        }

        switch (boxId) {
            case 1:
                interval = 1;
                break;
            case 2:
                interval = 3;
                break;
            case 3:
                interval = 7;
                break;
            case 4:
                interval = 30;
                break;
            case 5:
                interval = 30 * 6;
                break;
        }

        return new LeitnerAlgorithmResult(boxId, interval);
    }

    /**
     * Builder mit Fluent Interface zur Objekterzeugung des Leitner-Algorithmus
     */
    public static class LeitnerAlgorithmBuilder {

        private int boxId = 1;
        private boolean retrievalSuccessful = false;

        /**
         * @param boxId Nummer des derzeitigen Fachs
         * @return neu erzeugte Instanz der statischen Klasse LeitnerAlgorithmBuilder
         */
        public LeitnerAlgorithmBuilder boxId(int boxId) {
            this.boxId = boxId;
            return this;
        }

        /**
         * @param retrievalSuccessful Wahrheitswert, der angibt, ob der Lerninhalt erfolgreich abgerufen werden konnte (true) oder nicht (false)
         * @return neu erzeugte Instanz der statischen Klasse LeitnerAlgorithmBuilder
         */
        public LeitnerAlgorithmBuilder retrievalSuccessful(boolean retrievalSuccessful) {
            this.retrievalSuccessful = retrievalSuccessful;
            return this;
        }

        /**
         * Baut den Leitner-Algorithmus mithilfe der zuvor durch den Builder festgelegten Paramter.
         * @return neu gebautes LeitnerAlgorithm-Objekt
         */
        public LeitnerAlgorithm build() {
            return new LeitnerAlgorithm(this);
        }
    }
}

