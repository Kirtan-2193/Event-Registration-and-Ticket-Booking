package com.ertb.controllers;

import com.ertb.model.BookedEvent;
import com.ertb.model.MessageModel;
import com.ertb.model.TicketRequest;
import com.ertb.model.UserTicket;
import com.ertb.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<BookedEvent> bookTicket(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.bookedTicket(ticketRequest));
    }

    @GetMapping
    public ResponseEntity<UserTicket> getAllTicketByUser(@RequestParam String userId) {
        return ResponseEntity.ok(ticketService.getTickets(userId));
    }

    @PutMapping
    public ResponseEntity<MessageModel> cancelTickets(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.refundTicket(ticketRequest));
    }
}
