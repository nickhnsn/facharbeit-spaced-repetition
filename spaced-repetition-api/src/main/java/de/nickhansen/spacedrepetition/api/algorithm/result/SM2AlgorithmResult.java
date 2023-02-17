package de.nickhansen.spacedrepetition.api.algorithm.result;

/**
 * Berechnete Rückgaben des SuperMemo-Algorithmus SM-2
 */
public class SM2AlgorithmResult extends AlgorithmResult {

    private int repetitions, interval;
    float easinessFactor;

    /**
     * Konstruktor für das Ergebnis des SM-2-Algorithmus
     * @param repetitions bisherige Anzahl an Wiederholungen
     * @param easinessFactor bisheriger Leichtigkeitsfaktor (sollte zwischen 1,3 und 2,5 sein)
     * @param interval bisheriges Intervall, in welchem der Lerninhalt wiederholt wurde
     */
    public SM2AlgorithmResult(int repetitions, float easinessFactor, int interval) {
        super(interval);
        this.repetitions = repetitions;
        this.easinessFactor = easinessFactor;
        this.interval = interval;
    }

    /**
     * Erhalten des vorherigen Intervalls, in welchem der Lerninhalt wiederholtwurde
     * @return vorheriges Intervall in Tagen
     */
    @Override
    public int getInterval() {
        return this.interval;
    }

    /**
     * Erhalten der bisherigen Anzahl an Wiederholungen des Lerninhalts.
     * @return Anzahl an Wiederholungen
     */
    public int getRepetitions() {
        return this.repetitions;
    }

    /**
     * Erhalten des bisherigen Leichtigkeitsfaktors für den Lerninhalt
     * @return Leichtigkeitsfaktor EF (2,5 ≥ EF ≥ 1,3)
     */
    public float getEasinessFactor() {
        return this.easinessFactor;
    }
}
