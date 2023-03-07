package de.nickhansen.spacedrepetition.api;

import de.nickhansen.spacedrepetition.api.algorithm.FSRSAlgorithm;
import de.nickhansen.spacedrepetition.api.algorithm.LeitnerAlgorithm;
import de.nickhansen.spacedrepetition.api.algorithm.SM2Algorithm;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSRating;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSState;
import de.nickhansen.spacedrepetition.api.algorithm.result.FSRSAlgorithmResult;
import de.nickhansen.spacedrepetition.api.algorithm.result.LeitnerAlgorithmResult;
import de.nickhansen.spacedrepetition.api.algorithm.result.SM2AlgorithmResult;

/**
 * Hauptklasse der SpacedRepetitionAPI
 *
 * @author Nick Hansen
 * @version 25.02.2023
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
     * Beispiel zur Verwendung des implementierten Algorithmus nach Leitners Lernkartei
     * @param boxId bisherige Nummer des Fachs für den Inhalt
     * @param retrievalSuccessful Wahrheitswert, der angibt, ob der Lerninhalt erfolgreich abgerufen werden konnte (true) oder nicht (false)
     * @return Rückgabewerte des Algorithmus nach Leitners Lernkartei
     */
    public LeitnerAlgorithmResult basicLeitner(int boxId, boolean retrievalSuccessful) {
        LeitnerAlgorithm leitner = LeitnerAlgorithm.builder()
                .boxId(boxId)
                .retrievalSuccessful(retrievalSuccessful)
                .build();

        return leitner.calc();
    }

    /**
     * Beispiel zur Verwendung des implementierten SM-2-Algorithmus mit dem Builder
     * @param quality Qualität mit welcher der Lerninhalt bewertet wurde
     * @param repetitions bisherige Wiederholungen
     * @param easinessFactor bisheriger Leichtigkeitsfaktor EF
     * @param interval zuvor genutztes Intervall
     * @return Rückgabewerte des SM-2-Algorithmus
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
     * Beispiel zur Verwendung des implementierten Free Spaced Repetition Scheduler Algorithmus mit dem Builder
     * @param rating die Bewertung des Abrufens des Lerninhalts
     * @param stability die Stabilität des Lerninhalts
     * @param difficulty die Schwierigkeit des Lerninhalts
     * @param elapsedDays die ausgesetzten Tage zwischen letzter und nächster Wiederholung
     * @param scheduledDays die berechneten Tage zur nächsten Wiederholung
     * @param repetitions die bisherige Anzahl an Wiederholungen
     * @param state der Status des Lerninhalts
     * @param lastReview der letzte Zeitpunkt des Abfragens des Lerninhalts
     * @return Rückgabewerte des Free Spaced Repetition Scheduler Algorithmus
     */
    public FSRSAlgorithmResult basicFSRS(FSRSRating rating, float stability, float difficulty, int elapsedDays, int scheduledDays, int repetitions, FSRSState state, long lastReview) {
        FSRSAlgorithm fsrs = FSRSAlgorithm.builder()
                .rating(rating)
                .stability(stability)
                .difficulty(difficulty)
                .elapsedDays(elapsedDays)
                .scheduledDays(scheduledDays)
                .repetitions(repetitions)
                .state(state)
                .lastReview(lastReview)
                .build();

        return fsrs.calc();
    }

    /**
     * Erhalten der Klasseninstanz
     * @return die Instanz dieser Klasse
     */
    public static SpacedRepetitionAPI getInstance() {
        return instance;
    }
}
