package com.ertb.repositories;

import com.ertb.enumerations.TicketStatus;
import com.ertb.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByUserUserIdAndTicketStatus(String userId, TicketStatus status);

    List<Ticket> findByEventEventIdAndUserUserId(String eventId, String userId);

    List<Ticket> findByEventEventIdAndUserUserIdAndTicketStatus(String eventId, String userId, TicketStatus status);

    List<Ticket> findByTicketStatusAndExpiryDate(TicketStatus status, LocalDate expiryDate);

    Ticket findByTicketNumberAndEventEventIdAndTicketStatus(int ticketNumber, String eventId, TicketStatus status);
}
