package de.nickhansen.facharbeit.spaced_repetition.listener;

import de.nickhansen.facharbeit.spaced_repetition.card.Card;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * Löschen einer Karteikarte über das GUI
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

        // Delete card
        UUID uuid = (UUID) table.getModel().getValueAt(modelRow, 3);
        Card card = Card.getByUUID(uuid);
        if (card != null) {
            card.delete();
        }

        // Delete table row
        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
    }
}
