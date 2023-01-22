package de.nickhansen.spacedrepetition.software.card;

import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Diese Klasse modelliert eine Karteikarte.
 */
public class Card {

    private UUID uuid;
    private String front, back;
    private Long created;

    /**
     * Konstruktor für die Karteikarte, in dem alle übergebenen Paramter die deklarierten Klassenattribute initialiseren
     * @param uuid die UUID der Karteikarte zur Identifikation
     * @param front der Vorderseitentext der Karteikarte
     * @param back der Rückseitentext der Karteikarte
     * @param created der Zeitpunkt, zu dem die Karteikarte erstellt wurde
     */
    public Card(UUID uuid, String front, String back, Long created) {
        this.uuid = uuid;
        this.front = front;
        this.back = back;
        this.created = created;

        SpacedRepetitionApp.getInstance().getCards().put(uuid, this);
    }

    /**
     * Erstellen einer Karteikarte mit den gegebenen Daten und einer neu generierten, zufälligen UUID
     * @param front der Vorderseitentext der Karteikarte
     * @param back der Rückseitentext der Karteikarte
     */
    public static void create(String front, String back) {
        UUID uuid = UUID.randomUUID(); // Zufällig generierte UUID für die Karteikarte
        Long created = System.currentTimeMillis(); // Aktueller Zeitpunkt zur Erstellung der Karteikarte

        // Karteikarte in der Datenbank erstellen
        try {
            PreparedStatement ps = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().prepare("INSERT INTO cards (uuid, front, back, created) VALUES (?, ?, ?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, front);
            ps.setString(3, back);
            ps.setString(4, created.toString());
            ps.executeUpdate();
            System.out.println("[Cards] Sucessfully inserted card " + uuid + " into database");
        } catch (SQLException e) {
            System.out.println("[Cards] Failed inserting the card " + uuid + " into database: " + e);
        }

        // Karteikarten-Objekt erstellen
        new Card(uuid, front, back, created);
        System.out.println("[Cards] Sucessfully created card " + uuid + " locally");
    }

    /**
     * Löschen einer Karteikarte, indem es aus der Datenbank und der HashMap "cards" in der SpacedRepetitionApp gelöscht wird
     */
    public void delete() {
        SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().update("DELETE FROM cards WHERE uuid='" + this.uuid + "'");
        SpacedRepetitionApp.getInstance().getCards().remove(this.uuid, this);
        System.out.println("[Cards] Sucessfully deleted card " + this.uuid);
    }

    /**
     * Erhalten einer Karteikarte über ihre UUID
     * @param uuid die UUID der Karteikarte
     * @return geladenes Card-Objekt
     */
    public static Card getByUUID(UUID uuid) {
        // Erhalte die Karteikarte (Card) aus der HashMap per UUID
        return SpacedRepetitionApp.getInstance().getCards().get(uuid);
    }

    /**
     * Erhalten des Vorderseitentexts der Karteikarte
     * @return der Vorderseitentext
     */
    public String getFront() {
        return this.front;
    }

    /**
     * Erhalten des Rückseitentexts der Karteikarte
     * @return der Rückseitentext
     */
    public String getBack() {
        return this.back;
    }

    /**
     * Erhalten der UUID der Karteikarte
     * @return die UUID
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Erhalten des Zeitpunkts, zu dem die Karteikarte erstellt wurde
     * @return der Long in Millisekunden
     */
    public Long getCreated() {
        return this.created;
    }
}
