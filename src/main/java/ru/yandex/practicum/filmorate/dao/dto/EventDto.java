package ru.yandex.practicum.filmorate.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

@Data
@Builder
public class EventDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long userId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long eventId;
    EventType eventType;
    Operation operation;
    long timestamp;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long entityId;
}
