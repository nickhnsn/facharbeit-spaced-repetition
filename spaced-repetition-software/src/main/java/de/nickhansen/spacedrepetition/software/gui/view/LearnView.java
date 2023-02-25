package de.nickhansen.spacedrepetition.software.gui.view;

import de.nickhansen.spacedrepetition.software.listener.AlgorithmSelectionListener;
import de.nickhansen.spacedrepetition.software.listener.CardRatingListener;
import de.nickhansen.spacedrepetition.software.listener.LearnUpdateListener;
import de.nickhansen.spacedrepetition.software.util.AlgorithmType;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * LearnView als Tab der MainView:
 * Hier können die Karteikarten mit einem ausgewählten Algorithmus gelernt bzw. wiederholt werden.
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class LearnView extends JPanel {

    private JLabel algorithmLabel, chosenCardLabel, frontLabel, backLabel, divideLabel, frontDataLabel, backDataLabel;
    private JComboBox comboBox;
    private JButton updateButton;
    private JButton[] buttons;
    private JPanel buttonPanel;

    /**
     * Konstruktur für die LearnView, indem das Panel erzeugt wird, welches den Tab der MainView darstellt.
     * Es werden alle Komponenten erstellt, aus denen das Panel besteht.
     */
    public LearnView() {
        // Setzen des Layouts für die Tabelle
        setLayout(new TableLayout(new double[][] {
                {300, 550},
                {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 244}}));
        ((TableLayout) getLayout()).setHGap(5);
        ((TableLayout) getLayout()).setVGap(5);

        //---- Label und ComboBox, um einen Algorithmus auszuwählen ----
        this.algorithmLabel = new JLabel();
        this.algorithmLabel.setText("Wähle Algorithmus zum Lernen der Karteikarten aus:");
        this.algorithmLabel.setFont(this.algorithmLabel.getFont().deriveFont(this.algorithmLabel.getFont().getStyle() | Font.BOLD));
        add(this.algorithmLabel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));


        String comboBoxList[] = Arrays.stream(AlgorithmType.values()).map(Enum::name).toArray(String[]::new);
        this.comboBox = new JComboBox(comboBoxList);
        this.comboBox.addItemListener(new AlgorithmSelectionListener());
        add(this.comboBox, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Aktualisierungsbutton ----
        this.updateButton = new JButton();
        this.updateButton.setText("Aktualisieren");
        this.updateButton.addActionListener(new LearnUpdateListener());
        this.updateButton.setVisible(false);
        add(this.updateButton, new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Präsentieren der Karteikarte mit ihrer Vorder- & Rückseite ----
        this.chosenCardLabel = new JLabel();
        this.chosenCardLabel.setText("Ausgewählte Karteikarte");
        this.chosenCardLabel.setFont(this.chosenCardLabel.getFont().deriveFont(this.chosenCardLabel.getFont().getStyle() | Font.BOLD));
        add(this.chosenCardLabel, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Vorderseite der Karteikarte ----
        this.frontLabel = new JLabel();
        this.frontLabel.setText("Vorderseite:");
        this.frontLabel.setFont(this.frontLabel.getFont().deriveFont(this.frontLabel.getFont().getStyle() | Font.ITALIC));
        add(this.frontLabel, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.frontDataLabel = new JLabel();
        this.frontDataLabel.setText("???");
        add(this.frontDataLabel, new TableLayoutConstraints(1, 5, 1, 5, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //---- Abtrennungslinie, um zwischen Vorder- & Rückseite der Karteikarte abzutrennen ----
        this.divideLabel = new JLabel();
        this.divideLabel.setText("---------------------------------------------------------------------------------------------------------------------------------");
        add(this.divideLabel, new TableLayoutConstraints(1, 6, 1, 6, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //---- Rückseite der Karteikarte ----
        this.backLabel = new JLabel();
        this.backLabel.setText("Rückseite:");
        this.backLabel.setFont(this.backLabel.getFont().deriveFont(this.backLabel.getFont().getStyle() | Font.ITALIC));
        add(this.backLabel, new TableLayoutConstraints(0, 7, 0, 7, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.backDataLabel = new JLabel();
        this.backDataLabel.setText("???");
        add(this.backDataLabel, new TableLayoutConstraints(1, 7, 1, 7, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //======== Buttons, um zu bewerten, wie gut man die Karteikarte konnte ========
        // Dafür wird ein neues Panel mit einem eigenen TableLayout erzeugt, welches in das vorherige Panel eingefügt wird.
        this.buttonPanel = new JPanel();
        add(this.buttonPanel, new TableLayoutConstraints(1, 10, 1, 10, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));
    }

    public void updateButtonPanel(int buttons) {
        this.buttons = new JButton[buttons];
        this.buttonPanel.removeAll();

        // Berechnen der Tabellengröße je nach Buttonanzahl
        double[] layout = new double[buttons];
        for (int i = 0; i < buttons; i++) {
            layout[i] = (double) 500 / buttons;
        }

        this.buttonPanel.setLayout(new TableLayout(new double[][] {
                layout,
                {TableLayout.PREFERRED}}));
        ((TableLayout) this.buttonPanel.getLayout()).setHGap(5);
        ((TableLayout) this.buttonPanel.getLayout()).setVGap(5);

        for (int i = 0; i < buttons; i++) {
            this.buttons[i] = new JButton();
            this.buttons[i].setText(String.valueOf(i));
            this.buttons[i].addActionListener(new CardRatingListener());

            if (i == 0) {
                this.buttons[i].setForeground(Color.red);
            } else if (i == buttons-1) {
                this.buttons[i].setForeground(Color.green);
            }

            this.buttonPanel.add(this.buttons[i], new TableLayoutConstraints(i, 0, i, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        }
        this.buttonPanel.updateUI();
    }

    /**
     * Erhalten des Labels, durch das die Vorderseite der Karteikarte gezeigt wir
     * @return das FrontDataLabel
     */
    public JLabel getFrontDataLabel() {
        return this.frontDataLabel;
    }

    /**
     * Erhalten des Labels, durch das die Rückseite der Karteikarte gezeigt wird
     * @return das BackDataLabel
     */
    public JLabel getBackDataLabel() {
        return this.backDataLabel;
    }

    /**
     * Erhalten des Buttons, durch welchen das Panel aktualisiert werden kann
     * @return der Aktualisierungsbutton
     */
    public JButton getUpdateButton() {
        return this.updateButton;
    }

    /**
     * Erhalten der ComboBox, in welcher der genutzte Algorithmus ausgewählt werden kann
     * @return die ComboBox
     */
    public JComboBox getComboBox() {
        return this.comboBox;
    }

    /**
     * Erhalten aller Buttons, die zur Bewertung der Karteikarten genutzt werden
     * @return die Buttons als Array
     */
    public JButton[] getButtons() {
        return this.buttons;
    }
}
