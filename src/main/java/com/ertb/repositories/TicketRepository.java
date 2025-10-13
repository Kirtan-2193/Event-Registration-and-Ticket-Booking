package com.ertb.repositories;

import com.ertb.enumerations.TicketStatus;
import com.ertb.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByUserUserIdAndTicketStatus(String userId, TicketStatus status);

    List<Ticket> findByEventEventIdAndUserUserId(String eventId, String userId);

    Ticket findByTicketId(String ticketId);
}
