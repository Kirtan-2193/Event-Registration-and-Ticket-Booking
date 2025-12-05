package com.ertb.enumerations;

import lombok.Getter;

@Getter
public enum PermissionEnum {

    // Admin
    VIEW_ALL_BOOKINGS,
    MANAGE_USER,

    // User
    BOOK_TICKET,
    VIEW_MY_TICKETS,
    MAKE_PAYMENT,

    // User And Admin
    CANCEL_TICKET,

    //Organizer
    TICKET_CHECK_IN,

    //Admin and Organizer
    CREATE_EVENT,
    UPDATE_EVENT,
    DELETE_EVENT,
    VIEW_ALL_EVENTS,
    MANAGE_TICKETS
}
