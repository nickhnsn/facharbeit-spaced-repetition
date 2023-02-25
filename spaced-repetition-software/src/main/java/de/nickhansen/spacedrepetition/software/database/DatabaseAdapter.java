package de.nickhansen.spacedrepetition.software.database;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;

/**
 * Der DatabaseAdapter interagiert mit der MySQl-Datenbank.
 * Durch diese Klasse werden die Daten für die Datenbank an das MySQL-Objekt weitergegeben.
 * Außerdem werden alle für die Applikation notwendigen Tabellen in der Datenbank angelegt, falls das noch nicht der Fall ist.
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class DatabaseAdapter {

    private MySQL mySQL;
    private String username, password, hostname, database;

    /**
     * Konstruktor der DatabaseAdapters, der alle Daten aus der Konfiguration ausliest und die Datenbankverbindung herstellt.
     * Außerdem werden alle Tabellen erstellt.
     */
    public DatabaseAdapter() {
        // Daten für die Datenbankverbindung aus der Konfigurationsdatei auslesen
        this.username = SpacedRepetitionApp.getInstance().getConfig().getProperty("mysqlUsername");
        this.password = SpacedRepetitionApp.getInstance().getConfig().getProperty("mysqlPassword");
        this.hostname = SpacedRepetitionApp.getInstance().getConfig().getProperty("mysqlHostname");
        this.database = SpacedRepetitionApp.getInstance().getConfig().getProperty("mysqlDatabase");

        // MySQL-Objekt mit den geladenen Daten erzeugen
        this.mySQL = new MySQL(this.username, this.password, this.hostname, this.database).connect();

        // Datenbanktabellen erstellen
        this.createTables();
    }

    /**
     * Erstellt alle für die Applikation notwendigen Tabellen in der Datenbank, falls diese noch nicht existieren.
     */
    private void createTables() {
        /*
        Vorder- und Rückseitentext der Karteikarte auf 100 Zeichen limitiert; UUIDs sind immer 36 Zeichen lang; Erstellungszeitpunkt wird in Millisekunden gespeichert.
        Dabei wird die UUID als Primärschlüssel festgelegt, da sich mit der UUID die Zeile eindeutig identifizieren lässt. Jede Karteikarte benötigt eine UUID, daher darf diese nicht null sein.
         */
        this.mySQL.queryUpdate("CREATE TABLE IF NOT EXISTS cards (card_uuid VARCHAR(36) NOT NULL, front VARCHAR(100), back VARCHAR(100), created BIGINT, PRIMARY KEY (card_uuid))");

        // ==== Pro Algorithmus eine eigene Tabelle mit algorithmusspezifischen Daten ====

        // Leitner-Algorithmus
        this.mySQL.queryUpdate("CREATE TABLE IF NOT EXISTS leitner (card_uuid VARCHAR(36) NOT NULL, box_id INT DEFAULT 1, day_interval INT DEFAULT 0, next_repetition BIGINT DEFAULT 0, PRIMARY KEY (card_uuid))");

        // SM-2-Algorithmus
        this.mySQL.queryUpdate("CREATE TABLE IF NOT EXISTS sm2 (card_uuid VARCHAR(36) NOT NULL, repetitions INT DEFAULT 0, easiness_factor FLOAT DEFAULT 2.5, day_interval INT DEFAULT 0, next_repetition BIGINT DEFAULT 0, PRIMARY KEY (card_uuid))");

         // FSRS-Algorithmus
        this.mySQL.queryUpdate("CREATE TABLE IF NOT EXISTS fsrs (card_uuid VARCHAR(36) NOT NULL, stability FLOAT DEFAULT 0, difficulty FLOAT DEFAULT 0, elapsed_days INT DEFAULT 0, repetitions INT DEFAULT 0, state TEXT DEFAULT 'NEW', day_interval INT DEFAULT 0, next_repetition BIGINT DEFAULT 0, last_review BIGINT DEFAULT 0, PRIMARY KEY (card_uuid))");
    }

    /**
     * Erhalten des MySQL-Objekts, mit dem die Datenbankverbindung hergestellt wurde.
     * @return das MySQL-Objekt
     */
    public MySQL getMySQL() {
        return this.mySQL;
    }
}
