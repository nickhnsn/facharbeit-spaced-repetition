package de.nickhansen.spacedrepetition.api.algorithm;

import de.nickhansen.spacedrepetition.api.algorithm.result.AlgorithmResult;

/**
 * Bauplan für alle Typen von Algorithmen, die die Spaced-Repetition-Methode implementieren
 */
public interface Algorithm {

    /**
     * Jeder Algorithmus hat bestimmte Rückgaben, die berechnet wurden.
     * @return Rückgaben des Algorithmus
     */
    AlgorithmResult calc();
}
