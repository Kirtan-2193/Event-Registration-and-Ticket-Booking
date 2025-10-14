package com.ertb.services;

import com.ertb.enumerations.TicketStatus;
import com.ertb.exceptions.DataNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final TicketMapper ticketMapper;

    private final EventMapper eventMapper;

    private final UserMapper userMapper;


    public BookedEvent bookedTicket(String eventId, String userId, int bookedTicket) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User Not Found"));
        Event event = eventRepository.findByEventId(eventId).orElseThrow(
                () -> new DataNotFoundException("Event Not Found"));

        List<Ticket> ticketList = new ArrayList<>();
        for (int ticketBook = 0; ticketBook < bookedTicket; ticketBook++) {
            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setEvent(event);
            ticket.setExpiryDate(event.getEndDate());
            ticket.setExpiryTime(event.getEndTime());
            ticket.setTicketStatus(TicketStatus.BOOKED);
            ticket.setTicketNumber(event.getSoldOutTicket() + 1);
            ticketRepository.save(ticket);
            event.setSoldOutTicket(event.getSoldOutTicket() + 1);
            eventRepository.save(event);
            ticketList.add(ticket);
        }

        BookedEvent bookedEvent = eventMapper.eventToBookedEvent(event);
        List<TicketModel> ticketModelList = ticketMapper.ticketListToTicketModelList(ticketList);
        bookedEvent.setTicket(ticketModelList);
        bookedEvent.setTotalPrice(event.getTicketPrice() *  bookedTicket);

        return bookedEvent;
    }



    public UserTicket getTickets(String userId) {

        // 1️⃣ Get user info
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found with ID: " + userId));

        // 2️⃣ Get all tickets of this user
        List<Ticket> tickets = ticketRepository.findByUserUserIdAndTicketStatus(userId, TicketStatus.BOOKED);
        if (tickets.isEmpty()) {
            throw new DataNotFoundException("No tickets found for user: " + userId);
        }

        // 3️⃣ Group tickets by event
        Map<Event, List<Ticket>> ticketsByEvent = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getEvent));
        List<Event> eventList = new ArrayList<>();
        for (Map.Entry<Event, List<Ticket>> entry : ticketsByEvent.entrySet()) {
            Event event = entry.getKey();
            eventList.add(event);
        }

        List<BookedEvent> bookedEventList = eventMapper.eventListToBookedEventList(eventList);
        bookedEventList.forEach(bookedEvent -> {
            List<Ticket> ticketList = ticketRepository.findByEventEventIdAndUserUserId(bookedEvent.getEventId(), userId);
            List<TicketModel> ticketModelList = ticketMapper.ticketListToTicketModelList(ticketList);
            bookedEvent.setTotalPrice(bookedEvent.getTicketPrice() * ticketList.size());
            bookedEvent.setTicket(ticketModelList);
        });

        UserTicket userTicket = userMapper.userToUserTicket(user);
        userTicket.setEvent(bookedEventList);

        return userTicket;
    }



    @Scheduled(cron = "0 0/30 * * * ?")
    public void expiredTicket() {
        log.info("Ticket expired method triggered at "+ LocalDateTime.now());
        List<Ticket> ticketList = ticketRepository.findByTicketStatusAndExpiryDate(TicketStatus.BOOKED, LocalDate.now());
        ticketList.forEach(ticket -> {
            if(ticket.getExpiryTime().isBefore(LocalTime.now()) || ticket.getExpiryTime().equals(LocalTime.now())) {
                ticket.setTicketStatus(TicketStatus.EXPIRED);
                ticketRepository.save(ticket);
            }
            log.info("ticket has been expired.");
        });
    }
}
