package com.ertb.model.entities;

import com.ertb.enumerations.EventStatus;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class Event {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid")
    @Column(name = "event_id")
    private String eventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "description", length = 350)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_data", nullable = false)
    private LocalDate endDate;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;

    @Column(name = "available_ticket")
    private int availableTicket;

    @Column(name = "ticket_price")
    private double ticketPrice;

    @Column(name = "sold_out_ticket")
    private int soldOutTicket;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
