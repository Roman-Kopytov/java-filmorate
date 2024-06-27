package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Event {
    long userId;
    long eventId;
    EventType eventType;
    Operation operation;
    Timestamp timestamp;
    long entityId;
}
