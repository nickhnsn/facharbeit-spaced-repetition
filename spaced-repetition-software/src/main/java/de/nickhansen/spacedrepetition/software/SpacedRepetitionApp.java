package de.nickhansen.spacedrepetition.software;

import de.nickhansen.spacedrepetition.software.card.Card;
import de.nickhansen.spacedrepetition.software.card.CardScheduler;
import de.nickhansen.spacedrepetition.software.database.DatabaseAdapter;
import de.nickhansen.spacedrepetition.software.gui.view.MainView;
import de.nickhansen.spacedrepetition.software.util.AlgorithmType;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * Die Klasse dient als „Zentrale“ der Applikation. Über die Instanz der Klasse kann auf wichtige Programmklassen zugegriffen werden,
 * d. h. auf die Hauptklasse für das GUI (MainView), die Config der Applikation und die Datenbank (durch den DatabaseAdapter).
 * Außerdem werden hier die aus der Datenbank geladenen Karteikarten in einer HashMap gespeichert
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class SpacedRepetitionApp {

    private static SpacedRepetitionApp instance;
    private MainView mainView;
    private Properties config;
    private DatabaseAdapter databaseAdapter;

    private Map<UUID, Card> cards;
    private CardScheduler cardScheduler;

    /**
     * Instanziiert die SpacedRepetitionApp. Hier werden für das Starten der Applikation wesentliche Schritte durchgeführt
     */
    protected SpacedRepetitionApp() {
        instance = this;

        // Laden der Konfigurationsdatei config.properties
        this.config = new Properties();
        try {
            // Falls eine Config im selben Ordner vorliegt, in der die JAR ausgeführt wird, wird diese genommen
            if (new File("config.properties").exists()) {
                this.config.load(Files.newInputStream(Paths.get("config.properties")));
            } else {
                // Rückgriff auf die Config mit den Standardwerten unter resources/config.properties bzw. die kompilierte Config
                this.config.load(getClass().getResourceAsStream("/config.properties"));
            }
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

        // Standardmäßiger CardScheduler für den Algorithmus nach Leitner, der standardmäßig in der ComboBox der LearnView selektiert ist
        this.cardScheduler = new CardScheduler(AlgorithmType.LEITNER_SYSTEM);
    }

    /**
     * Laden der Karteikarten aus der Datenbanktabelle "cards".
     * Dabei werden die Karten in der HashMap "cards" in der Klasse abgespeichert
     */
    public void loadCards() {
        this.databaseAdapter.getMySQL().query("SELECT * FROM cards", resultSet -> {
            try {
                while (resultSet.next()) {
                    new Card(UUID.fromString(resultSet.getString("card_uuid")), resultSet.getString("front"), resultSet.getString("back"), resultSet.getLong("created"));
                    System.out.println("[Cards] Sucessfully loaded card " + resultSet.getString("card_uuid"));
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
    public Map<UUID, Card> getCards() {
        return this.cards;
    }

    /**
     * Erhalten des CardSchedulers, der die zu wiederholenden Karteikarten verwaltet
     * @return der CardScheduler
     */
    public CardScheduler getCardScheduler() {
        return this.cardScheduler;
    }

    /**
     * Setzen des CardSchedulers, der die zu wiederholenden Karteikarten verwaltet (z. B. falls der Algorithmus gewechselt wird und der Scheduler entsprechend an den neuen Algorithmus angepasst werden muss)
     * @param cardScheduler der neue CardScheduler
     */
    public void setCardScheduler(CardScheduler cardScheduler) {
        this.cardScheduler = cardScheduler;
    }
}
