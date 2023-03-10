package de.nickhansen.spacedrepetition.api.algorithm.result;

/**
 * Berechnete Rückgaben des SuperMemo-Algorithmus SM-2
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class SM2AlgorithmResult extends AlgorithmResult {

    private int repetitions;
    private float easinessFactor;

    /**
     * Konstruktor für das Ergebnis des SM-2-Algorithmus
     * @param repetitions neue Anzahl an Wiederholungen
     * @param easinessFactor neuer Leichtigkeitsfaktor (sollte zwischen 1,3 und 2,5 sein)
     * @param interval neues Intervall, in welchem der Lerninhalt wiederholt wurde
     */
    public SM2AlgorithmResult(int repetitions, float easinessFactor, int interval) {
        super(interval);
        this.repetitions = repetitions;
        this.easinessFactor = easinessFactor;
    }

    /**
     * Erhalten der neuen Anzahl an Wiederholungen des Lerninhalts
     * @return aktualisierte Anzahl an Wiederholungen
     */
    public int getRepetitions() {
        return this.repetitions;
    }

    /**
     * Erhalten des neuen Leichtigkeitsfaktors für den Lerninhalt
     * @return neuer Leichtigkeitsfaktor EF (2,5 ≥ EF ≥ 1,3)
     */
    public float getEasinessFactor() {
        return this.easinessFactor;
    }
}
