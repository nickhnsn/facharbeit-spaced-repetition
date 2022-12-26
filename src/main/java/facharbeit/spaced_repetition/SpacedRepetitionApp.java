package facharbeit.spaced_repetition;

import facharbeit.spaced_repetition.view.MainView;

import javax.swing.*;

/**
 * Spaced repetition app class with is instantiated by the launcher
 */
public class SpacedRepetitionApp {

    public MainView mainView;

    /**
     * Instantiates a new Spaced repetition app
     */
    public SpacedRepetitionApp() {
        // Set the UI look to the look of the system if possible
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to the Metal LAF
        }

        // Instantiate main view (GUI)
        this.mainView = new MainView();
    }

    /**
     * Gets main view.
     *
     * @return the main view
     */
    public MainView getMainView() {
        return this.mainView;
    }
}
