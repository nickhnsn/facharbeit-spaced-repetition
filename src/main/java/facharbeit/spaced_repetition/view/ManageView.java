package facharbeit.spaced_repetition.view;

import javax.swing.*;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.*;

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
     * Instantiates a new Manage view using TableLayout
     */
    public ManageView() {
        setLayout(new TableLayout(new double[][] {
                {300, 300, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED},
                {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, 374}}));
        ((TableLayout) getLayout()).setHGap(5);
        ((TableLayout) getLayout()).setVGap(5);

        //---- Label to create a card ----
        this.createLabel = new JLabel();
        this.createLabel.setText("Karteikarte erstellen");
        this.createLabel.setFont(this.createLabel.getFont().deriveFont(this.createLabel.getFont().getStyle() | Font.BOLD));
        add(this.createLabel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Label for the front and back of the card ----
        this.frontLabel = new JLabel();
        this.frontLabel.setText("Vorderseite");
        add(this.frontLabel, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.backLabel = new JLabel();
        this.backLabel.setText("Rückseite");
        add(this.backLabel, new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Text fields for the front and back of the card ----,
        this.frontTextField = new JTextField();
        this.backTextField = new JTextField();
        add(this.frontTextField, new TableLayoutConstraints(0, 2, 0, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        add(this.backTextField, new TableLayoutConstraints(1, 2, 1, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Button to create the card with the entered front and back of the card ----
        this.createButton = new JButton();
        this.createButton.setText("Erstellen");
        add(this.createButton, new TableLayoutConstraints(3, 2, 3, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- Label for the overview of the cards ----
        this.overviewLabel = new JLabel();
        this.overviewLabel.setText("Karteikartenübersicht");
        this.overviewLabel.setFont(this.overviewLabel.getFont().deriveFont(this.overviewLabel.getFont().getStyle() | Font.BOLD));
        add(this.overviewLabel, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        this.scrollPane = new JScrollPane();
        this.table = new JTable();
        this.scrollPane.setViewportView(this.table);
        add(this.scrollPane, new TableLayoutConstraints(0, 6, 0, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
    }

    /**
     * Gets the button to create a card
     *
     * @return the create button
     */
    public JButton getCreateButton() {
        return this.createButton;
    }

    /**
     * Gets front text field (used to enter the front text of the card)
     *
     * @return the front text field
     */
    public JTextField getFrontTextField() {
        return this.frontTextField;
    }

    /**
     * Gets back text field (used to enter the back text of the card)
     *
     * @return the back text field
     */
    public JTextField getBackTextField() {
        return this.backTextField;
    }

    /**
     * Gets table which displays all cards and their front/back as well as the option to delete the card
     *
     * @return the table
     */
    public JTable getTable() {
        return this.table;
    }
}
