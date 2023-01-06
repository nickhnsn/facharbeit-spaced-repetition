package de.nickhansen.facharbeit.spaced_repetition.database;

import de.nickhansen.facharbeit.spaced_repetition.SpacedRepetitionApp;

/**
 * Der DatabaseAdapter interagiert mit der MySQl-Datenbank.
 * Durch diese Klasse werden die Daten für die Datenbank an das MySQL-Objekt weitergegeben.
 * Außerdem werden alle für die Applikation notwendigen Tabellen in der Datenbank angelegt, falls das noch nicht der Fall ist.
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
        // Vorder- und Rückseitentext der Karteikarte auf 100 Zeichen limitiert; UUIDs sind immer 36 Zeichen lang; Erstellungszeitpunkt wird in Millisekunden gespeichert
        this.mySQL.queryUpdate("CREATE TABLE IF NOT EXISTS cards (uuid VARCHAR(36), front VARCHAR(100), back VARCHAR(100), created bigint)");
    }

    /**
     * Erhalten des MySQL-Objekts, mit dem die Datenbankverbindung hergestellt wurde.
     * @return das MySQL-Objekt
     */
    public MySQL getMySQL() {
        return this.mySQL;
    }
}
