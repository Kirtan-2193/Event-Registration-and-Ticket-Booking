package com.ertb.services;

import com.ertb.enumerations.EventStatus;
import com.ertb.enumerations.PaymentStatus;
import com.ertb.enumerations.TicketStatus;
import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.mappers.EventMapper;
import com.ertb.mappers.PaymentMapper;
import com.ertb.mappers.TicketMapper;
import com.ertb.mappers.UserMapper;
import com.ertb.model.*;
import com.ertb.model.entities.Event;
import com.ertb.model.entities.Payment;
import com.ertb.model.entities.Ticket;
import com.ertb.model.entities.User;
import com.ertb.repositories.EventRepository;
import com.ertb.repositories.PaymentRepository;
import com.ertb.repositories.TicketRepository;
import com.ertb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    private final PaymentRepository paymentRepository;

    private final TicketMapper ticketMapper;

    private final EventMapper eventMapper;

    private final UserMapper userMapper;

    private final PaymentMapper paymentMapper;

    private final PaymentService paymentService;


    public BookedEvent bookedTicket(String eventId, String userId, int bookedTicket, Long accountNumber) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new DataNotFoundException("User Not Found"));
        Event event = eventRepository.findByEventIdAndEventStatusIn(eventId, List.of(EventStatus.OPEN, EventStatus.UPCOMING)).orElseThrow(
                () -> new DataNotFoundException("Event Not Found or Event is ended now"));

        if (event.getSoldOutTicket() == event.getAvailableTicket()) {
            throw new DataValidationException("Tickets are sold out");
        }

        int activeTicket = event.getAvailableTicket() - event.getSoldOutTicket();
        if (activeTicket < bookedTicket) {
            throw new DataValidationException("Only " + activeTicket + " tickets are available");
        }

        double totalAmount = event.getTicketPrice() * bookedTicket;
        PaymentClientModel paymentClientModel = paymentService.makePayment(accountNumber, totalAmount);

        if (!"SUCCESS".equals(paymentClientModel.getPaymentStatus())){
            throw new DataValidationException("Payment Failed");
        }

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionReferenceId(paymentClientModel.getPaymentId());
        paymentResponse.setAmount(totalAmount);
        paymentResponse.setPaymentStatus(paymentClientModel.getPaymentStatus());
        paymentResponse.setMessage("Payment for "+event.getEventName());

        Payment payment = paymentMapper.paymentResponseToPayment(paymentResponse);
        payment.setUser(user);
        paymentRepository.save(payment);

        List<TicketModel> ticketModelList = addTicket(user, event, bookedTicket);

        BookedEvent bookedEvent = eventMapper.eventToBookedEvent(event);
        bookedEvent.setTicket(ticketModelList);
        bookedEvent.setTotalPrice(totalAmount);

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


    public List<TicketModel> addTicket(User user, Event event, int bookedTicket) {
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
        return ticketMapper.ticketListToTicketModelList(ticketList);
    }
}
