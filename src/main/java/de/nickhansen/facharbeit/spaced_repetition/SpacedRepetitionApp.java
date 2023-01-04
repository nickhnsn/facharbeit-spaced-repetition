package de.nickhansen.facharbeit.spaced_repetition;

import de.nickhansen.facharbeit.spaced_repetition.card.Card;
import de.nickhansen.facharbeit.spaced_repetition.database.DatabaseAdapter;
import de.nickhansen.facharbeit.spaced_repetition.gui.view.MainView;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

/**
 * Die Klasse dient als „Zentrale“ der Applikation. Über die Instanz der Klasse kann auf wichtige Programmklassen zugegriffen werden,
 * d. h. auf die Hauptklasse für das GUI (MainView), die Config der Applikation und die Datenbank (durch den DatabaseAdapter).
 * Außerdem werden hier die aus der Datenbank geladenen Karteikarten in einer HashMap gespeichert.
 */
public class SpacedRepetitionApp {

    private static SpacedRepetitionApp instance;
    private MainView mainView;
    private Properties config;
    private DatabaseAdapter databaseAdapter;

    private HashMap<UUID, Card> cards;

    /**
     * Instanziiert die SpacedRepetitionApp. Hier werden für das Starten der Applikation wesentliche Schritte durchgeführt.
     */
    public SpacedRepetitionApp() {
        instance = this;

        // Laden der Konfigurationsdatei, die unter resources/config.properties gefunden werden kann
        try {
            FileInputStream propsInput = new FileInputStream("src/main/resources/config.properties");
            this.config = new Properties();
            this.config.load(propsInput);
            System.out.println("[Config] Successfully loaded the configuration file");
        } catch (IOException e) {
            System.out.println("[Config] Failed loading the configuration file: " + e);
        }

        // Setzen des UI-Aussehens zu dem Aussehen des Systems, sofern möglich
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback zu dem Metal LAF (Standard-Aussehen von Swing)
        }

        // Instanziierung des DatabaseAdapters, der sich mit der MySQL-Datenbank verbindet
        this.databaseAdapter = new DatabaseAdapter();

        // Instanziierung der HashMap, in der die aus der Datenbank geladenen Karteikarten gespeichert werden
        this.cards = new HashMap<>();
        this.loadCards();

        // Instanziierung des MainViews, die Hauptklasse für die Benutzeroberfläche, durch die u. a. das Hauptfenster erzeugt wird
        this.mainView = new MainView();
    }

    /**
     * Laden der Karteikarten aus der Datenbanktabelle "cards".
     * Dabei werden die Karten in der HashMap "cards" in der Klasse abgespeichert.
     */
    public void loadCards() {
        this.databaseAdapter.getMySQL().query("SELECT * FROM cards", resultSet -> {
            try {
                while (resultSet.next()) {
                    new Card(UUID.fromString(resultSet.getString("uuid")), resultSet.getString("front"), resultSet.getString("back"), resultSet.getLong("created"));
                    System.out.println("[Cards] Sucessfully loaded card " + resultSet.getString("uuid"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Erhalten der Klasseninstanz
     * @return die Instanz dieser Klasse
     */
    public static SpacedRepetitionApp getInstance() {
        return instance;
    }

    /**
     * Erhalten der MainView, die Hauptklasse für die Benutzeroberfläche, durch die u. a. das Hauptfenster erzeugt wird
     * @return die MainView der Benutzeroberfläche
     */
    public MainView getMainView() {
        return this.mainView;
    }

    /**
     * Erhalten der Konfiguration für die Applikation
     * @return die Konfiguration in Form einer Properties-Datei
     */
    public Properties getConfig() {
        return this.config;
    }

    /**
     * Erhalten des DatabaseAdapters, der dazu dient, um mit der MySQL-Datenbank zu interagieren
     * @return der DatabaseAdapter
     */
    public DatabaseAdapter getDatabaseAdapter() {
        return this.databaseAdapter;
    }

    /**
     * Erhalten der HashMap, in der die aus der Datenbank geladenen Karteikarten gespeichert werden
     * @return die HashMap mit Karteikarten, in der die Karteikarte durch die UUID erhalten werden kann
     */
    public HashMap<UUID, Card> getCards() {
        return this.cards;
    }
}
