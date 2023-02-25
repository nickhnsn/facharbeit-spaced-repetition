package de.nickhansen.spacedrepetition.software.card;

import de.nickhansen.spacedrepetition.api.algorithm.FSRSAlgorithm;
import de.nickhansen.spacedrepetition.api.algorithm.LeitnerAlgorithm;
import de.nickhansen.spacedrepetition.api.algorithm.SM2Algorithm;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSRating;
import de.nickhansen.spacedrepetition.api.algorithm.fsrs.FSRSState;
import de.nickhansen.spacedrepetition.api.algorithm.result.FSRSAlgorithmResult;
import de.nickhansen.spacedrepetition.api.algorithm.result.LeitnerAlgorithmResult;
import de.nickhansen.spacedrepetition.api.algorithm.result.SM2AlgorithmResult;
import de.nickhansen.spacedrepetition.software.SpacedRepetitionApp;
import de.nickhansen.spacedrepetition.software.gui.view.LearnView;
import de.nickhansen.spacedrepetition.software.util.AlgorithmType;
import de.nickhansen.spacedrepetition.software.util.Queue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * Der CardScheduler verwaltet die Warteschlange an Karteikarten, die wiederholt werden müssen
 *
 * @author Nick Hansen
 * @version 25.02.2023
 */
public class CardScheduler {

    private Queue<Card> queue;
    private AlgorithmType type;

    /**
     * Konstruktor des CardSchedulers
     * @param type der Algorithmustyp, für welchen diese Warteschlange ist
     */
    public CardScheduler(AlgorithmType type) {
        this.queue = new Queue<>();
        this.type = type;

        this.queueDueCards();
        this.updateGUI();
    }

    /**
     * Setzt die Karteikarten in die Warteschlange, deren Fälligkeitsdatum überschritten ist
     */
    public void queueDueCards() {
        // Leere die Queue
        while (!this.queue.isEmpty()) {
            this.queue.dequeue();
        }

        // Finde Karten mit überschrittenem Fälligkeitsdatum
        SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().syncQuery("SELECT * FROM " + this.type.getDatabaseTable() + " WHERE next_repetition <= " + System.currentTimeMillis(), resultSet -> {
            try {
                while (resultSet.next()) {
                    Card dueCard = Card.getByUUID(UUID.fromString(resultSet.getString("card_uuid")));
                    System.out.println("[CardScheduler] Due time for card " + dueCard.getUUID().toString() + " exceeded (algorithm: " + this.type + ")");
                    this.queue.enqueue(dueCard);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Aktualisiert das GUI je nach Zustand der Queue
     */
    public void updateGUI() {
        // Über das MainView die LearnView erhalten
        LearnView learnView = SpacedRepetitionApp.getInstance().getMainView().getLearnView();

        if (this.queue.isEmpty()) {
            learnView.getFrontDataLabel().setText("Aktuell keine Karteikarten zum Wiederholen verfügbar");
            if (SpacedRepetitionApp.getInstance().getCards().size() == 0) {
                learnView.getBackDataLabel().setText("Bitte erstelle Karteikarten!");
                SpacedRepetitionApp.getInstance().getMainView().getLearnView().getUpdateButton().setVisible(false);
                SpacedRepetitionApp.getInstance().getMainView().getLearnView().updateButtonPanel(0);
            } else {
                try {
                    ResultSet rs = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().query("SELECT next_repetition FROM " + this.type.getDatabaseTable() + " ORDER BY next_repetition ASC LIMIT 1");
                    if (rs.next()) {
                        learnView.getBackDataLabel().setText("Die nächste Karteikarte ist zur Wiederholung fällig am: " + new Date(rs.getLong("next_repetition")));
                    }
                } catch (SQLException e) {
                    learnView.getBackDataLabel().setText("Bitte komme später noch einmal zur Wiederholung vorbei");
                    e.printStackTrace();
                }
                SpacedRepetitionApp.getInstance().getMainView().getLearnView().getUpdateButton().setVisible(true);
                SpacedRepetitionApp.getInstance().getMainView().getLearnView().updateButtonPanel(0);
            }
        } else {
            learnView.getFrontDataLabel().setText(this.queue.front().getFront());
            learnView.getBackDataLabel().setText(this.queue.front().getBack());
            SpacedRepetitionApp.getInstance().getMainView().getLearnView().getUpdateButton().setVisible(false);
            SpacedRepetitionApp.getInstance().getMainView().getLearnView().updateButtonPanel(this.type.getRatingButtons());
        }
    }

    /**
     * Veränderung der Queue sowie Karteikartendaten bei der Bewertung einer Karteikarte im Wiederholungsprozess
     * @param rating die Qualität der Wiederholung (Intervall von dem int variiert je nach Algorithmus)
     */
    public void onRating(int rating) {
        Card ratedCard = this.queue.front();

        if (ratedCard != null) {
            try {
                PreparedStatement ps = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().prepare("SELECT * FROM " + this.type.getDatabaseTable() + " WHERE card_uuid = ?");
                ps.setString(1, ratedCard.getUUID().toString());
                ResultSet rs = ps.executeQuery();
                rs.next();

                if (this.type == AlgorithmType.LEITNER_SYSTEM) {
                    LeitnerAlgorithm.LeitnerAlgorithmBuilder builder = LeitnerAlgorithm.builder()
                            .boxId(rs.getInt("box_id"));
                    if (rating == 0) {
                        builder.retrievalSuccessful(false);
                    } else {
                        builder.retrievalSuccessful(true);
                    }

                    LeitnerAlgorithm algorithm = builder.build();
                    LeitnerAlgorithmResult result = algorithm.calc();

                    // Berücksichtigung von Leitners Vorgabe, dass Karten ab Box 5 nicht mehr in der Lernkartei vorkommen
                    // Karteikarten in einer Box ab 5 gelten als "fertig gelernt"
                    if (result.toBeRemoved()) {
                        SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().syncUpdate("DELETE FROM " + this.type.getDatabaseTable() + " WHERE card_uuid = '" + ratedCard.getUUID() + "'");
                    } else {
                        PreparedStatement db = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().prepare("UPDATE " + this.type.getDatabaseTable() + " SET box_id = ?, day_interval = ?, next_repetition = ? WHERE card_uuid = ?");
                        db.setInt(1, result.getBoxId());
                        db.setInt(2, result.getInterval());
                        db.setLong(3, result.getNextRepetitionTime());
                        db.setString(4, ratedCard.getUUID().toString());
                        db.execute();
                    }
                } else if (this.type == AlgorithmType.SUPERMEMO_2) {
                    SM2Algorithm algorithm = SM2Algorithm.builder()
                            .quality(rating + 1) // ansonsten wäre die Qualität zwischen 0 und 4; bei dem Algorithmus muss die Qualität jedoch zwischen 1 und 5 liegen
                            .easinessFactor(rs.getFloat("easiness_factor"))
                            .interval(rs.getInt("day_interval"))
                            .repetitions(rs.getInt("next_repetition"))
                            .build();

                    SM2AlgorithmResult result = algorithm.calc();

                    PreparedStatement db = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().prepare("UPDATE " + this.type.getDatabaseTable() + " SET repetitions = ?, easiness_factor = ?, day_interval = ?, next_repetition = ? WHERE card_uuid = ?");
                    db.setInt(1, result.getRepetitions());
                    db.setFloat(2, result.getEasinessFactor());
                    db.setInt(3, result.getInterval());
                    db.setLong(4, result.getNextRepetitionTime());
                    db.setString(5, ratedCard.getUUID().toString());
                    db.execute();
                } else if (this.type == AlgorithmType.FREE_SPACED_REPETITION_SCHEDULER) {
                    FSRSAlgorithm algorithm = FSRSAlgorithm.builder()
                            .rating(FSRSRating.values()[rating])
                            .stability(rs.getFloat("stability"))
                            .difficulty(rs.getFloat("difficulty"))
                            .elapsedDays(rs.getInt("elapsed_days"))
                            .repetitions(rs.getInt("repetitions"))
                            .state(FSRSState.valueOf(rs.getString("state")))
                            .lastReview(rs.getLong("last_review"))
                            .build();

                    FSRSAlgorithmResult result = algorithm.calc();
                    PreparedStatement db = SpacedRepetitionApp.getInstance().getDatabaseAdapter().getMySQL().prepare("UPDATE " + this.type.getDatabaseTable() + " SET repetitions = ?, difficulty = ?, stability = ?, elapsed_days = ?, repetitions = ?, state = ?, day_interval = ?, next_repetition = ?, last_review = ? WHERE card_uuid = ?");
                    db.setLong(1, result.getRepetitions());
                    db.setFloat(2, result.getDifficulty());
                    db.setFloat(3, result.getStability());
                    db.setInt(4, result.getElapsedDays());
                    db.setInt(5, result.getRepetitions());
                    db.setString(6, result.getState().toString());
                    db.setInt(7, result.getInterval());
                    db.setLong(8, result.getNextRepetitionTime());
                    db.setLong(9, result.getLastReview());
                    db.setString(10, ratedCard.getUUID().toString());
                    db.execute();
                }

                System.out.println("[CardScheduler] Card " + ratedCard.getUUID() + " rated with " + rating + " (algorithm: " + this.type + ")");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        this.queue.dequeue();
        if (this.queue.isEmpty()) {
            this.queueDueCards();
        }
        this.updateGUI();
    }

    /**
     * Erhalten des Typs, für den dieser CardScheduler ist
     * @return der Typ des CardSchedulers
     */
    public AlgorithmType getType() {
        return this.type;
    }
}
