package de.nickhansen.spacedrepetition.software.listener;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.card.CardScheduler;
import de.nickhansen.spacedrepetition.software.gui.view.LearnView;
import de.nickhansen.spacedrepetition.software.util.AlgorithmType;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Auswählen eines Algorithmus über die ComboBox
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class AlgorithmSelectionListener implements ItemListener {

    /**
     * Die Methode wird abgerufen, sobald etwas in der CombeBox der LearnView ausgewählt wird.
     * @param event das ausgeführte ActionEvent
     */
    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            // Über das MainView die LearnView erhalten
            LearnView learnView = SpacedRepetitionApp.getInstance().getMainView().getLearnView();

            // Prüfen, ob die Event-ComoBox mit der ComboBox aus der LearnView übereinstimmt
            if (event.getSource() == learnView.getComboBox()) {
                // Selektierten AlgorithmType aus der ComboBox auslesen
                JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
                AlgorithmType type = AlgorithmType.valueOf((String) comboBox.getSelectedItem());

                // Prüfen, ob der CardScheduler bereits den Algorithmustyp behandelt
                if (type != SpacedRepetitionApp.getInstance().getCardScheduler().getType()) {
                    SpacedRepetitionApp.getInstance().setCardScheduler(new CardScheduler(type));
                }
            }
        }
    }
}
