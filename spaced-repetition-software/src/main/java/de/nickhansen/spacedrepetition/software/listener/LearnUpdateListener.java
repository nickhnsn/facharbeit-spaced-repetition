package de.nickhansen.spacedrepetition.software.listener;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.gui.view.LearnView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Aktualisieren des GUIs durch den Aktualisierungsbutton der LearnView
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class LearnUpdateListener implements ActionListener {

    /**
     * Die Methode wird abgerufen, sobald der Aktualisierungsbutton in der LearnView geklickt wird.
     * @param e das ausgeführte ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Über das MainView die LearnView erhalten
        LearnView learnView = SpacedRepetitionApp.getInstance().getMainView().getLearnView();

        // Prüfen, ob der Event-Button mit dem Button aus der LearnView übereinstimmt
        if (e.getSource() == learnView.getUpdateButton()) {
            SpacedRepetitionApp.getInstance().getCardScheduler().queueDueCards();
            SpacedRepetitionApp.getInstance().getCardScheduler().updateGUI();
        }
    }
}
