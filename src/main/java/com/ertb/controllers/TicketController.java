package com.ertb.controllers;

import com.ertb.model.BookedEvent;
import com.ertb.model.UserTicket;
import com.ertb.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<BookedEvent> bookTicket(@RequestParam String eventId,
                                                  @RequestParam String userId,
                                                  @RequestParam int bookedTicket,
                                                  @RequestParam Long accountNumber) {
        return ResponseEntity.ok(ticketService.bookedTicket(eventId, userId, bookedTicket, accountNumber));
    }

    @GetMapping
    public ResponseEntity<UserTicket> getAllTicketByUser(@RequestParam String userId) {
        return ResponseEntity.ok(ticketService.getTickets(userId));
    }
}
