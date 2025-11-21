package com.ertb.model.entities;

import com.ertb.enumerations.TicketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @Column(name = "ticket_id")
    private String ticketId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "booking_date")
    private LocalDate bookingDate = LocalDate.now();

    @Column(name = "booking_time")
    private LocalTime bookingTime = LocalTime.now();

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "expiry_time")
    private LocalTime expiryTime;

    @Column(name = "allocated_ticket")
    private int allocatedTicket;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status")
    private TicketStatus ticketStatus;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
