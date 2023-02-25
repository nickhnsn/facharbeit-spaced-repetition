package de.nickhansen.spacedrepetition.software.listener;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.card.Card;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * Löschen einer Karteikarte über das GUI
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class CardDeletionListener implements ActionListener {

    /**
     * Die Methode wird abgerufen, sobald der Button zum Löschen der Karteikarte in dem JTable der ManageView geklickt wird.
     * @param e das ausgeführte ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = (JTable) e.getSource();
        int modelRow = Integer.parseInt(e.getActionCommand());

        // Karteikarte löschen
        UUID uuid = (UUID) table.getModel().getValueAt(modelRow, 3);
        Card card = Card.getByUUID(uuid);
        if (card != null) {
            card.delete();
        }

        // Tabellenreihe löschen
        ((DefaultTableModel) table.getModel()).removeRow(modelRow);

        // Gelöschte Karteikarten aus dem CardScheduler entfernen
        SpacedRepetitionApp.getInstance().getCardScheduler().queueDueCards();
        SpacedRepetitionApp.getInstance().getCardScheduler().updateGUI();
    }
}
