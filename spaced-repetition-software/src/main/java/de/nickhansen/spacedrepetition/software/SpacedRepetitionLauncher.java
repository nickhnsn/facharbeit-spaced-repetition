package de.nickhansen.spacedrepetition.software;

import javax.swing.*;

/**
 * Hauptklasse der Applikation, die den Launcher darstellt.
 * Der Launcher startet die Applikation, indem die Klasse SpacedRepetitionApp instanziiert wird.
 * In dem Konstruktur der SpacedRepetitionApp Klasse geschieht das Hauptsächliche zum Starten der Applikation.
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class SpacedRepetitionLauncher {

    /**
     * Eingangsmethode der Applikation, die beim Starten ausgeführt wird
     * @param args die Programm-Argumente, die beim Start geliefert werden
     */
    public static void main(String[] args) {
        System.out.println("[Launcher] Spaced Repetition App launching ...");
        try {
            new SpacedRepetitionApp();
            System.out.println("[Launcher] Successfully launched the Spaced Repetition App");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame("a"),
                    "Fehler: " + e,
                    "Fehler beim Starten der Spaced-Repetition-Software", JOptionPane.ERROR_MESSAGE);
            System.out.println("[Launcher] Failed launching the Spaced Repetition App");
        }
    }
}
