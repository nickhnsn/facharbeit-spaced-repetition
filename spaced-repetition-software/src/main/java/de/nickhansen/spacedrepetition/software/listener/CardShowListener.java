package de.nickhansen.spacedrepetition.software.listener;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.gui.view.LearnView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Aufdecken der Rückseite einer Karteikarte
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class CardShowListener implements ActionListener {


    /**
     * Die Methode wird abgerufen, sobald der Aufdeckungsbutton der LearnView geklickt wird.
     * @param e das ausgeführte ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Über das MainView die LearnView erhalten
        LearnView learnView = SpacedRepetitionApp.getInstance().getMainView().getLearnView();

        // Prüfen, ob der Event-Button mit dem Button aus der LearnView übereinstimmt
        if (e.getSource() == learnView.getShowButton()) {
            learnView.getShowButton().setVisible(false);
            learnView.getBackDataLabel().setVisible(true);
        }
    }
}
