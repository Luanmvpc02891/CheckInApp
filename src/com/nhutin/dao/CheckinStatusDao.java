package com.nhutin.dao;

import com.nhutin.model.CheckinStatus;
import com.nhutin.helper.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckinStatusDao {

    public void addCheckinStatus(CheckinStatus checkinStatus) {
        String sql = "INSERT INTO CheckinStatus (ParticipantID, EventID, CheckinTime) VALUES (?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                checkinStatus.getParticipantID(),
                checkinStatus.getEventID(),
                Timestamp.valueOf(checkinStatus.getCheckinTime()));
    }

    public List<CheckinStatus> getAllCheckinStatus() {
        String sql = "SELECT * FROM CheckinStatus";
        return select(sql);
    }

    public CheckinStatus getCheckinStatusById(int checkinID) {
        String sql = "SELECT * FROM CheckinStatus WHERE CheckinID=?";
        List<CheckinStatus> list = select(sql, checkinID);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void updateCheckinStatus(CheckinStatus checkinStatus) {
        String sql = "UPDATE CheckinStatus SET ParticipantID=?, EventID=?, CheckinTime=? WHERE CheckinID=?";
        JdbcHelper.executeUpdate(sql,
                checkinStatus.getParticipantID(),
                checkinStatus.getEventID(),
                Timestamp.valueOf(checkinStatus.getCheckinTime()),
                checkinStatus.getCheckinID());
    }

    public void deleteCheckinStatus(int checkinID) {
        String sql = "DELETE FROM CheckinStatus WHERE CheckinID=?";
        JdbcHelper.executeUpdate(sql, checkinID);
    }

    private List<CheckinStatus> select(String sql, Object... args) {
        List<CheckinStatus> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    CheckinStatus checkinStatus = readFromResultSet(rs);
                    list.add(checkinStatus);
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

    private CheckinStatus readFromResultSet(ResultSet rs) throws SQLException {
        CheckinStatus checkinStatus = new CheckinStatus();
        checkinStatus.setCheckinID(rs.getInt("CheckinID"));
        checkinStatus.setParticipantID(rs.getInt("ParticipantID"));
        checkinStatus.setEventID(rs.getInt("EventID"));
        checkinStatus.setCheckinTime(rs.getTimestamp("CheckinTime").toLocalDateTime());
        return checkinStatus;
    }
}
