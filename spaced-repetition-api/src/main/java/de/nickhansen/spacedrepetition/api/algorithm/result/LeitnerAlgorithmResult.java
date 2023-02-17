package de.nickhansen.spacedrepetition.api.algorithm.result;

/**
 * Berechnete Rückgaben des Algorithmus nach Leitners Lernkarteisystem
 */
public class LeitnerAlgorithmResult extends AlgorithmResult {

    private int boxId, interval;

    /**
     * Konstruktor für das Ergebnis des Algorithmus nach Leitners Lernkarteisystem
     * @param boxId neue Nummer des Fachs für den Lerninhalt
     * @param interval neues Intervall, in welchem der Lerninhalt wiederholt werden soll
     */
    public LeitnerAlgorithmResult(int boxId, int interval) {
        super(interval);
        this.boxId = boxId;
        this.interval = interval;
    }

    /**
     * Erhalten des neuen Intervalls, in welchem der Lerninhalt wiederholt werden soll
     * @return neues Intervall in Tagen
     */
    @Override
    public int getInterval() {
        return this.interval;
    }

    /**
     * Erhalten der neuen Nummer des Fachs für den Lerninhalt
     * @return Nummer des neuen Fachs, in welches der Lerninhalt verschoben wurde
     */
    public int getBoxId() {
        return this.boxId;
    }

    /**
     * Gibt an, ob der Lerninhalt aus der Lernkartei entfernt werden soll.
     * Nach Leitner hat die Lernkartei fünf Fächer, sodass der Lerninhalt nach Fach 5 als "fertig" gelernt angesehen wird.
     * @return
     */
    public boolean toBeRemoved() {
        if (this.boxId > 5) {
            return true;
        }
        return false;
    }
}
