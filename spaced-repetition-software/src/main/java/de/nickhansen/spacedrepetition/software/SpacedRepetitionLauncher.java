package de.nickhansen.spacedrepetition.software;

/**
 * Hauptklasse der Applikation, die den Launcher darstellt.
 * Der Launcher startet die Applikation, indem die Klasse SpacedRepetitionApp instanziiert wird.
 * In dem Konstruktur der SpacedRepetitionApp Klasse geschieht das Hauptsächliche zum Starten der Applikation.
 */
public class SpacedRepetitionLauncher {

    /**
     * Eingangsmethode der Applikation, die beim Starten ausgeführt wird
     * @param args die Programm-Argumente, die beim Start geliefert werden
     */
    public static void main(String[] args) {
        System.out.println("[Launcher] Spaced Repetition App launching ...");
        new SpacedRepetitionApp();
    }
}
