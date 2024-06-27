package ru.yandex.practicum.filmorate.dao.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcEventRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public void saveEvent(long userId, long entityId, EventType eventType, Operation operation) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put("userId", userId);
        eventValues.put("entityId", entityId);
        eventValues.put("eventType", eventType.toString());
        eventValues.put("operation", operation.toString());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource(eventValues);
        String query = "INSERT INTO EVENT (USER_ID,ENTITY_ID,EVENT_TYPE,OPERATION)" +
                " VALUES(:userId,:entityId,:eventType,:operation)";
        jdbcOperations.update(query, params, keyHolder);
    }
}
