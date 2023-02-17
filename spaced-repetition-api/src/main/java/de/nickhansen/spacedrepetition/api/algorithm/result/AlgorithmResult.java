package de.nickhansen.spacedrepetition.api.algorithm.result;

/**
 * Abstraktes Klassenmodell für die berechneten Rückgaben von verschiedenen Algorithmen
 */
public abstract class AlgorithmResult {

    private int interval;

    /**
     * Konstruktor für die abstrakte Klasse AlgorithmResult.
     * Jeder implementierte Algorithmus muss mindestens ein neues Intervall zurückgeben.
     * @param interval neues Wiederholungsintervall für den Lerninhalt in Tagen
     */
    public AlgorithmResult(int interval) {
        this.interval = interval;
    }

    /**
     * Berechnung des Zeitpunkts der nächsten notwendigen Wiederholung in Millisekunden
     * @return Zeitpunkt in Millisekunden, an welchem die nächste Wiederholung stattfinden soll
     */
    public long getNextRepetitionTime() {
        // Ein Tag in Millisekunden
        long dayMillis = 60 * 60 * 24 * 1000;
        // Derzeitige Zeit in Millisekunden
        long currentMillis = System.currentTimeMillis();

        return currentMillis + (dayMillis * interval);
    }


    /**
     * Erhalten des neu berechneten Wiederholungsintervalls, welches jede Unterklasse dem Konstruktor übergeben muss
     * @return neues Wiederholungsintervall für den Lerninhalt in Tagen
     */
    public int getInterval() {
        return this.interval;
    }
}
