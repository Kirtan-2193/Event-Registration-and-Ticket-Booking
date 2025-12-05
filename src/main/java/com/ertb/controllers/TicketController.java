package com.ertb.controllers;

import com.ertb.model.BookedEvent;
import com.ertb.model.MessageModel;
import com.ertb.model.TicketRequest;
import com.ertb.model.UserTicket;
import com.ertb.services.TicketService;
import com.ertb.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;



    @PostMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).BOOK_TICKET)")
    public ResponseEntity<BookedEvent> bookTicket(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.bookedTicket(ticketRequest, SecurityUtils.getCurrentUserEmail()));
    }

    @GetMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).VIEW_MY_TICKETS)")
    public ResponseEntity<UserTicket> getAllTicketByUser() {
        return ResponseEntity.ok(ticketService.getTickets(SecurityUtils.getCurrentUserEmail()));
    }

    @PutMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).CANCEL_TICKET)")
    public ResponseEntity<MessageModel> cancelTickets(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.refundTicket(ticketRequest));
    }

    @PutMapping("/check-in")
    public ResponseEntity<MessageModel> ticketCheck(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.ticketChecking(ticketRequest));
    }
}
