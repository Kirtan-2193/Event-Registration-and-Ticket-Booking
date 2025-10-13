package com.ertb.enumerations;

public enum TicketStatus {

    AVAILABLE,      // Ticket is available for booking
    RESERVED,       // Ticket is temporarily reserved (e.g., during payment process)
    BOOKED,         // Ticket is successfully booked
    CANCELLED,      // Ticket booking was canceled
    EXPIRED,        // Ticket validity period is over
    USED            // Ticket has been used for entry
}
