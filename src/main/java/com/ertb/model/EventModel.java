package com.ertb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalDate startDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String eventStatus;

    private int availableTicket;

    private double ticketPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int soldOutTicket;
}
