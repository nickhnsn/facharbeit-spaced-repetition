package de.nickhansen.spacedrepetition.software.database;

import java.sql.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * MySQL-Datenbankklasse, die die Verbindung zur Datenbank herstellt und verschiedene Aktionen (Updates, Queries, ...) ermöglicht
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class MySQL {

    private String username, password, hostname, database;
    private ExecutorService executorService;
    private Connection connection;

    /**
     * Konstruktur der MySQl-Klasse, in dem u.a. die Daten für die Datenbank festgelegt werden
     *
     * @param username der Benutzername
     * @param password das Passwort
     * @param hostname der Hostname
     * @param database der Datenbankname
     */
    public MySQL(String username, String password, String hostname, String database) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.database = database;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Verbinden mit der MySQL-Datenbank
     * @return das MySQl-Objekt
     */
    public MySQL connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + "3306" + "/" + this.database + "?autoReconnect=true", this.username, this.password);
            System.out.println("[Database] Successfully connected with database");
        } catch (SQLException e) {
            System.out.println("[Database] Could not connect with database: " + e);
        }

        return this;
    }

    /**
     * Verbindung mit der MySQL-Datenbank aufheben
     */
    public void disconnect() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualisieren der Datenbank durch ein PreparedStatement
     * @param statement das PreparedStatement
     */
    public void update(PreparedStatement statement) {
        checkConnection();
        this.executorService.execute(() -> this.queryUpdate(statement));
    }

    /**
     * Aktualisieren der Datenbank durch ein Statement in Form eines Strings
     * @param statement das Statement
     */
    public void update(String statement) {
        checkConnection();
        this.executorService.execute(() -> this.queryUpdate(statement));
    }

    /**
     * Synchrones Aktualisieren der Datenbank durch ein Statement in Form eines Strings
     * @param statement das Statement
     */
    public void syncUpdate(String statement) {
        checkConnection();
        this.queryUpdate(statement);
    }

    /**
     * Query in der Datenbank ausführen
     * @param statement das PreparedStatement
     * @param consumer  der Consumer mit einem ResultSet
     */
    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        checkConnection();
        this.executorService.execute(() -> {
            ResultSet result = this.query(statement);
            consumer.accept(result);
        });
    }

    /**
     * Query in der Datenbank ausführen
     * @param statement das Statement in Form eines Strings
     * @param consumer  der Consumer
     */
    public void query(String statement, Consumer<ResultSet> consumer) {
        checkConnection();
        this.executorService.execute(() -> {
            ResultSet result = this.query(statement);
            consumer.accept(result);
        });
    }

    /**
     * Query in der Datenbank synchron ausführen
     * @param statement das Statement in Form eines Strings
     * @param consumer  der Consumer
     */
    public void syncQuery(String statement, Consumer<ResultSet> consumer) {
        ResultSet result = this.query(statement);
        consumer.accept(result);
    }


    /**
     * Vorbereiten eines PreparedStatements
     * @param query die Query
     * @return das fertige PreparedStatement
     */
    public PreparedStatement prepare(String query) {
        checkConnection();
        try {
            return this.connection.prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Datenbank durch eine Query aktualisieren
     * @param query die Query als String
     */
    public void queryUpdate(String query) {
        checkConnection();
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            queryUpdate(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Datenbank durch ein PreparedStatement aktualisieren
     * @param preparedStatement das PreparedStatement
     */
    public void queryUpdate(PreparedStatement preparedStatement) {
        checkConnection();
        try {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Query durchführen mithilfe eines Query-Strings
     * @param query die Query
     * @return das ResultSet
     */
    public ResultSet query(String query) {
        checkConnection();
        try {
            return query(this.connection.prepareStatement(query));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Query durchführen mithilfe eines PreparedStatements
     * @param statement das PreparedStatement
     * @return das ResultSet
     */
    public ResultSet query(PreparedStatement statement) {
        checkConnection();
        try {
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prüfen, ob die Datenbankverbindung noch besteht
     */
    private void checkConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wahrheitswert, der angibt, ob die Datenbankverbindung besteht
     * @return der Boolean, der die Datenbankverbindung angibt
     */
    public boolean isConnected() {
        return connection != null;
    }
}