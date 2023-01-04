package de.nickhansen.facharbeit.spaced_repetition.card;

import de.nickhansen.facharbeit.spaced_repetition.SpacedRepetitionApp;

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
        UUID uuid = UUID.randomUUID();
        Long created = System.currentTimeMillis(); // Aktueller Zeitpunkt zur Erstellung der Karteikarte

        // Karteikarte in der Datenbank erstellen
        SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().update("INSERT INTO cards (uuid, front, back, created) VALUES ('" + uuid + "', '" + front + "', '" + back + "', '" + created + "')");

        // Karteikarten-Objekt erstellen
        new Card(uuid, front, back, created);
        System.out.println("[Cards] Sucessfully created card " + uuid);
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
