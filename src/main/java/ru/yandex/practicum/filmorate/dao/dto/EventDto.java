package ru.yandex.practicum.filmorate.dao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long userId;
    long eventId;
    String eventType;
    String operation;
    long timestamp;
    long entityId;
}
