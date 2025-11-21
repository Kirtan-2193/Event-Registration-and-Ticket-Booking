package com.ertb.model;

import lombok.Data;

@Data
public class TicketRequest {

    private String eventId;

    private String ticketId;

    private int bookedTicket;

    private Long accountNumber;
}
