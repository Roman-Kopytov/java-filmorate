package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<Event> {
    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = Event.builder()
                .eventType(rs.getString("EVENT_TYPE"))
                .operation(rs.getString("OPERATION"))
                .eventId(rs.getLong("EVENT_ID"))
                .timestamp(rs.getTimestamp("TIMESTAMP"))
                .entityId(rs.getLong("ENTITY_ID"))
                .userId(rs.getLong("USER_ID"))
                .build();
        return event;
    }
}
