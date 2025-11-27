package com.ertb.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TicketModel {

    private String ticketId;

    private int allocatedTicket;

    private LocalDate expiryDate;

    private LocalTime expiryTime;
}
