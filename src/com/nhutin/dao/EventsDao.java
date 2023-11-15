package com.nhutin.dao;

import com.nhutin.model.Events;
import com.nhutin.helper.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventsDao {

    public void addEvent(Events event) {
        String sql = "INSERT INTO Events (EventName, Location, EventDate, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                event.getEventName(),
                event.getLocation(),
                event.getEventDate(),
                event.getStartTime(),
                event.getEndTime());
    }

    public List<Events> getAllEvents() {
        String sql = "SELECT * FROM Events";
        return select(sql);
    }

    public Events getEventById(int eventID) {
        String sql = "SELECT * FROM Events WHERE EventID=?";
        List<Events> list = select(sql, eventID);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void updateEvent(Events event) {
        String sql = "UPDATE Events SET EventName=?, Location=?, EventDate=?, StartTime=?, EndTime=? WHERE EventID=?";
        JdbcHelper.executeUpdate(sql,
                event.getEventName(),
                event.getLocation(),
                event.getEventDate(),
                Timestamp.valueOf(event.getStartTime()),
                Timestamp.valueOf(event.getEndTime()),
                event.getEventID());
    }

    public void deleteEvent(int eventID) {
        String sql = "DELETE FROM Events WHERE EventID=?";
        JdbcHelper.executeUpdate(sql, eventID);
    }

    private List<Events> select(String sql, Object... args) {
        List<Events> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    Events event = readFromResultSet(rs);
                    list.add(event);
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

    private Events readFromResultSet(ResultSet rs) throws SQLException {
        
        Events event = new Events();
        event.setEventID(rs.getInt("EventID"));
        event.setEventName(rs.getString("EventName"));
        event.setLocation(rs.getString("Location"));
        event.setEventDate(rs.getDate("EventDate"));
        event.setStartTime(rs.getTimestamp("StartTime").toLocalDateTime());
        event.setEndTime(rs.getTimestamp("EndTime").toLocalDateTime());
        return event;
    }
}
