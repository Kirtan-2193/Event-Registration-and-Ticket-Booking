package com.ertb.services;

import com.ertb.enumerations.EventStatus;
import com.ertb.enumerations.PaymentStatus;
import com.ertb.enumerations.TicketStatus;
import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.exceptions.PaymentValidationException;
import com.ertb.mappers.EventMapper;
import com.ertb.mappers.PaymentMapper;
import com.ertb.mappers.TicketMapper;
import com.ertb.mappers.UserMapper;
import com.ertb.model.BookedEvent;
import com.ertb.model.MessageModel;
import com.ertb.model.PaymentClientModel;
import com.ertb.model.PaymentResponse;
import com.ertb.model.TicketModel;
import com.ertb.model.TicketRequest;
import com.ertb.model.UserTicket;
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


    public BookedEvent bookedTicket(TicketRequest ticketRequest, String email) {
        User user = userRepository.findByEmail(email);
        Event event = eventRepository.findByEventIdAndEventStatusIn(ticketRequest.getEventId(), List.of(EventStatus.OPEN, EventStatus.UPCOMING)).orElseThrow(
                () -> new DataNotFoundException("Event Not Found or Event is ended now"));

        if (event.getSoldOutTicket() == event.getAvailableTicket()) {
            throw new DataValidationException("Tickets are sold out");
        }

        int activeTicket = event.getAvailableTicket() - event.getSoldOutTicket();
        if (activeTicket < ticketRequest.getBookedTicket()) {
            throw new DataValidationException("Only " + activeTicket + " tickets are available");
        }

        double totalAmount = event.getTicketPrice() * ticketRequest.getBookedTicket();
        PaymentClientModel paymentClientModel = paymentService.makePayment(ticketRequest.getAccountNumber(), totalAmount);

        if (!"SUCCESS".equals(paymentClientModel.getPaymentStatus())){
            throw new PaymentValidationException("Payment Failed");
        }

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransactionReferenceId(paymentClientModel.getPaymentId());
        paymentResponse.setAmount(totalAmount);
        paymentResponse.setPaymentStatus(paymentClientModel.getPaymentStatus());
        paymentResponse.setMessage("Payment for "+event.getEventName());

        Payment payment = paymentMapper.paymentResponseToPayment(paymentResponse);
        payment.setUser(user);
        payment.setEvent(event);
        paymentRepository.save(payment);

        TicketModel ticketModel = addTicket(user, event, ticketRequest.getBookedTicket(), TicketStatus.BOOKED);

        BookedEvent bookedEvent = eventMapper.eventToBookedEvent(event);
        bookedEvent.setTicket(ticketModel);
        bookedEvent.setTotalPrice(totalAmount);

        return bookedEvent;
    }



    public MessageModel refundTicket(TicketRequest ticketRequest) {
        MessageModel messageModel = new MessageModel();

        Payment payment = paymentRepository.findByUserUserIdAndEventEventId(ticketRequest.getUserId(), ticketRequest.getEventId());
        PaymentClientModel paymentClientModel = paymentService.refundPayment(payment.getTransactionReferenceId());
        if (!"REFUNDED".equals(paymentClientModel.getPaymentStatus())) {
            throw new DataValidationException("Refund is Failed");
        }
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        Payment savePayment = paymentRepository.save(payment);

        if (savePayment.getPaymentStatus().equals(PaymentStatus.REFUNDED)) {
            List<Ticket> ticketList = ticketRepository.findByEventEventIdAndUserUserIdAndTicketStatus(
                    ticketRequest.getEventId(), ticketRequest.getUserId(), TicketStatus.BOOKED);
            ticketList.forEach(ticket -> {
                ticket.setTicketStatus(TicketStatus.CANCELLED);
                ticketRepository.save(ticket);
            });
            int cancelBookTicket = ticketList.size();

            Event event = eventRepository.findByEventId(ticketRequest.getEventId()).orElse(null);
            event.setSoldOutTicket(event.getSoldOutTicket() - cancelBookTicket);
            eventRepository.save(event);

            messageModel.setMessage(ticketList.size()+" Tickets are Successfully cancelled");
        } else {
            messageModel.setMessage("Tickets are not cancelled");
        }
        return messageModel;
    }



    public UserTicket getTickets(String email) {

        // Get user info
        User user = userRepository.findByEmail(email);

        // Get all tickets of this user
        List<Ticket> tickets = ticketRepository.findByUserUserIdAndTicketStatus(user.getUserId(), TicketStatus.BOOKED);
        if (tickets.isEmpty()) {
            throw new DataNotFoundException("No tickets found for user: " + user.getUserId());
        }

        // Group tickets by event
        Map<Event, List<Ticket>> ticketsByEvent = tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getEvent));
        List<Event> eventList = new ArrayList<>();
        for (Map.Entry<Event, List<Ticket>> entry : ticketsByEvent.entrySet()) {
            Event event = entry.getKey();
            eventList.add(event);
        }

        List<BookedEvent> bookedEventList = eventMapper.eventListToBookedEventList(eventList);
        bookedEventList.forEach(bookedEvent -> {
             Ticket ticket = ticketRepository.findByEventEventIdAndUserUserId(bookedEvent.getEventId(), user.getUserId());
             TicketModel ticketModel = ticketMapper.ticketToTicketModel(ticket);
            bookedEvent.setTotalPrice(bookedEvent.getTicketPrice() * ticket.getAllocatedTicket());
            bookedEvent.setTicket(ticketModel);
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


    public TicketModel addTicket(User user, Event event, int bookedTicket, TicketStatus status) {

            Ticket ticket = new Ticket();
            ticket.setUser(user);
            ticket.setEvent(event);
            ticket.setExpiryDate(event.getEndDate());
            ticket.setExpiryTime(event.getEndTime());
            ticket.setTicketStatus(status);
            ticket.setAllocatedTicket(bookedTicket);
            ticketRepository.save(ticket);
            event.setSoldOutTicket(event.getSoldOutTicket() + 1);
            eventRepository.save(event);

        return ticketMapper.ticketToTicketModel(ticket);
    }

    public MessageModel ticketChecking(TicketRequest ticketRequest) {
        MessageModel messageModel = new MessageModel();
        /*Ticket ticket = ticketRepository.findByTicketNumberAndEventEventIdAndTicketStatus(ticketRequest.getTicketNumber(),
                                                                            ticketRequest.getEventId(),
                                                                            TicketStatus.BOOKED);
        if(ticket == null) {
            throw new DataNotFoundException("Ticket not found or not booked");
        }

        ticket.setTicketStatus(TicketStatus.USED);
        ticketRepository.save(ticket);*/
        messageModel.setMessage("Allow to check in");
        return messageModel;
    }
}
