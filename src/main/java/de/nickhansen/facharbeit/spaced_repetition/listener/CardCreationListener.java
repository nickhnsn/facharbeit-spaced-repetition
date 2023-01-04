package de.nickhansen.facharbeit.spaced_repetition.listener;

import de.nickhansen.facharbeit.spaced_repetition.SpacedRepetitionApp;
import de.nickhansen.facharbeit.spaced_repetition.card.Card;
import de.nickhansen.facharbeit.spaced_repetition.gui.view.ManageView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Erstellen einer Karteikarte über das GUI
 */
public class CardCreationListener implements ActionListener {

    /**
     * Die Methode wird abgerufen, sobald der CreateButton aus der ManageView geklickt wird.
     * @param e das ausgeführte ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Über das MainView die ManageView erhalten
        ManageView manageView = SpacedRepetitionApp.getInstance().getMainView().getManageView();

        // Prüfen, ob der Event-Button mit dem Button aus der ManageView übereinstimmt
        if (e.getSource() == manageView.getCreateButton()) {
            // Vorderseite und Rückseite für die Karteikarte aus den Textfeldern lesen
            String front = manageView.getFrontTextField().getText();
            String back = manageView.getBackTextField().getText();

            // Limitierung des Textes für die Vorder- und Rückseite der Karteikarte auf max. 100 Zeichen
            if (front.length() > 100 || back.length() > 100) {
                JOptionPane.showMessageDialog(manageView, "Der Text auf der Vorder- und Rückseite der Karteikarte darf nicht länger als 100 Zeichen sein");
            // Prüfen, ob die Vorder- und Rückseite der Karteikarte nicht leer ist
            } else if (front.length() != 0 && back.length() != 0) {
                // Karteikarte erstellen
                Card.create(front, back);

                // Textfelder leeren
                manageView.getFrontTextField().setText("");
                manageView.getBackTextField().setText("");

                // Bestätigung über das Erstellen der Karteikarte
                JOptionPane.showMessageDialog(manageView, "Die Karteikarte wurde erfolgreich erstellt");

                // Tabelle der ManageView aktualisieren, in der alle erstellten Karteikarten aufgeführt werden
                manageView.updateTable();
            } else {
                // Fehlermeldung, wenn Textfelder für Vorder- und Rückseite leer sind
                JOptionPane.showMessageDialog(manageView, "Der Text auf der Vorder- und Rückseite der Karteikarte muss mindestens 1 Zeichen lang sein");
            }
        }
    }
}
