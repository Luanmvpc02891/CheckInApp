package com.nhutin.dao;

import com.nhutin.model.Participants;
import com.nhutin.helper.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ParticipantsDao {

    public void addParticipant(Participants participant) {
        String sql = "INSERT INTO Participants (EventID, FirstName, LastName, Barcode, Email) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                participant.getEventID(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getBarcode(),
                participant.getEmail());
    }

    public List<Participants> getAllParticipants() {
        String sql = "SELECT * FROM Participants";
        return select(sql);
    }

    public Participants getParticipantById(int participantID) {
        String sql = "SELECT * FROM Participants WHERE ParticipantID=?";
        List<Participants> list = select(sql, participantID);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void updateParticipant(Participants participant) {
        String sql = "UPDATE Participants SET EventID=?, FirstName=?, LastName=?, Barcode=?, Email=? WHERE ParticipantID=?";
        JdbcHelper.executeUpdate(sql,
                participant.getEventID(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getBarcode(),
                participant.getEmail(),
                participant.getParticipantID());
    }

    public void deleteParticipant(int participantID) {
        String sql = "DELETE FROM Participants WHERE ParticipantID=?";
        JdbcHelper.executeUpdate(sql, participantID);
    }

    private List<Participants> select(String sql, Object... args) {
        List<Participants> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    Participants participant = readFromResultSet(rs);
                    list.add(participant);
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.getStatement().getConnection().close();
                    } catch (SQLException ex) {
                        // Handle exception when closing connection
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private Participants readFromResultSet(ResultSet rs) throws SQLException {
        Participants participant = new Participants();
        participant.setParticipantID(rs.getInt("ParticipantID"));
        participant.setEventID(rs.getInt("EventID"));
        participant.setFirstName(rs.getString("FirstName"));
        participant.setLastName(rs.getString("LastName"));
        participant.setBarcode(rs.getString("Barcode"));
        participant.setEmail(rs.getString("Email"));
        return participant;
    }
}
