package com.ertb.controllers;

import com.ertb.model.BookedEvent;
import com.ertb.model.EventModel;
import com.ertb.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<BookedEvent> addEvent(@RequestParam String eventId,
                                                @RequestParam String userId,
                                                @RequestParam int allocateTicket) {
        return ResponseEntity.ok(ticketService.bookedTicket(eventId, userId, allocateTicket));
    }
}
