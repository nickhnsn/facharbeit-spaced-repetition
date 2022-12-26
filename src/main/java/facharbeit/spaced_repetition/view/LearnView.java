package facharbeit.spaced_repetition.view;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import java.awt.*;

/**
 * The learn view tab of the main view
 */
public class LearnView extends JPanel {

    private JLabel algorithmLabel, chosenCardLabel, frontLabel, backLabel, divideLabel, frontDataLabel, backDataLabel;
    private JComboBox comboBox;
    private JButton againButton, hardButton, okayButton, goodButton, easyButton;

    /**
     * Instantiates a new Learn view using TableLayout
     */
    public LearnView() {
        setLayout(new TableLayout(new double[][] {
                {300, 550},
                {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 244}}));
        ((TableLayout) getLayout()).setHGap(5);
        ((TableLayout) getLayout()).setVGap(5);

        //---- Label and combo box to choose an algorithm ----
        this.algorithmLabel = new JLabel();
        this.algorithmLabel.setText("Wähle Algorithmus zum Lernen der Karteikarten aus:");
        this.algorithmLabel.setFont(this.algorithmLabel.getFont().deriveFont(this.algorithmLabel.getFont().getStyle() | Font.BOLD));
        add(this.algorithmLabel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        String comboBoxList[] = {"Neuronales Netz", "Leitner-System", "SuperMemo-Algorithmus"};
        this.comboBox = new JComboBox(comboBoxList);
        add(this.comboBox, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Present card with front and back ----
        this.chosenCardLabel = new JLabel();
        this.chosenCardLabel.setText("Ausgewählte Karteikarte");
        this.chosenCardLabel.setFont(this.chosenCardLabel.getFont().deriveFont(this.chosenCardLabel.getFont().getStyle() | Font.BOLD));
        add(this.chosenCardLabel, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Front ----
        this.frontLabel = new JLabel();
        this.frontLabel.setText("Vorderseite:");
        this.frontLabel.setFont(this.frontLabel.getFont().deriveFont(this.frontLabel.getFont().getStyle() | Font.ITALIC));
        add(this.frontLabel, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.frontDataLabel = new JLabel();
        this.frontDataLabel.setText("???");
        add(this.frontDataLabel, new TableLayoutConstraints(1, 5, 1, 5, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //---- Divide front and back ----
        this.divideLabel = new JLabel();
        this.divideLabel.setText("---------------------------------------------------------------------------------------------------------------------------------");
        add(this.divideLabel, new TableLayoutConstraints(1, 6, 1, 6, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //---- Back ----
        this.backLabel = new JLabel();
        this.backLabel.setText("Rückseite:");
        this.backLabel.setFont(this.backLabel.getFont().deriveFont(this.backLabel.getFont().getStyle() | Font.ITALIC));
        add(this.backLabel, new TableLayoutConstraints(0, 7, 0, 7, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.backDataLabel = new JLabel();
        this.backDataLabel.setText("???");
        add(this.backDataLabel, new TableLayoutConstraints(1, 7, 1, 7, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));

        //======== Buttons to rate the card ========
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new TableLayout(new double[][] {
                {100, 100, 100, 100, 100},
                {TableLayout.PREFERRED}}));
        ((TableLayout) buttonPanel.getLayout()).setHGap(5);
        ((TableLayout) buttonPanel.getLayout()).setVGap(5);

        this.againButton = new JButton();
        this.againButton.setText("Nochmal");
        buttonPanel.add(this.againButton, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.hardButton = new JButton();
        this.hardButton.setText("Schwer");
        this.hardButton.setForeground(Color.red);
        buttonPanel.add(this.hardButton, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.okayButton = new JButton();
        this.okayButton.setText("Okay");
        this.okayButton.setForeground(Color.orange);
        buttonPanel.add(this.okayButton, new TableLayoutConstraints(2, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.goodButton = new JButton();
        this.goodButton.setText("Gut");
        this.goodButton.setForeground(Color.green);
        buttonPanel.add(this.goodButton, new TableLayoutConstraints(3, 0, 3, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.easyButton = new JButton();
        this.easyButton.setText("Einfach");
        this.easyButton.setForeground(Color.blue);
        buttonPanel.add(this.easyButton, new TableLayoutConstraints(4, 0, 4, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        add(buttonPanel, new TableLayoutConstraints(1, 10, 1, 10, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));
    }

    /**
     * Gets divide label
     *
     * @return the divide label
     */
    public JLabel getDivideLabel() {
        return this.divideLabel;
    }

    /**
     * Gets front data label
     *
     * @return the front data label
     */
    public JLabel getFrontDataLabel() {
        return this.frontDataLabel;
    }

    /**
     * Gets back data label
     *
     * @return the back data label
     */
    public JLabel getBackDataLabel() {
        return this.backDataLabel;
    }
}
