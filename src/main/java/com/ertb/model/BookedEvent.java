package com.ertb.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookedEvent {

    private String eventId;

    private String eventName;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private double ticketPrice;

    private List<TicketModel> ticket;

    private double totalPrice;
}
