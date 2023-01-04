package de.nickhansen.facharbeit.spaced_repetition.gui.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Hauptklasse für die grafische Benutzeroberfläche, durch die u. a. das Hauptfenster erzeugt wird:
 * Die MainView erzeugt die zwei Tabs, die in dem Fenster existieren, nämlich die LearnView und die ManageView.
 */
public class MainView extends JFrame {

    private JTabbedPane tabbedPane;
    private LearnView learnView;
    private ManageView manageView;

    /**
     * Konstruktur für die MainView, indem das Hauptfenster des GUIs erzeugt wird.
     * Außerdem werden die Tabs erzeugt und in dem Hauptfenster eingefügt.
     */
    public MainView() {
        // Titel für die Benutzeroberfläche
        super("Spaced-Repetition-Software zur Facharbeit");

        // Bestätigung vor dem Beenden des Programms
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        MainView.this,
                        "Möchtest du das Programm wirklich schließen?",
                        "Schließen bestätigen",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    // Beenden des Programms
                    System.exit(0);
                }
            }
        });

        // Erstellen der TabbedPane, um verschiedene Tabs zu ermöglichen
        this.tabbedPane = new JTabbedPane();

        // Erster Tab
        this.manageView = new ManageView();
        this.tabbedPane.addTab("Karteikarten verwalten", null, this.manageView,
                "Erstelle neue Karteikarten und lösche bisher erstellte");

        // Zweiter Tab
        this.learnView = new LearnView();
        this.tabbedPane.addTab("Karteikarten lernen", null, this.learnView,
                "Lerne erstellte Karteikarten unter Auswahl des Algorithmus");

        // TabbedPane in dem Hauptfenster einfügen
        this.setContentPane(this.tabbedPane);

        // Größe des Hauptfensters festlegen
        this.setMinimumSize(new Dimension(1000, 600));
        this.setSize(new Dimension(1000, 600));

        // Komponenten in das Hauptfenster „packen“ und es sichtbar machen
        this.pack();
        this.setVisible(true);
    }

    /**
     * Erhalten des LearnViews, in der die Karteikarten gelernt werden können
     * @return die LearnView
     */
    public LearnView getLearnView() {
        return this.learnView;
    }

    /**
     * Erhalten des ManageViews, indem neue Karteikarten erstellt werden können, bisherige gelöscht werden können und es eine Karteikartenübersicht gibt
     * @return die ManageView
     */
    public ManageView getManageView() {
        return this.manageView;
    }
}
