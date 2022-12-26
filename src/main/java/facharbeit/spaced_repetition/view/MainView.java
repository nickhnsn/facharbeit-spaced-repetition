package facharbeit.spaced_repetition.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main view of the GUI program
 */
public class MainView extends JFrame {

    private JTabbedPane tabbedPane;
    private LearnView learnView;
    private ManageView manageView;

    /**
     * Instantiates a new Main view.
     */
    public MainView() {
        super("Spaced-Repetition-Software zur Facharbeit");

        // Ask for confirmation before terminating the program
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
                    System.exit(0);
                }
            }
        });

        // Create tabbed pane in order to have tabs
        this.tabbedPane = new JTabbedPane();

        // First tab
        this.manageView = new ManageView();
        this.tabbedPane.addTab("Karteikarten verwalten", null, this.manageView,
                "Erstelle neue Karteikarten und lösche bisher erstellte");

        // Second tab
        this.learnView = new LearnView();
        this.tabbedPane.addTab("Karteikarten lernen", null, this.learnView,
                "Lerne erstellte Karteikarten unter Auswahl des Algorithmus");

        this.setContentPane(tabbedPane);
        this.setMinimumSize(new Dimension(1000, 600));
        this.setSize(new Dimension(1000, 600));
        this.pack();
        this.setVisible(true);
    }
}
