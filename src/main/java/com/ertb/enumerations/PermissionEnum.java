package com.ertb.enumerations;

import lombok.Getter;

@Getter
public enum PermissionEnum {

    // Admin Permissions
    CREATE_EVENT,
    UPDATE_EVENT,
    DELETE_EVENT,
    VIEW_ALL_EVENTS,
    VIEW_ALL_BOOKINGS,

    // User Permissions
    BOOK_TICKET,
    CANCEL_TICKET,
    VIEW_MY_TICKETS,
    MAKE_PAYMENT,
}
