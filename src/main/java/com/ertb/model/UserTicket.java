package com.ertb.model;

import lombok.Data;

import java.util.List;

@Data
public class UserTicket {

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private List<BookedEvent> event;
}
