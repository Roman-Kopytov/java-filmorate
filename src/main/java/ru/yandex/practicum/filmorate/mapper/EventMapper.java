package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dao.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {
    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .timestamp(event.getTimestamp().toInstant().toEpochMilli())
                .eventType(event.getEventType())
                .eventId(event.getEventId())
                .userId(event.getUserId())
                .operation(event.getOperation())
                .entityId(event.getEntityId())
                .build();
    }
}
