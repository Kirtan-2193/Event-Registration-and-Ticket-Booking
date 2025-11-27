package com.ertb.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookedEvent {

    private String eventId;

    private String eventName;

    private LocalDate startDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate endDate;

    private double ticketPrice;

    private TicketModel ticket;

    private double totalPrice;
}
