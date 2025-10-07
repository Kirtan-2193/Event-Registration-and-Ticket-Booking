package com.ertb.services;

import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.mappers.EventMapper;
import com.ertb.mappers.TicketMapper;
import com.ertb.model.BookedEvent;
import com.ertb.model.TicketModel;
import com.ertb.model.entities.Event;
import com.ertb.model.entities.Ticket;
import com.ertb.model.entities.User;
import com.ertb.repositories.EventRepository;
import com.ertb.repositories.TicketRepository;
import com.ertb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final TicketMapper ticketMapper;

    private final EventMapper eventMapper;


    public BookedEvent bookedTicket(String eventId, String userId, int allocateTicket) {
        Event event = eventRepository.findByEventId(eventId).orElseThrow(
                () -> new DataNotFoundException("Event Not Found"));

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User Not Found"));

        if (event.getAvailableTicket() == event.getSoldOutTicket()) {
            throw new DataValidationException("Sorry!, All Tickets are Sold Out");
        }

        int a = event.getAvailableTicket() -  event.getSoldOutTicket();
        Ticket ticket = new Ticket();

        if (a < allocateTicket) {
            throw new DataValidationException("Sorry!, Only "+a+" Tickets are Available");
        } else {
            ticket.setEvent(event);
            ticket.setUser(user);
            ticket.setAllocateTicket(allocateTicket);
            ticketRepository.save(ticket);
            event.setSoldOutTicket(event.getSoldOutTicket() + allocateTicket);
            eventRepository.save(event);
        }

        BookedEvent bookedEvent = eventMapper.eventToBookedEvent(event);
        TicketModel ticketModel = ticketMapper.ticketToTicketModel(ticket);
        bookedEvent.setTicket(ticketModel);

        return bookedEvent;
    }
}
