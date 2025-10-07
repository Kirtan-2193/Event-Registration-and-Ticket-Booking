package com.ertb.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookedEvent {

    private String eventId;

    private String eventName;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private TicketModel ticket;
}
