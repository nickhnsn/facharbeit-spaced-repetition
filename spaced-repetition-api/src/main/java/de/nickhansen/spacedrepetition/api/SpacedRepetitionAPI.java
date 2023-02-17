package de.nickhansen.spacedrepetition.api;

import de.nickhansen.spacedrepetition.api.algorithm.LeitnerAlgorithm;
import de.nickhansen.spacedrepetition.api.algorithm.SM2Algorithm;
import de.nickhansen.spacedrepetition.api.algorithm.result.LeitnerAlgorithmResult;
import de.nickhansen.spacedrepetition.api.algorithm.result.SM2AlgorithmResult;

/**
 * Hauptklasse der SpacedRepetitionAPI
 */
public class SpacedRepetitionAPI {

    private static SpacedRepetitionAPI instance;

    /**
     * Konstruktor für die SpacedRepetitionAPI
     */
    protected SpacedRepetitionAPI() {
        instance = this;
    }

    /**
     * Beispiel zur Verwendung des implementierten SM-2-Algorithmus mit dem Builder.
     * @param quality Qualität mit welcher der Lerninhalt bewertet wurde
     * @param repetitions bisherige Wiederholungen
     * @param easinessFactor bisheriger Leichtigkeitsfaktor EF
     * @param interval zuvor genutztes Intervall
     * @return
     */
    public SM2AlgorithmResult basicSM2(int quality, int repetitions, int easinessFactor, int interval) {
        SM2Algorithm sm2 = SM2Algorithm.builder()
                .quality(quality)
                .repetitions(repetitions)
                .easinessFactor(easinessFactor)
                .interval(interval)
                .build();

        return sm2.calc();
    }

    /**
     * Beispiel zur Verwendung des implementierten Algorithmus nach Leitners Lernkartei.
     * @param boxId bisherige Nummer des Fachs für den Inhalt
     * @param retrievalSuccessful Wahrheitswert, der angibt, ob der Lerninhalt erfolgreich abgerufen werden konnte (true) oder nicht (false)
     * @return
     */
    public LeitnerAlgorithmResult basicLeitner(int boxId, boolean retrievalSuccessful) {
        LeitnerAlgorithm leitner = LeitnerAlgorithm.builder()
                .boxId(boxId)
                .retrievalSuccessful(retrievalSuccessful)
                .build();

        return leitner.calc();
    }

    /**
     * Erhalten der Klasseninstanz
     * @return die Instanz dieser Klasse
     */
    public static SpacedRepetitionAPI getInstance() {
        return instance;
    }
}
