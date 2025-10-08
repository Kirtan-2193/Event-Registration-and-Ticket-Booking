package com.ertb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String eventId;

    @NotNull(message = "Event Name is required")
    private String eventName;

    @NotNull(message = "description is required")
    private String description;

    @NotNull(message = "Venue is required")
    private String venue;

    @FutureOrPresent(message = "Start date must be today or a future date")
    @NotNull(message = "Start date is required")
    private LocalDateTime  startDateTime;

    @NotNull(message = "End date is required")
    private LocalDateTime endDateTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String eventStatus;

    private int availableTicket;

    private double ticketPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int soldOutTicket;
}
