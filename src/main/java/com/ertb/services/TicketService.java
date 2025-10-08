package com.ertb.services;

import com.ertb.enumerations.Status;
import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.mappers.EventMapper;
import com.ertb.mappers.TicketMapper;
import com.ertb.mappers.UserMapper;
import com.ertb.model.BookedEvent;
import com.ertb.model.TicketModel;
import com.ertb.model.UserTicket;
import com.ertb.model.entities.Event;
import com.ertb.model.entities.Ticket;
import com.ertb.model.entities.User;
import com.ertb.repositories.EventRepository;
import com.ertb.repositories.TicketRepository;
import com.ertb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final TicketMapper ticketMapper;

    private final EventMapper eventMapper;

    private final UserMapper userMapper;


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
        ticketModel.setPrice(event.getTicketPrice());
        ticketModel.setTotalPrice(event.getTicketPrice() * allocateTicket);
        bookedEvent.setTicket(ticketModel);

        return bookedEvent;
    }

    public UserTicket getTickets(String userId) {

        List<Ticket> ticketList = ticketRepository.findByUserUserIdAndStatus(userId, Status.ACTIVE);

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User Not Found"));
        List<Event> eventList = new ArrayList<>();
        ticketList.forEach(ticket -> {
            Event event = eventRepository.findByEventId(ticket.getEvent().getEventId()).orElse(null);
            eventList.add(event);
        });

        UserTicket userTicket = userMapper.userToUserTicket(user);
        List<BookedEvent> bookedEventList = eventMapper.eventListToBookedEventList(eventList);

        bookedEventList.forEach(bookedEvent -> {
            TicketModel ticketModel = ticketMapper.ticketToTicketModel(ticketRepository.findByEventEventId(bookedEvent.getEventId()));
            bookedEvent.setTicket(ticketModel);
        });
        userTicket.setEvent(bookedEventList);

        return userTicket;
    }
}
