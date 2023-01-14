package de.nickhansen.spacedrepetition.software.gui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import de.nickhansen.spacedrepetition.software.card.Card;
import de.nickhansen.spacedrepetition.software.gui.ButtonColumn;
import de.nickhansen.spacedrepetition.software.listener.CardCreationListener;
import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.listener.CardDeletionListener;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.*;
import java.util.*;

/**
 * The manage view tab of the main view
 */
public class ManageView extends JPanel {

    private JLabel createLabel, frontLabel, backLabel, overviewLabel;
    private JTextField frontTextField, backTextField;
    private JButton createButton;
    private JTable table;
    private JScrollPane scrollPane;

    /**
     * Konstruktor für das ManageView, indem alle Komponenten für das Panel erzeugt werden.
     * Für das Panel wird das TableLayout verwendet, indem die Komponenten angeordnet werden.
     */
    public ManageView() {
        // Setzen des Layouts für die Tabelle
        setLayout(new TableLayout(new double[][] {
                {600, 300, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED},
                {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 374}}));
        ((TableLayout) getLayout()).setHGap(5);
        ((TableLayout) getLayout()).setVGap(5);

        //---- Label zum Erstellen einer Karte ----
        this.createLabel = new JLabel();
        this.createLabel.setText("Karteikarte erstellen");
        this.createLabel.setFont(this.createLabel.getFont().deriveFont(this.createLabel.getFont().getStyle() | Font.BOLD));
        add(this.createLabel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Label für die Vorder- & Rückseite der Karteikarte ----
        this.frontLabel = new JLabel();
        this.frontLabel.setText("Vorderseite");
        add(this.frontLabel, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.backLabel = new JLabel();
        this.backLabel.setText("Rückseite");
        add(this.backLabel, new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Textfelder für die Vorder- & Rückseite der Karteikarte ----,
        this.frontTextField = new JTextField();
        this.backTextField = new JTextField();
        add(this.frontTextField, new TableLayoutConstraints(0, 2, 0, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        add(this.backTextField, new TableLayoutConstraints(1, 2, 1, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Button, um die Karteikarte mit dem Text aus den Textfeldern für die Vorder- & Rückseite der Karteikarte zu erstellen ----
        this.createButton = new JButton();
        this.createButton.setText("Erstellen");
        this.createButton.addActionListener(new CardCreationListener());
        add(this.createButton, new TableLayoutConstraints(3, 2, 3, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Label, um die Karteikartenübersicht, die aus einem JTable besteht, einzuleiten ----
        this.overviewLabel = new JLabel();
        this.overviewLabel.setText("Karteikartenübersicht");
        this.overviewLabel.setFont(this.overviewLabel.getFont().deriveFont(this.overviewLabel.getFont().getStyle() | Font.BOLD));
        add(this.overviewLabel, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Erzeugen der Tabelle, in der alle erstellen Karteikarten aufgeführt werden ----
        this.scrollPane = new JScrollPane();
        this.table = new JTable();
        this.table.setModel(getTableModel());
        this.table.setDefaultEditor(Object.class, null); // JTable nicht editierbar machen durch den Benutzer (statische Zeilen)
        this.scrollPane.setViewportView(this.table);
        this.initButtonColumn();
        add(this.scrollPane, new TableLayoutConstraints(0, 6, 0, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        this.updateTable();
    }

    /**
     * Aktualisiert den Table, indem das TableModel neu gesetzt und die ButtonColumn neu instanziiert wird.
     * Beispielsweise notwendig, wenn neue Karteikarten erstellt oder gelöscht wurden.
     */
    public void updateTable() {
        this.table.setModel(getTableModel());
        this.initButtonColumn();

        /*
        Verstecken der UUID-Spalte, da diese für den Nutzer nicht relevant ist:
        - der Wert der Spalte wird lediglich von dem CardDeletionListener benutzt
        - der Wert der Spalte (sprich die UUID) wird benötigt, um die zu löschende Karte in dem System zu finden
         */
        this.table.getColumnModel().getColumn(3).setWidth(0);
        this.table.getColumnModel().getColumn(3).setMinWidth(0);
        this.table.getColumnModel().getColumn(3).setMaxWidth(0);
    }

    /**
     * Instanziiert die ButtonColumn, die dafür verantwortlich ist, dass eine Spalte erstellt werden kann, in der ein Button ist und das Löschen von Zeilen ermöglicht.
     * Diese ButtonColumn wird für das Löschen von Karteikarten durch einen Button benötigt.
     */
    public void initButtonColumn() {
        new ButtonColumn(table, new CardDeletionListener(), 4);
    }

    /**
     * Das TableModel erzeugt ein Modell für die Tabelle inkl. aller Daten.
     * @return TableModel mit allen Daten
     */
    private TableModel getTableModel() {
        String[] columnNames = {"Vorderseite", "Rückseite", "Erstellt", "UUID", " "};
        Object[][] data = new Object[SpacedRepetitionApp.getInstance().getCards().size()][5];
        int i = 0;

        // Erstellen einer Zeile für jede Karteikarte
        for (Card card : SpacedRepetitionApp.getInstance().getCards().values()) {
            data[i][0] = card.getFront();
            data[i][1] = card.getBack();
            data[i][2] = new Date(card.getCreated());
            data[i][3] = card.getUuid();
            data[i][4] = "Löschen";
            i++;
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        return model;
    }

    /**
     * Erhalten des Buttons, um eine neue Karteikarte zu erstellen
     * @return der CreateButton
     */
    public JButton getCreateButton() {
        return this.createButton;
    }

    /**
     * Erhalten des Textfelds, um den eingegebnen Text für die Vorderseite der Karteikarte zu bekommen
     * @return das FrontTextField
     */
    public JTextField getFrontTextField() {
        return this.frontTextField;
    }

    /**
     * Erhalten des Textfelds, um den eingegebnen Text für die Rückseite der Karteikarte zu bekommen
     * @return das BackTextField
     */
    public JTextField getBackTextField() {
        return this.backTextField;
    }
}
