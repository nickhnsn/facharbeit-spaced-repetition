package de.nickhansen.spacedrepetition.software.listener;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.gui.view.LearnView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Bewerten einer Karteikarte über Buttons der LearnView
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class CardRatingListener implements ActionListener {

    /**
     * Die Methode wird abgerufen, sobald einer der Bewertungsbuttons der LearnView geklickt wird
     * @param e das ausgeführte ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Über das MainView die LearnView erhalten
        LearnView learnView = SpacedRepetitionApp.getInstance().getMainView().getLearnView();

        int index = Arrays.asList(learnView.getButtons()).indexOf(e.getSource());
        // Falls Button mit dem Index existiert
        if (index != -1) {
            SpacedRepetitionApp.getInstance().getCardScheduler().onRating(index);
        }
    }
}
