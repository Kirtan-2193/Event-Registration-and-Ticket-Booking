package com.ertb.model;

import lombok.Data;

@Data
public class TicketModel {

    private String ticketId;

    private double price;

    private int allocateTicket;

    private double totalPrice;
}
